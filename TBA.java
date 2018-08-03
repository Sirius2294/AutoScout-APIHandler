import java.io.*;

import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.util.EntityUtils;

import org.json.*;

// This class processes requests to and from The Blue Alliance API

public class TBA {
	
	// The base URL from which all calls are added to the end
	private final static String baseURL = "http://www.thebluealliance.com/api/v3";
	// The team key for team 2264, Wayzata Robotics
	private final static String homeTeamKey = "frc2264";
	// The event key for the 2018 Duluth, Minnesota regional
	private final static String eventKey = "2018mndu";
	// The year of the current competition
	private final static int currentYear = 2018;
	
	// The header that communicates the user to the API
	private final static String userAgent = "test-scout";
	// The authentication key that allows the application to interact with the API
	private final static String authKey = "YXwa7Fm6N0mks7XhnRMIVnJzsjE3frXe30GZAMp5r3rDmhdLFcZjsFkFTpxxRUtR";
	
	
	// Returns the JSONObject returned by the call to the API
	public static JSONObject getJSON(String call) {
		JSONObject json = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		
		// Readies a request to be sent to the API
		HttpGet httpGet = new HttpGet(baseURL + call);
		httpGet.addHeader("User-Agent", userAgent);
		httpGet.addHeader("X-TBA-Auth-Key", authKey);
		
		try {
			// Executes the request
			response = httpClient.execute(httpGet);
			
			// Converts the response to a JSONObject
			String jsonString = EntityUtils.toString(response.getEntity());
			json = new JSONObject(jsonString);
			
		} catch(ClientProtocolException e) {
			System.err.println("ClientProtocolException when executing http request to " + baseURL + call);
			e.printStackTrace();
		} catch(IOException e) {
			System.err.println("IOException when executing http request to " + baseURL + call);
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
				response.close();
			} catch(IOException e) {
				System.out.println("IOException when closing " + httpClient.getClass() +" and "+ response.getClass());
				e.printStackTrace();
			}
		}
		
		return json;
	}
	
	public static String getString(String call) {
		String jsonString = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		
		// Readies a request to be sent to the API
		HttpGet httpGet = new HttpGet(baseURL + call);
		httpGet.addHeader("User-Agent", userAgent);
		httpGet.addHeader("X-TBA-Auth-Key", authKey);
		
		try {
			// Executes the request
			response = httpClient.execute(httpGet);
			
			jsonString = EntityUtils.toString(response.getEntity());
		} catch(ClientProtocolException e) {
			System.err.println("ClientProtocolException when executing http request to " + baseURL + call);
			e.printStackTrace();
		} catch(IOException e) {
			System.err.println("IOException when executing http request to " + baseURL + call);
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
				response.close();
			} catch(IOException e) {
				System.out.println("IOException when closing " + httpClient.getClass() +" and "+ response.getClass());
				e.printStackTrace();
			}
		}
		
		return jsonString;
	}
	
	public static String getMatch(int matchNum) {
		return String.format("/match/%1$s_qm%2$d", eventKey, matchNum);
	}
	
	public static String getTeamStatus(int teamNumber) {
		return String.format("/team/frc%1$d/events/%2$d/statuses", teamNumber, currentYear);
	}
	
	public static String getTeamMatches(int teamNumber) {
		return String.format("/team/frc%1$d/event/%2$s/matches", teamNumber, eventKey);
	}
}
