package net.frcdb.util;

import net.frcdb.api.event.Event;
import net.frcdb.db.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tim
 */
public class UpdateLatLong {
	
	private static Logger logger = LoggerFactory.getLogger(UpdateLatLong.class);
	
	public static void main(String[] args) {
		Database db = Database.getInstance();
		
		for (Event e : db.getEvents()) {
			String location = e.getCity() + ", " + e.getCountry();
			
			logger.info("Attempting to geocode " + e);
			
			try {
				Geocoder geo = new Geocoder(location);
				geo.fetch();
				
				e.setLatitude(geo.getLatitude());
				e.setLongitude(geo.getLongitude());
				
				db.store(e);
				
				logger.info("Event " + e.getShortName() + " is now at "
						+ geo.getLatitude() + "," + geo.getLongitude());
			} catch (Exception ex) {
				logger.error("Failed to geocode location", ex);
			}
		}
	}
	
}
