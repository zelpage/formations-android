package zelpage.razeni.bo

import java.io.Serializable
import org.json.JSONException
import org.json.JSONObject
import zelpage.extension.toCoupleList
import zelpage.extension.toStringList

/**
 * Business object for train data
 */
data class Train(
	var name: String? = null,
	var number: String? = null,
	var notes: List<String>? = null,
	var departures: List<String>? = null,
	var couples: List<Couple>? = null,
	var category: String? = null) : Serializable {

	companion object {

		/**
		 * Creates Train object from its json representation
		 * @param json object
		 * *
		 * @return Train object
		 */
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

	override fun toString() = category + " " + number + " " + name

}
