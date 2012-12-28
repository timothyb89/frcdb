package net.frcdb.export;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import net.frcdb.api.event.Event;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.team.Team;
import net.frcdb.db.Database;
import net.frcdb.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tim
 */
public class Export {
	
	private static Logger logger = LoggerFactory.getLogger(Export.class);
	
	public static void main(String[] args) throws IOException {
		Database db = new Database();
		
		logger.info("Connected to database");
		
		File dir = new File("export");
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		JsonFactory f = new JsonFactory();
		
		File teams = new File("export/teams.json");
		FileOutputStream out = new FileOutputStream(teams);
		JsonGenerator g = f.createJsonGenerator(out, JsonEncoding.UTF8);
		
		g.writeStartArray();
		for (Team t : db.getTeams()) {
			logger.debug("Exporting team: " + t);

			JSONUtil.exportTeam(g, t);
		}
		g.writeEndArray();
		g.close();
		
		logger.info("Teams exported to export/teams.json.");
		
		File events = new File("export/events.json");
		out = new FileOutputStream(events);
		g = f.createJsonGenerator(out, JsonEncoding.UTF8);
		
		g.writeStartArray();
		for (Event e : db.getEvents()) {
			logger.debug("Exporting event: " + e);
			
			JSONUtil.exportEvent(g, e);
		}
		g.writeEndArray();
		g.close();
		logger.info("Events exported to export/events.json.");
		
		File games = new File("export/games.json");
		out = new FileOutputStream(games);
		g = f.createJsonGenerator(out, JsonEncoding.UTF8);
		
		g.writeStartArray();
		for (Game game : db.getGames()) {
			try {
				logger.debug("Exporting game: " + game);

				JSONUtil.exportGame(g, game);
			} catch (Exception ex) {
				logger.error("Failed to export game: " + game, ex);
			}
		}
		g.writeEndArray();
		g.close();
		logger.info("Games exported to export/games.json");
		
		logger.info("Finished");
	}
	
}
