package zelpage.extension

import org.json.JSONArray
import zelpage.razeni.bo.Couple
import java.util.ArrayList

fun JSONArray.toCoupleList() : ArrayList<Couple> {
	val list = ArrayList<Couple>()
	for (index in 0..length() - 1) {
		list.add(Couple.Companion.fromJson(getJSONObject(index)))
	}
	return list
}

fun JSONArray.toStringList() : ArrayList<String> {
	val list = ArrayList<String>()
	for (index in 0..length() - 1) {
		list.add(optString(index))
	}
	return list
}
