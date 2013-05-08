package net.frcdb.eventmanager.util;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.frcdb.eventmanager.Editor;
import net.frcdb.eventmanager.api.Event;
import net.frcdb.eventmanager.api.EventEntry;
import net.frcdb.eventmanager.api.Game;

/**
 *
 * @author tim
 */
@Slf4j
public class Exporter {
	
	private JsonFactory factory;
	
	public Exporter() {
		factory = new JsonFactory();
	}
	
	public void export(final List<EventEntry> events, File dir)
			throws IOException {
		
		File gameDir = new File(dir, "games");
		gameDir.mkdir();
		
		final List<Event> queue = new ArrayList<Event>();
		
		for (EventEntry e : events) {
			if (e.getShortName() == null) {
				log.error(e + " has no shortName, skipping...");
				continue;
			}

			// write the new game if needed
			if (e.isNewGame() && e.getGame() != null) {
				String name = e.getShortName() + "-" + Editor.getSelectedYear()
						+ ".json";
				writeJson(new File(gameDir, name),
						new GameWriter(e.getCandidate(), e.getGame()));
			}

			// skip events that have no modifications
			if (!e.isModified()) {
				log.info("Skipping " + e + " with no modifications");
				continue;
			}

			queue.add(e.getCandidate());
		}
		
		writeJson(new File(dir, "events.json"), new JsonWriter() {

			public void write(JsonGenerator g) throws IOException {
				g.writeStartArray();
				
				for (Event e : queue) {
					g.writeStartObject();
					
					g.writeStringField("shortName", e.getShortName());
					g.writeStringField("name", e.getName());
					g.writeStringField("venue", e.getVenue());
					g.writeStringField("city", e.getCity());
					g.writeStringField("state", e.getState());
					g.writeStringField("country", e.getCountry());
					g.writeStringField("identifier", e.getIdentifier());
					
					g.writeEndObject();
				}
				
				g.writeEndArray();
			}
			
		});
	}
	
	private void writeJson(File f, JsonWriter writer) throws IOException {
		JsonGenerator g = factory.createGenerator(f, JsonEncoding.UTF8);
		g.useDefaultPrettyPrinter();		
		writer.write(g);
		g.close();
	}
	
	private interface JsonWriter {
		
		public void write(JsonGenerator g) throws IOException;
		
	}
	
	private class GameWriter implements JsonWriter {

		private Event event;
		private Game game;

		public GameWriter(Event event, Game game) {
			this.event = event;
			this.game = game;
		}
		
		public void write(JsonGenerator g) throws IOException {
			g.writeStartObject();
			
			g.writeNumberField("gameYear", Editor.getSelectedYear());
			g.writeStringField("eventShortName", event.getShortName());
			
			g.writeNumberField("startDate", game.getStartDate().getTime());
			g.writeNumberField("endDate", game.getEndDate().getTime());
			
			g.writeArrayFieldStart("teams");
			g.writeEndArray();
			
			g.writeArrayFieldStart("matches");
			g.writeEndArray();
			
			g.writeArrayFieldStart("standings");
			g.writeEndArray();
			
			g.writeEndObject();
		}
		
	}
	
}
