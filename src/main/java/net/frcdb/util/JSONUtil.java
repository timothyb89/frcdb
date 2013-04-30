package net.frcdb.util;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.googlecode.objectify.Ref;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import net.frcdb.api.event.Event;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.game.event.GameType;
import net.frcdb.api.game.event.element.GameOPRProvider;
import net.frcdb.api.game.match.Match;
import net.frcdb.api.game.standing.Standing;
import net.frcdb.api.game.team.TeamEntry;
import net.frcdb.api.game.team.element.*;
import net.frcdb.api.team.Team;
import net.frcdb.api.team.TeamStatistics;
import net.frcdb.db.Database;

/**
 *
 * @author tim
 */
public class JSONUtil {
	
	public static void exportTeam(OutputStream out, Team team, boolean entries)
			throws IOException {
		JsonFactory f = new JsonFactory();
		JsonGenerator g = f.createJsonGenerator(out, JsonEncoding.UTF8);
		g.setPrettyPrinter(new DefaultPrettyPrinter());
		
		exportTeam(g, team, entries);
		
		g.close();
	}
	
	public static void exportTeam(OutputStream out, Team team) 
			throws IOException {
		exportTeam(out, team, false);
	}
	
	public static void exportTeam(JsonGenerator g, Team team, boolean entries) 
			throws IOException {
		g.writeStartObject();
		g.writeNumberField("number", team.getNumber());
		g.writeStringField("name", team.getName());
		g.writeStringField("nickname", team.getNickname());
		g.writeStringField("city", team.getCity());
		g.writeStringField("state", team.getState());
		g.writeStringField("country", team.getCountry());
		g.writeNumberField("rookieSeason", team.getRookieSeason());
		g.writeStringField("website", team.getWebsite());
		g.writeStringField("motto", team.getMotto());
		
		// this is expensive, so avoid doing it whenever possible
		if (entries) {
			// bad place for a db query, oh well
			Database db = Database.getInstance();

			Map<GameType, List<Game>> games = db.getParticipatedGames(team);
			g.writeArrayFieldStart("entries");
			for (GameType type : games.keySet()) {
				g.writeStartObject();

				g.writeStringField("gameName", type.getName());
				g.writeNumberField("gameYear", type.getYear());

				g.writeArrayFieldStart("entries");
				for (Game game : games.get(type)) {
					g.writeStartObject();
					g.writeStringField("eventName", game.getEvent().getShortName());
					g.writeStringField("eventFullName", game.getEvent().getName());
					g.writeEndObject();
				}
				g.writeEndArray();

				g.writeEndObject();
			}
			g.writeEndArray();
		}
		
		g.writeArrayFieldStart("statistics");
		for (TeamStatistics s : team.getStatistics()) {
			g.writeStartObject();
			
			g.writeNumberField("year", s.getYear());
			
			g.writeNumberField("oprMax", s.getOprMax());
			g.writeNumberField("oprMean", s.getOprMean());
			g.writeNumberField("oprMin", s.getOprMin());
			g.writeNumberField("oprSum", s.getOprSum());
			g.writeNumberField("oprVariance", s.getOprVariance());
			g.writeNumberField("oprStandardDeviation", s.getOprStandardDeviation());
			
			g.writeNumberField("oprZMax", s.getOprMax());
			g.writeNumberField("oprZMean", s.getOprMean());
			g.writeNumberField("oprZMin", s.getOprMin());
			g.writeNumberField("oprZSum", s.getOprSum());
			g.writeNumberField("oprZVariance", s.getOprVariance());
			g.writeNumberField("oprZStandardDeviation", s.getOprStandardDeviation());
			
			g.writeEndObject();
		}
		g.writeEndArray();
		
		g.writeEndObject();
	}
	
	public static void exportTeam(JsonGenerator g, Team team) 
			throws IOException {
		exportTeam(g, team, false);
	}
	
	public static void exportEvent(OutputStream out, Event event) 
			throws IOException {
		JsonFactory f = new JsonFactory();
		JsonGenerator g = f.createJsonGenerator(out, JsonEncoding.UTF8);
		g.setPrettyPrinter(new DefaultPrettyPrinter());
		
		exportEvent(g, event);
		
		g.close();
	}
	
