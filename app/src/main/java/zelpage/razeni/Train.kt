package zelpage.razeni

import java.io.Serializable
import org.json.JSONException
import org.json.JSONObject

data class Train(
	var name : String? = null,
	var number : String? = null,
	var notes : List<String>? = null,
	var departures : List<String>? = null,
	var couples : List<Couple>? = null,
	var category : String? = null) : Serializable {

	companion object {

		@Throws(JSONException::class)
		fun fromJson(json : JSONObject) : Train =
			Train(
				name = json.optString("TrainName"),
				number = json.optString("ID"),
				category = json.optString("Category"),
				departures = json.optJSONArray("Deparatures").toStringList(),
				notes = json.optJSONArray("Notes").toStringList(),
				couples = json.optJSONArray("Couples").toCoupleList())

	}

	override fun toString() = "$category $number $name"

}
