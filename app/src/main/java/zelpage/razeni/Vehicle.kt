package zelpage.razeni

import org.json.JSONObject
import java.io.Serializable

/**
 * Vehicle Business object
 */
data class Vehicle (
	var name: String? = null,
	var annotation: String? = null) : Serializable {

	companion object {

		/**
		 * Creates Vehicle object from Json object
		 * @param json json object
		 *
		 * @return new Vehicle object
		 */
		fun fromJson(json : JSONObject) : Vehicle =
			Vehicle(
				name = json.optString("Name"),
				annotation = json.optString("Annotation"))

	}

}