	public static void exportEvent(JsonGenerator g, Event event)
			throws IOException {
		g.writeStartObject();
		g.writeStringField("name", event.getName());
		g.writeStringField("shortName", event.getShortName());
		g.writeStringField("venue", event.getVenue());
		g.writeStringField("city", event.getCity());
		g.writeStringField("state", event.getState());
		g.writeStringField("country", event.getCountry());
		g.writeStringField("identifier", event.getIdentifier());
		g.writeNumberField("latitude", event.getLatitude());
		g.writeNumberField("longitude", event.getLongitude());
		
		if (event.getAliases() != null) {
			g.writeArrayFieldStart("aliases");
			for (String s : event.getAliases()) {
				g.writeString(s);
			}
			g.writeEndArray(); // end aliases array
		}
		
		g.writeArrayFieldStart("years");
		for (Ref<Game> ref : event.getGameReferences()) {
			g.writeNumber(ref.getKey().getId());
		}
		g.writeEndArray();
		
		g.writeEndObject();
	}
	
	public static void exportGame(OutputStream out, Game game) 
			throws IOException {
		JsonFactory f = new JsonFactory();
		JsonGenerator g = f.createJsonGenerator(out, JsonEncoding.UTF8);
		g.setPrettyPrinter(new DefaultPrettyPrinter());
		
		exportGame(g, game);
		
		g.close();
	}
	
	public static void exportBasicGame(JsonGenerator g, Game game) 
			throws IOException {
		g.writeStartObject();
		g.writeNumberField("gameYear", game.getGameYear());
		g.writeStringField("gameName", game.getGameName());
		g.writeStringField("eventShortName", game.getEvent().getShortName());
		g.writeNumberField("startDate", game.getStartDate().getTime());
		g.writeNumberField("endDate", game.getEndDate().getTime());
		g.writeStringField("resultsURL", game.getResultsURL());
		g.writeStringField("standingsURL", game.getStandingsURL());
		g.writeStringField("awardsURL", game.getAwardsURL());
		
		if (game.getParent() != null) {
			g.writeObjectFieldStart("parent");
			g.writeStringField(
					"name",
					game.getParent().getEvent().getShortName());
			g.writeNumberField("year", game.getParent().getGameYear());
			g.writeEndObject();
		} else {
			g.writeNullField("parent");
		}
		
		g.writeArrayFieldStart("children");
		for (Game child : game.getChildren()) {
			g.writeStartObject();
			g.writeStringField("name", child.getEvent().getShortName());
			g.writeNumberField("year", child.getGameYear());
			g.writeEndObject();
		}
		g.writeEndArray();
		
		if (game instanceof GameOPRProvider) {
			GameOPRProvider goprp = (GameOPRProvider) game;
			g.writeNumberField("averageOPR", goprp.getAverageOPR());
			g.writeNumberField("averageDPR", goprp.getAverageDPR());
			g.writeNumberField("totalOPR", goprp.getTotalOPR());
			g.writeNumberField("totalDPR", goprp.getTotalDPR());
			g.writeNumberField("highestOPR", goprp.getHighestOPR());
			if (goprp.getHighestOPRTeam() != null) {
				g.writeNumberField(
						"highestOPRTeam",
						goprp.getHighestOPRTeam().getTeam().getNumber());
			}
			
			g.writeNumberField("lowestOPR", goprp.getLowestOPR());
			if (goprp.getLowestOPRTeam() != null) {
				g.writeNumberField(
						"lowestOPRTeam",
						goprp.getLowestOPRTeam().getTeam().getNumber());
			}
		}
		
		g.writeEndObject();
	}
	
