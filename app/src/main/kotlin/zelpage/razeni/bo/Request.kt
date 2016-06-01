package zelpage.razeni.bo

import org.json.JSONException
import org.json.JSONObject

/**
 * BO for razeni query
 */
data class Request(
	var trainNumberQuery: String? = null) {

	@Throws(JSONException::class)
	fun ToJson(): JSONObject {
		val result = JSONObject()
		result.put("TrainNumberQuery", this.trainNumberQuery)

		return result
	}

}
