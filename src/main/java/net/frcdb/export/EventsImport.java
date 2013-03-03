package net.frcdb.export;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import net.frcdb.api.event.Event;
import net.frcdb.db.Database;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tim
 */
public class EventsImport {
	
	public static final String EVENTS_FILE = "export/events.json";
	
	private Logger logger = LoggerFactory.getLogger(EventsImport.class);
	private Database db;
	
	public EventsImport(InputStream input) throws IOException {
		db = new Database();
		
		logger.info("Parsing event data...");
		
		long time = System.currentTimeMillis();
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(input);
		
		double secs = (System.currentTimeMillis() - time) / 1000d;
		logger.info("Done in " + secs + " seconds; now importing event data...");
		
		Iterator<JsonNode> iter = rootNode.getElements();
		while (iter.hasNext()) {
			JsonNode eventNode = iter.next();
			parse(eventNode);
		}
	}
	
	private void parse(JsonNode eventNode) {
		String shortName = eventNode.get("shortName").asText();
		
		// don't overwrite if we can avoid it
		Event event = db.getEventByShortName(shortName);
		if (event == null) {
			event = new Event(shortName);
			logger.info("Importing new event: " + event);
		} else {
			logger.info("Merging event: " + event);
		}
		
		String name       = eventNode.get("name").asText();
		String venue      = eventNode.get("venue").asText();
		String city       = eventNode.get("city").asText();
		String state      = eventNode.get("state").asText();
		String country    = eventNode.get("country").asText();
		String identifier = eventNode.get("identifier").asText();
		
		event.setName(name);
		event.setShortName(shortName);
		event.setVenue(venue);
		event.setCity(city);
		event.setState(state);
		event.setCountry(country);
		event.setIdentifier(identifier);
		
		db.store(event);
	}
	
	public static void main(String[] args) throws IOException {
		File file = new File(EVENTS_FILE);
		if (!file.exists()) {
			System.err.println("[Error] Not found: export/events.json");
			return;
		}
		
		EventsImport em = new EventsImport(file.toURI().toURL().openStream());
	}
	
}