	public static void exportGame(JsonGenerator g, Game game) 
			throws IOException {
		g.writeStartObject();
		g.writeNumberField("gameYear", game.getGameYear());
		g.writeStringField("gameName", game.getGameName());
		g.writeStringField("eventShortName", game.getEvent().getShortName());
		g.writeNumberField("startDate", game.getStartDate().getTime());
		g.writeNumberField("endDate", game.getEndDate().getTime());
		
		if (game.getParent() != null) {
			g.writeObjectFieldStart("parent");
			g.writeStringField(
					"name",
					game.getParent().getEvent().getShortName());
			g.writeNumberField("year", game.getParent().getGameYear());
			g.writeEndObject();
		} else {
			g.writeNullField("parent");
		}
		
		g.writeArrayFieldStart("children");
		for (Game child : game.getChildren()) {
			g.writeStartObject();
			g.writeStringField("name", child.getEvent().getShortName());
			g.writeNumberField("year", child.getGameYear());
			g.writeEndObject();
		}
		g.writeEndArray();
		
		if (game instanceof GameOPRProvider) {
			GameOPRProvider goprp = (GameOPRProvider) game;
			g.writeNumberField("averageOPR", goprp.getAverageOPR());
			g.writeNumberField("averageDPR", goprp.getAverageDPR());
			g.writeNumberField("totalOPR", goprp.getTotalOPR());
			g.writeNumberField("totalDPR", goprp.getTotalDPR());
			g.writeNumberField("highestOPR", goprp.getHighestOPR());
			if (goprp.getHighestOPRTeam() != null) {
				g.writeNumberField(
						"highestOPRTeam",
						goprp.getHighestOPRTeam().getTeam().getNumber());
			}
			g.writeNumberField("lowestOPR", goprp.getLowestOPR());
			if (goprp.getLowestOPRTeam() != null) {
				g.writeNumberField(
						"lowestOPRTeam",
						goprp.getLowestOPRTeam().getTeam().getNumber());
			}
		}
		
		g.writeArrayFieldStart("teams");
		for (TeamEntry e : game.getTeams()) {
			g.writeStartObject();
			g.writeNumberField("number", e.getTeam().getNumber());
			g.writeNumberField("rank", e.getRank());
			g.writeNumberField("matchesPlayed", e.getMatchesPlayed());
			g.writeNumberField("gameYear", e.getGame().get().getGameYear());
			g.writeNumberField("wins", e.getWins());
			g.writeNumberField("losses", e.getLosses());
			g.writeNumberField("ties", e.getTies());
			
			if (e.getFinalMatchLevel() != null) {
				g.writeStringField("finalMatchLevel", e.getFinalMatchLevel().getText().toLowerCase());
			}
		
			if (e instanceof Seeding2007Provider) {
				Seeding2007Provider seed = (Seeding2007Provider) e;
				g.writeNumberField("qualificationScore", seed.getQualificationScore());
				g.writeNumberField("rankingScore", seed.getRankingScore());
			}
			
			if (e instanceof MatchPointsProvider) {
				MatchPointsProvider mpp = (MatchPointsProvider) e;
				g.writeNumberField("matchPoints", mpp.getMatchPoints());
			}
			
			if (e instanceof Seeding2010Provider) {
				Seeding2010Provider seed = (Seeding2010Provider) e;
				
				g.writeNumberField("seedingScore", seed.getSeedingScore());
				g.writeNumberField("coopertitionBonus", seed.getCoopertitionBonus());
			}
			
			if (e instanceof HangingProvider) {
				HangingProvider hang = (HangingProvider) e;
				g.writeNumberField("hangingPoints", hang.getHangingPoints());
			}
			
			if (e instanceof OPRProvider) {
				OPRProvider opr = (OPRProvider) e;
				
				g.writeNumberField("opr", opr.getOPR());
				g.writeNumberField("dpr", opr.getDPR());
			}
			
			g.writeEndObject();
		}
		g.writeEndArray(); // end teams array
		
		g.writeArrayFieldStart("matches");
		for (Match m : game.getAllMatches()) {
			g.writeStartObject();
			g.writeStringField("type", m.getType().getText().toLowerCase());
			g.writeNumberField("number", m.getNumber());
			g.writeStringField("time", m.getTime());
			
			g.writeNumberField("redScore", m.getRedScore());
			g.writeArrayFieldStart("redTeams");
			for (Team t : m.getRedTeams()) {
				g.writeNumber(t.getNumber());
			}
			g.writeEndArray();
			
			g.writeNumberField("blueScore", m.getBlueScore());
			g.writeArrayFieldStart("blueTeams");
			for (Team t : m.getBlueTeams()) {
				g.writeNumber(t.getNumber());
			}
			g.writeEndArray();
			
			g.writeStringField("winner", m.getWinningAlliance().toString().toLowerCase());
			
			g.writeEndObject();
		}
		g.writeEndArray(); // end matches array
		
		g.writeArrayFieldStart("standings");
		for (Standing s : game.getStandings()) {
			g.writeStartObject();
			
			g.writeNumberField("rank", s.getRank());
			g.writeNumberField("team", s.getTeam().getTeam().getNumber());
			g.writeNumberField("matchesPlayed", s.getMatchesPlayed());
			
			if (s instanceof Seeding2007Provider) {
				Seeding2007Provider seed = (Seeding2007Provider) s;
				g.writeNumberField("qualificationScore", seed.getQualificationScore());
				g.writeNumberField("rankingScore", seed.getRankingScore());
			}
			
			if (s instanceof MatchPointsProvider) {
				MatchPointsProvider mpp = (MatchPointsProvider) s;
				g.writeNumberField("matchPoints", mpp.getMatchPoints());
			}
			
			if (s instanceof Seeding2010Provider) {
				Seeding2010Provider seed = (Seeding2010Provider) s;
				g.writeNumberField("seedingScore", seed.getSeedingScore());
				g.writeNumberField("coopertitionBonus", seed.getCoopertitionBonus());
			}
			
			if (s instanceof HangingProvider) {
				HangingProvider hang = (HangingProvider) s;
				g.writeNumberField("hangingPoints", hang.getHangingPoints());
			}
			
			g.writeEndObject();
		}
		g.writeEndArray(); // end standings array
		
		g.writeEndObject();
	}
	
