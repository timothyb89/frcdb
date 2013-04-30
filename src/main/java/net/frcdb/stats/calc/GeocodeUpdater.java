package net.frcdb.stats.calc;

import net.frcdb.api.event.Event;
import net.frcdb.db.Database;
import net.frcdb.util.Geocoder;
import net.frcdb.util.GeocodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tim
 */
public class GeocodeUpdater implements EventStatistic {

	private Logger logger = LoggerFactory.getLogger(GeocodeUpdater.class);
	
	@Override
	public String[] getNames() {
		return new String[] {
			"GeocodeUpdater",
			"update-geocodes",
			"geocodes",
			"geocoder"
		};
	}
	
	@Override
	public void calculate(Event event) {
		String location = event.getVenue() + " "
				+ event.getCity() + " "
				+ event.getState();
		
		Geocoder coder = new Geocoder(location);
		try {
			coder.fetch();
			
			event.setLatitude(coder.getLatitude());
			event.setLongitude(coder.getLongitude());
			
			Database.save().entity(event).now();
			
			logger.info(String.format(
					"%s geocoded to %.4f, %.4f",
					event, coder.getLatitude(), coder.getLongitude()));
		} catch (GeocodingException ex) {
			logger.error("Failed to geocode event " + event + ": "
					+ ex.getMessage(), ex);
		} catch (Exception ex) {
			logger.error("Failed to geocode event " + event, ex);
		}
	}

	@Override
	public String getBackendName() {
		return null;
	}
	
}
