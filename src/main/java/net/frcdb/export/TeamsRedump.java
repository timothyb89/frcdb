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
 * Segments files a little more
 * @author tim
 */
public class TeamsRedump {
	
	public static final int TEAMS_PER_FILE = 500;
	
	public static final String TEAMS_FILE = "export/teams.json";
	public static final String TEAMS_DIRECTORY = "export/teams";
	
	private Logger logger = LoggerFactory.getLogger(GamesImport.class);
	
	private ObjectMapper mapper;
	private JsonFactory factory;
	private File dir;
	
	public TeamsRedump(InputStream input) throws IOException {
		mapper = new ObjectMapper();
		factory = new JsonFactory();
		
		JsonNode rootNode = mapper.readTree(input);
		
		dir = new File(TEAMS_DIRECTORY);
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		Iterator<JsonNode> iter = rootNode.elements();
		int count = 0;
		int countAbs = 0;
		
		File out = new File(dir, count + ".json");
		JsonGenerator gen = factory.createJsonGenerator(out, JsonEncoding.UTF8);
		
		gen.writeStartArray();
		
		while (iter.hasNext()) {
			mapper.writeTree(gen, iter.next());
			
			count++;
			countAbs++;
			if (count > TEAMS_PER_FILE) {
				count = 0;
				gen.writeEndArray();
				gen.close();
				
				out = new File(dir, countAbs + ".json");
				gen = factory.createJsonGenerator(out, JsonEncoding.UTF8);
				gen.writeStartArray();
			}
		}
		
		gen.close();
		
		input.close();
	}
	
	public static void main(String[] args) throws IOException {
		File file = new File(TEAMS_FILE);
		if (!file.exists()) {
			System.err.println("[Error] Not found: export/teams.json");
			return;
		}
		
		TeamsRedump em = new TeamsRedump(file.toURI().toURL().openStream());
	}
	
}
