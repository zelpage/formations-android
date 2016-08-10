package zelpage.razeni

import org.json.JSONObject
import java.io.Serializable

data class Vehicle (
	var name: String? = null,
	var annotation: String? = null) : Serializable {

	companion object {

		fun fromJson(json : JSONObject) : Vehicle =
			Vehicle(
				name = json.optString("Name"),
				annotation = json.optString("Annotation"))

	}

}
