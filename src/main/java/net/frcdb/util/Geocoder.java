package net.frcdb.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
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
	
	public void fetch() throws URISyntaxException, IOException {
		DefaultHttpClient client = new DefaultHttpClient();
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("output", "csv"));
		params.add(new BasicNameValuePair("q", location));
		
		URI uri = URIUtils.createURI(
				"http", "maps.google.com", 80, "/maps/geo",
				URLEncodedUtils.format(params, "UTF-8"), null);
		
		HttpGet get = new HttpGet(uri);
		HttpResponse response = client.execute(get);
		
		InputStream is = response.getEntity().getContent();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		
		String text = br.readLine();
		String[] split = StringUtils.split(text, ",");
		
		String latString = split[2];
		latitude = Double.parseDouble(latString);
		
		String longString = split[3];
		longitude = Double.parseDouble(longString);
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