	public static void exportMatches(JsonGenerator g, Game game) throws IOException {
		g.writeStartArray();
		
		for (Match m : game.getAllMatches()) {
			g.writeStartObject();
			g.writeStringField("type", m.getType().getText().toLowerCase());
			g.writeNumberField("number", m.getNumber());
			g.writeStringField("time", m.getTime());
			
			g.writeNumberField("redScore", m.getRedScore());
			g.writeArrayFieldStart("redTeams");
			for (Team t : m.getRedTeams()) {
				g.writeNumber(t.getNumber());
			}
			g.writeEndArray();
			
			g.writeNumberField("blueScore", m.getBlueScore());
			g.writeArrayFieldStart("blueTeams");
			for (Team t : m.getBlueTeams()) {
				g.writeNumber(t.getNumber());
			}
			g.writeEndArray();
			
			g.writeStringField("winner", m.getWinningAlliance().toString().toLowerCase());
			
			g.writeEndObject();
		}
		
		g.writeEndArray();
	}
	
	public static void exportMatches(OutputStream out, Game game)
			throws IOException {
		JsonFactory f = new JsonFactory();
		JsonGenerator g = f.createJsonGenerator(out, JsonEncoding.UTF8);
		g.setPrettyPrinter(new DefaultPrettyPrinter());
		
		exportMatches(g, game);
		
		g.close();
	}
	
	public static void exportTeamResults(JsonGenerator g, Collection<Team> teams)
			throws IOException {
		g.writeStartArray();
		
		for (Team t : teams) {
			g.writeStartObject();
			
			g.writeNumberField("number", t.getNumber());
			g.writeStringField("nickname", t.getNickname());
			
			g.writeEndObject();
		}
		
		g.writeEndArray();
	}
	
	public static void exportTeamResults(OutputStream out, Collection<Team> teams)
			throws IOException {
		JsonFactory f = new JsonFactory();
		JsonGenerator g = f.createJsonGenerator(out, JsonEncoding.UTF8);
		g.setPrettyPrinter(new DefaultPrettyPrinter());
		
		exportTeamResults(g, teams);
		
		g.close();
	}
	
	public static void exportStats(OutputStream out, Database db) 
			throws IOException {
		JsonFactory f = new JsonFactory();
		JsonGenerator g = f.createJsonGenerator(out, JsonEncoding.UTF8);
		
		exportStats(g, db);
		
		g.close();
	}
	
	public static void exportStats(JsonGenerator g, Database db)
			throws IOException {
		g.writeStartObject();
		g.writeNumberField("teams", db.getTeams().size());
		g.writeNumberField("events", db.getEvents().size());
		// TODO: more here?
		
		g.writeEndObject();
	}
	
	public static void jsonError(OutputStream out, String message) 
			throws IOException {
		JsonFactory f = new JsonFactory();
		JsonGenerator g = f.createJsonGenerator(out, JsonEncoding.UTF8);
		
		g.writeStartObject();
		g.writeStringField("error", message);
		g.writeEndObject();
		
		g.close();
	}
	
	public static String streamToString(StreamGenerator s) {
		StringWriter writer = new StringWriter();
		JsonFactory f = new JsonFactory();
		try {
			JsonGenerator g = f.createJsonGenerator(writer);
			g.useDefaultPrettyPrinter();
			
			s.generate(g);
			g.close();
			
			return writer.toString();
		} catch (IOException ex) {
			return null;
		}
	}

	public static interface StreamGenerator {

		public void generate(JsonGenerator g) throws IOException;
		
	}
	
}
