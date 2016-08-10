package zelpage.razeni

import java.io.Serializable
import java.util.ArrayList
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import zelpage.razeni.Vehicle

/**
 * Couple Business object
 * @author jindrichbr
 */
data class Couple (
	var fromStation: String? = null,
	var toStation: String? = null,
	var vehicles: List<Vehicle>? = null) : Serializable {

	companion object {

		/**
		 * Creates new object from json representation
		 * @param json json object
		 * *
		 * @return java object
		 */
		@Throws(JSONException::class)
		fun fromJson(json : JSONObject) : Couple {
			val c = Couple()
			c.fromStation = json.optString("FromStation")
			c.toStation = json.optString("ToStation")

			val v = ArrayList<Vehicle>()
			val vehiclesArray = json.getJSONArray("VehiclesInCouple")
			for (index in 0..vehiclesArray.length() - 1) {
				v.add(Vehicle.fromJson(vehiclesArray.getJSONObject(index)))
			}
			c.vehicles = v
			return c
		}
	}
}
