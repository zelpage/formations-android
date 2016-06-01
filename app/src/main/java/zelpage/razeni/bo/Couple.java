package zelpage.razeni.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Couple Business object
 * @author jindrichbr
 */
public class Couple implements Serializable {
    private String fromStation;
    private String toStation;
    private List<Vehicle> vehicles;

    public String getFromStation() {
        return fromStation;
    }

    public void setFromStation(String fromStation) {
        this.fromStation = fromStation;
    }

    public String getToStation() {
        return toStation;
    }

    public void setToStation(String toStation) {
        this.toStation = toStation;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
    
    /**
     * Creates new object from json representation
     * @param json json object
     * @return java object
     */
    public static Couple fromJson(JSONObject json) throws JSONException{
        Couple c = new Couple();
        c.setFromStation(json.optString("FromStation"));
        c.setToStation(json.optString("ToStation"));
        
        List<Vehicle> v = new ArrayList<Vehicle>();
        JSONArray vehiclesArray = json.getJSONArray("VehiclesInCouple");
        for(int index=0;index<vehiclesArray.length();index++){
            v.add(Vehicle.fromJson(vehiclesArray.getJSONObject(index)));
        }
        c.setVehicles(v);
        return c;
    }
}
