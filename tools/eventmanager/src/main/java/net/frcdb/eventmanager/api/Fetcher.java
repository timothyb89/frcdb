package net.frcdb.eventmanager.api;

import net.frcdb.eventmanager.api.Event;
import net.frcdb.eventmanager.api.Game;
import net.frcdb.eventmanager.util.DateUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author tim
 */
@Slf4j
public class Fetcher {
	
	public static final String EVENTS_URL = "http://api.frcdb.net/events.json";
	
	public static final String FIRST_EVENTS_URL =
			"https://my.usfirst.org/frc/scoring/index.lasso?page=eventlist";
	
	public static List<Event> fetchEvents(URL url) throws IOException {
		log.info("Loading FRC-DB events from " + url.toString());
		ObjectMapper mapper = new ObjectMapper();
		
		JsonNode root = mapper.readTree(url);
		
		List<Event> ret = new ArrayList<Event>();
		for (JsonNode event : root) {
			ret.add(new Event(event));
		}
		
		return ret;
	}
	
	public static List<Event> fetchEvents() throws IOException {
		return fetchEvents(new URL(EVENTS_URL));
	}
	
	public static List<Event> fetchFirstEvents(URL url) throws IOException {
		log.debug("Downloading FIRST event list...");
		
		Document doc = Jsoup.parse(url, 5000);
		
		log.debug("Parsing events...");
		
		String text = doc.select("pre").text();
		
		List<Event> ret = new ArrayList<Event>();
		for (String input : StringUtils.split(text, "\n")) {
			if (!Character.isDigit(input.charAt(0))) {
				continue;
			}
			
			String[] fields = input.split("\t");
			
			// trim everything
			for (int i = 0; i < fields.length; i++) {
				fields[i] = fields[i].trim();
			}

			Event e = new Event();
			e.setName(fields[1]);
			e.setVenue(fields[2]);
			e.setCity(fields[3]);
			e.setState(fields[4]);
			e.setCountry(fields[5]);
			e.setIdentifier(fields[11]);
			
			Game g = new Game();
			g.setEid(Integer.parseInt(fields[0]));
			g.setStartDate(DateUtil.parse(fields[6]));
			g.setEndDate(DateUtil.fixEndDate(DateUtil.parse(fields[7])));
			e.setGame(g);
			
			ret.add(e);
		}
		
		return ret;
	}
	
	public static List<Event> fetchFirstEvents() throws IOException {
		return fetchFirstEvents(new URL(FIRST_EVENTS_URL));
	}
	
	public static void main(String[] args) throws IOException {
		for (Event e : fetchEvents(new File("/home/tim/Documents/events.json")
				.toURI().toURL())) {
			log.info(e.toString());
		}
		//for (Event e : fetchFirstEvents()) {
		//	log.info(e.toString());
		//}
	}
	
}
