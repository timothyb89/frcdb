package net.frcdb.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 * Gets latitude and longitude for a particular location using google's
 * geocoding API.
 * @author tim
 */
public class Geocoder {
	
	public static final String MAPS_URL = "http://maps.google.com/maps/geo";
	
	private String location;
	
	private double latitude;
	private double longitude;
	
	public Geocoder(String location) {
		this.location = location;
	}
	
	public void fetch() throws URISyntaxException, IOException, GeocodingException {
		DefaultHttpClient client = new DefaultHttpClient();
		
		String str = "http://maps.google.com/maps/api/geocode/json?"
				+ "address=" + URLEncoder.encode(location, "UTF-8")
				+ "&sensor=false";
		
		URL url = new URL(str);
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(url.openStream());
		
		JsonNode results = root.get("results");
		if (results.has(0)) {
			JsonNode result = results.get(0);
			JsonNode loc = result.path("geometry").path("location");
			
			if (loc.isMissingNode()) {
				throw new GeocodingException("Invalid geocoding result");
			}
			
			latitude = loc.get("lat").asDouble();
			longitude = loc.get("lng").asDouble();
		} else {
			throw new GeocodingException("No results for query: " + location);
		}
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
	
	public static void main(String[] args) throws Exception {
		Geocoder geo = new Geocoder("denver, colorado");
		geo.fetch();
		
		System.out.println("Latitude:  " + geo.getLatitude());
		System.out.println("Longitude: " + geo.getLongitude());
	}

	
	
}
