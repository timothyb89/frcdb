package net.frcdb.export;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import net.frcdb.api.team.Team;
import net.frcdb.db.Database;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A crappy parser for team json. Virtually no error handling, so beware.
 * @author tim
 */
public class TeamsImport {
	
	public static final String TEAMS_FILE = "export/teams.json";
	
	private Logger logger = LoggerFactory.getLogger(TeamsImport.class);
	private Database db;
	
	public TeamsImport(InputStream input) throws IOException {
		db = new Database();
		
		logger.info("Parsing team data...");
		
		long time = System.currentTimeMillis();
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(input);
		
		double secs = (System.currentTimeMillis() - time) / 1000d;
		logger.info("Done in " + secs + " seconds; now importing team data...");
		
		Iterator<JsonNode> iter = rootNode.getElements();
		while (iter.hasNext()) {
			JsonNode teamNode = iter.next();
			
			// TODO: remove this, for faster debugging
			//if (teamNode.get("number").getIntValue() != 1977) { continue; }
			
			parse(teamNode);
		}
	}
	
	private void parse(JsonNode node) {
		int number = node.get("number").getIntValue();
		
		if (db.getTeam(number) != null) {
			return; // skip existing
		}
		
		Team team = new Team();
		team.setNumber(number);
		
		team.setName(node.get("name").getTextValue());
		team.setNickname(node.get("nickname").getTextValue());
		team.setCity(node.get("city").getTextValue());
		team.setState(node.get("state").getTextValue());
		team.setCountry(node.get("country").getTextValue());
		team.setRookieSeason(node.get("rookieSeason").getIntValue());
		team.setWebsite(node.get("website").getTextValue());
		team.setMotto(node.get("motto").getTextValue());
		
		// skip RobotInfo
		// TODO: possibly write a converter or something
		
		db.store(team);
		
		logger.info("Imported team: " + team.toString());
	}
	
	public static void main(String[] args) throws IOException {
		File file = new File(TEAMS_FILE);
		if (!file.exists()) {
			System.err.println("[Error] Not found: export/teams.json");
			return;
		}
		
		TeamsImport im = new TeamsImport(file.toURI().toURL().openStream());
	}
	
}
