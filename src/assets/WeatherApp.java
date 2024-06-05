package assets;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

// get weather data from API

public class WeatherApp {
	
	public static JSONObject getWeatherData(String locationName) {
		// get location coordinates using the API
		
		JSONArray locationData = getLocationData(locationName);
		
		// get latittude and longitude data
		JSONObject location = (JSONObject) locationData.get(0);
		double latitude = (double) location.get("latitude");
		double longitude = (double) location.get("longitude");
		
		
		// create api request url with coordinates
		String urlString = "https://api.open-meteo.com/v1/forecast?"
				+ "latitude=" + latitude + "&longitude=" + longitude
				+ "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&timezone=America%2FLos_Angeles";
		
		
		try {
			// call api for response
			HttpURLConnection conn = fetchApiResponse(urlString);
			
			// check response - 200 means success
			if (conn.getResponseCode() != 200) {
				System.out.println("Error! Could not connect to API!");
				return null;
			}
			
			// store results from API
			StringBuilder resultJson = new StringBuilder();
			Scanner scanner = new Scanner(conn.getInputStream());
			
			while (scanner.hasNext()) {
				resultJson.append(scanner.nextLine());
			} // while
			
			scanner.close(); // close scanner
			conn.disconnect(); // close url connection
			
			// parse through data
			JSONParser parser = new JSONParser();
			JSONObject resultJsonObj  = (JSONObject) parser.parse(String.valueOf(resultJson));
			
			// get hourly data
			JSONObject hourly = (JSONObject) resultJsonObj.get("hourly");
			
			// get data of current hour
			JSONArray time = (JSONArray) hourly.get("time");
			int index = findIndexOfCurrentTime(time);
			
			// get temp from API
			JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
			double temperature = (double) temperatureData.get(index);
			
			// get weather code
			JSONArray weathercode = (JSONArray) hourly.get("weathercode");
			String weatherCondition = convertWeatherCode((long) weathercode.get(index));
			
			// get humidity level
			JSONArray relativeHumidity = (JSONArray) hourly.get("relativehumidity_2m");
			long humidity = (long) relativeHumidity.get(index);
			
			// get windspeed level
			JSONArray windspeedData = (JSONArray) hourly.get("windspeed_10m");
			double windspeed = (double) windspeedData.get(index);
			
			// create front end of weather app
			JSONObject weatherData = new JSONObject();
			weatherData.put("temperature", temperature);
			weatherData.put("weather_condition", weatherCondition);
			weatherData.put("humidity", humidity);
			weatherData.put("windspeed", windspeed);
			
			return weatherData;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	} // end method
	
	public static JSONArray getLocationData(String locationName) { // gets geographic coordinates based off location name
		// over here fill empty spaces with a + to easily retrive data through API request 
		locationName = locationName.replaceAll(" ", "+");
		
		// build API url with specific city/location
		String urlString = "https://geocoding-api.open-meteo.com/v1/search?name="
				+locationName+ "&count=10&language=en&format=json";
		
		try {
			HttpURLConnection conn = fetchApiResponse(urlString);
			
			// check response code - 200 is good !
			if (conn.getResponseCode() != 200) { // if error
				System.out.println("Error! Could not connect to API!");
				return null;
			} // if
			
			else { // store api results
				StringBuilder resultJson = new StringBuilder();
				Scanner scanner = new Scanner(conn.getInputStream());
				
				// read and store data into a new string
				while (scanner.hasNext()) {
					resultJson.append(scanner.nextLine());
				} // while
				
				scanner.close(); // end scanner
				conn.disconnect(); // disconnect url
				
				// parse json string to a json object
				JSONParser parser = new JSONParser();
				JSONObject resultsJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));
				
				// get list of location data from API 
				JSONArray locationData = (JSONArray) resultsJsonObj.get("results");
				return locationData;
				
			} // end else
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// couldnt find a location
		return null;
		
	} // end method
	
	private static HttpURLConnection fetchApiResponse(String urlString) {
		try { // attempt to connect
			URL url = new URL(urlString);
			HttpURLConnection conn  = (HttpURLConnection) url.openConnection();
			
			// request method is GET
			conn.setRequestMethod("GET");
			
			// connect to API
			conn.connect();
			return conn;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null; // throw could no connection
	} // end method
	
	private static int findIndexOfCurrentTime(JSONArray timeList) {
		String currentTime = getCurrentTime();
		
		for (int i = 0; i < timeList.size(); i++) {
			String time = (String) timeList.get(i);
			
			if (time.equalsIgnoreCase(currentTime)) {
				return i;
			}
			
		} // for 
		
		return 0;
	}
	
	public static String getCurrentTime() {
		// get current date and time
		LocalDateTime currentDateTime = LocalDateTime.now();
		
		//format properly for api request
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");
		
		// print date and time
		String formattedDateTime = currentDateTime.format(formatter);
		
		return formattedDateTime;
	} // end method
	
	private static String convertWeatherCode(long weathercode) {
		String weatherCondition = "";
		
		if (weathercode == 0L) {
			weatherCondition = "Clear";
		} 
		else if (weathercode > 0L && weathercode <= 3L) {
			weatherCondition = "Cloudy";
		}
		else if ( (weathercode >= 51L && weathercode <= 67L) || (weathercode >= 80L && weathercode <= 99L) ) {
			weatherCondition = "Rain";
		}
		else if (weathercode >= 71L && weathercode <= 77L) {
			weatherCondition = "Snow";
		}
		
		return weatherCondition;
		
		
	} // end method
	
	
	
} // end class
