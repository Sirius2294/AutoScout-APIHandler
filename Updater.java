import org.json.*;
import java.util.*;

public class Updater {
	
	// Arrays containing all the keys of interest
	// This array contains all the keys where the values are the same for each team on the same alliance
	private final static String[] genericKeys = {"teleopScaleOwnershipSec", "autoScaleOwnershipSec", "vaultPoints", "autoSwitchAtZero", "teleopOwnershipPoints", "teleopPoints", "autoOwnershipPoints", "autoPoints", "teleopSwitchOwnershipSec"};
	// This array contains all the keys where the values differ between teams on the same alliance
	private final static String[] uniqueKeys = {};
	
	// Returns a HashMap<String, Object> array that contains the match data for each team
	@SuppressWarnings("unchecked")
	public static HashMap<String, Object>[] getMatchData(JSONObject matchJSON) {
		HashMap<String, Object>[] infoTable = (HashMap<String, Object>[]) new HashMap[6];
		for(int x = 0; x < infoTable.length; x++) {
			infoTable[x] = new HashMap<String, Object>();
		}
		
		//JSONObject json = TBA.getJSON(TBA.getMatch(matchNum));
		
		// Gets the team number for each team
		JSONObject allianceJSON = matchJSON.getJSONObject("alliances");
		JSONObject blueJSON = allianceJSON.getJSONObject("blue");
		JSONObject redJSON = allianceJSON.getJSONObject("red");
		for(int x = 0; x < 3; x++) {
			infoTable[x].put("teamNumber", blueJSON.getJSONArray("team_keys").get(x).toString().substring(3));
			infoTable[x+3].put("teamNumber", redJSON.getJSONArray("team_keys").get(x).toString().substring(3));
		}
		
		JSONObject breakdownJSON = matchJSON.getJSONObject("score_breakdown");
		blueJSON = breakdownJSON.getJSONObject("blue");
		redJSON = breakdownJSON.getJSONObject("red");
		
		// Extracts all the values associated with all the uniqueKeys
		for(int k = 0; k < uniqueKeys.length; k++) {
			for(int x = 0; x < 3; x++) {
				infoTable[x].put(uniqueKeys[k], blueJSON.get(uniqueKeys[k] + (x+1)));
				infoTable[x+3].put(uniqueKeys[k], redJSON.get(uniqueKeys[k] + (x+1)));
			}
		}
		
		// Extracts all the values associated with all the genericKeys
		for(int k = 0; k < genericKeys.length; k++) {
			for(int x = 0; x < 3; x++) {
				infoTable[x].put(genericKeys[k], blueJSON.get(genericKeys[k]));
				infoTable[x+3].put(genericKeys[k], redJSON.get(genericKeys[k]));
			}
		}
		
		// This section handles specific keys and information that is structurally different from the rest
		for(int x = 0; x < 3; x++) {
			infoTable[x].put("endgameClimb", blueJSON.get("endgameRobot" + (x+1)).equals("Climbing"));
			infoTable[x+3].put("endgameClimb", redJSON.get("endgameRobot" + (x+1)).equals("Climbing"));
			
			infoTable[x].put("autoRun", blueJSON.get("autoRobot"+(x+1)).equals("AutoRun"));
			infoTable[x+3].put("autoRun", redJSON.get("autoRobot"+(x+1)).equals("AutoRun"));
		}
		
		return infoTable;
	}

}
