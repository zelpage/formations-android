package zelpage.razeni

import org.json.JSONException
import org.json.JSONObject

data class Request(
	var trainNumberQuery: String? = null) {

	@Throws(JSONException::class)
	fun ToJson(): JSONObject {
		val result = JSONObject()
		result.put("TrainNumberQuery", this.trainNumberQuery)

		return result
	}

}
