package net.frcdb.export;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Converts a massive .json file of all games into a one-file-per-game
 * @author tim
 */
public class GamesRedump {
	
	public static final String GAMES_FILE = "export/games.json";
	public static final String GAME_DIRECTORY = "export/games";
	
	private Logger logger = LoggerFactory.getLogger(GamesImport.class);
	
	private ObjectMapper mapper;
	private JsonFactory factory;
	private File dir;
	
	public GamesRedump(InputStream input) throws IOException {
		mapper = new ObjectMapper();
		factory = new JsonFactory();
		
		JsonNode rootNode = mapper.readTree(input);
		
		dir = new File(GAME_DIRECTORY);
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		Iterator<JsonNode> iter = rootNode.elements();
		while (iter.hasNext()) {
			dump(iter.next());
		}
		
		input.close();
	}
	
	private void dump(JsonNode node) {
		String eventShortName = node.get("eventShortName").asText();
		int gameYear = node.get("gameYear").asInt();
		
		File out = new File(dir, eventShortName + "-" + gameYear + ".json");
		try {
			JsonGenerator gen = factory.createJsonGenerator(out, JsonEncoding.UTF8);
			gen.useDefaultPrettyPrinter();
			
			mapper.writeTree(gen, node);
			gen.close();
		} catch (Exception ex) {
			logger.error(
					"Failed to redump event " + eventShortName + " " + gameYear,
					ex);
		}
	}
	
	public static void main(String[] args) throws IOException {
		File file = new File(GAMES_FILE);
		if (!file.exists()) {
			System.err.println("[Error] Not found: export/games.json");
			return;
		}
		
		GamesRedump em = new GamesRedump(file.toURI().toURL().openStream());
	}
	
}
