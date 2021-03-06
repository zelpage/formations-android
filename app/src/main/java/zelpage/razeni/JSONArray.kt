package zelpage.razeni

import org.json.JSONArray
import java.util.ArrayList

fun JSONArray.toCoupleList() : ArrayList<Couple> {
	val list = ArrayList<Couple>()
	for (index in 0..length() - 1) {
		list.add(Couple.fromJson(getJSONObject(index)))
	}
	return list
}

fun JSONArray.toStringList() : ArrayList<String> {
	val list = ArrayList<String>()
	for (index in 0.until(length())) {
		list.add(optString(index))
	}
	return list
}
