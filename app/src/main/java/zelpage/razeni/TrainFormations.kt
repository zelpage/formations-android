package zelpage.razeni

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.json.JSONArray
import org.json.JSONObject
import zelpage.razeni.Request
import zelpage.razeni.Train
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.ArrayList
import java.util.Scanner

/**
 * Train Formations Web API implementation to access data from
 * www.razeni.cz.
 */
class TrainFormations : AnkoLogger {

	/**
	 * Url address of data provider endpoint
	 */
	val DATA_PROVIDER_URL = "http://www.polenius-laboratories.cz/RazeniProvider.ashx"

	/**
	 * Get Train by its number
	 * @param trainNumber train number
	 * *
	 * @return Train Business Objects. 1 if exact match, 0 if none found, more if ambiguous
	 */
	fun getTrain(trainNumber : String) : List<Train> {
		val req = Request()
		req.trainNumberQuery = trainNumber

		val response = JSONObject(doServerCall(DATA_PROVIDER_URL, req.ToJson()))
		val result = ArrayList<Train>()

		val trainCount = response.getInt("MatchCount")

		when (trainCount) {
			0 -> {}
			1 -> result.add(Train.fromJson(response.getJSONObject("MatchingTrain")))
			else -> {
				val candidates : JSONArray = response.getJSONArray("SelectableTrains")

				for (index in 0.until(candidates.length())) {
					val candidate = candidates.getJSONObject(index)

					result.add(
						Train(
							category = candidate.getString("Category"),
							number = candidate.getString("ID"),
							name = candidate.getString("TrainName")))
				}
			}
		}

		return result
	}

	private fun doServerCall(endpoint : String, request : JSONObject) : String {
		info("Backend server call url> $endpoint")

		try {
			val bytePayload = request.toString().toByteArray(StandardCharsets.UTF_8)

			val url : URL = URL(endpoint)
			val conn : HttpURLConnection = url.openConnection() as HttpURLConnection

			conn.requestMethod = "POST"
			conn.setRequestProperty("Content-Type", "application/json")
			conn.setRequestProperty("Charset", StandardCharsets.UTF_8.displayName())
			conn.useCaches = false
			conn.doOutput = true
			conn.doInput = true

			val outStream = conn.outputStream

			outStream.write(bytePayload)
			outStream.flush()
			outStream.close()

			val inStream = conn.inputStream
			val scanner = Scanner(inStream, "UTF-8").useDelimiter("\\A")
			val response = scanner.next()

			info("Backend server call response> $response")

			return response

		} catch (ex : Exception) {
			throw RuntimeException("An error while accessing train formation data at server.", ex)
		}
	}
}
