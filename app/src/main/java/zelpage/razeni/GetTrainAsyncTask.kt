package zelpage.razeni

import java.util.ArrayList

import android.os.AsyncTask
import android.text.Html
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class GetTrainAsyncTask(val parent : View, val t : Train?) : AsyncTask<String, Void, List<Train>>(), AnkoLogger {

	var trainResult : Train? = null
		private set
	private val api : TrainFormations

	init {
		this.trainResult = t

		this.api = TrainFormations()
	}

	override fun doInBackground(vararg trainNumber : String) : List<Train> {
		if (this.trainResult == null) {
			try {
				val trains = api.getTrain(trainNumber[0])
				return trains
			} catch (ex : Exception) {
				info(ex.message, ex)

				return ArrayList()
			}
		} else {
			val r = ArrayList<Train>()
			r.add(this.trainResult!!)
			return r
		}
	}

	override fun onPostExecute(result: List<Train>) {

		this.parent.findViewById(R.id.progress).visibility = View.GONE

		if (result.size == 1) {

			val traintNumberTB = this.parent.findViewById(R.id.trainnumtb) as EditText
			traintNumberTB.setText("")

			this.trainResult = result[0]

			val trainInfo = this.parent.findViewById(R.id.label) as TextView
			val couples = this.parent.findViewById(R.id.couples) as TextView
			val deparatures = this.parent.findViewById(R.id.deparatures) as TextView
			this.parent.findViewById(R.id.deparatureslabel).visibility = View.VISIBLE
			val notes = this.parent.findViewById(R.id.notes) as TextView

			if (this.trainResult!!.name!!.compareTo("") == 0) {
				trainInfo.text = String.format("%s %s",
					this.trainResult!!.category,
					this.trainResult!!.number)
			} else {
				trainInfo.text = String.format("%s %s '%s'",
					this.trainResult!!.category,
					this.trainResult!!.number,
					this.trainResult!!.name)
			}
			trainInfo.invalidate()

			var htmlData = ""
			if (this.trainResult!!.couples!!.size == 1) {
				val c = this.trainResult!!.couples!![0]

				for ((name, annotation) in c.vehicles!!) {
					htmlData += String.format("<b>%s</b> <i>%s</i>",
						name, annotation)
					htmlData += "<br/>"
				}
			} else {
				for ((fromStation, toStation, vehicles) in this.trainResult!!.couples!!) {

					if (fromStation != null && toStation != null) {
						htmlData += String.format(
							"Řazení v úseku %s - %s<br/>",
							fromStation, toStation)
					}
					for ((name, annotation) in vehicles!!) {
						htmlData += String.format("<b>%s</b> <i>%s</i>",
							name, annotation)
						htmlData += "<br/>"
					}
					htmlData += "<br/>"
				}
				htmlData = htmlData.substring(0, htmlData.lastIndexOf("<br/>"))
			}
			couples.text = Html.fromHtml(htmlData)

			var nText = ""
			for (singleNote in this.trainResult!!.notes!!) {
				nText += singleNote + "\n"
			}
			notes.text = nText

			nText = ""
			for (singleNote in this.trainResult!!.departures!!) {
				nText += singleNote + "\n"
			}
			deparatures.text = nText
			return
		} else if (result.isEmpty()) {
			Toast.makeText(this.parent.context, "Žádný vlak nenalezen",
				Toast.LENGTH_LONG).show()
		} else {
			val lv = this.parent.findViewById(R.id.trainsselector) as ListView
			lv.dividerHeight = 1

			val data = ArrayAdapter(
				this.parent.context, R.layout.listitem, result)
			lv.adapter = data
			lv.visibility = View.VISIBLE
		}

		val trainInfo = this.parent.findViewById(R.id.label) as TextView
		val couples = this.parent.findViewById(R.id.couples) as TextView
		val deparatures = this.parent.findViewById(R.id.deparatures) as TextView
		this.parent.findViewById(R.id.deparatureslabel).visibility = View.GONE
		val notes = this.parent.findViewById(R.id.notes) as TextView
		trainInfo.text = ""
		couples.text = ""
		notes.text = ""
		deparatures.text = ""
	}

	override fun onPreExecute() {
		this.parent.findViewById(R.id.progress).visibility = View.VISIBLE
		this.parent.findViewById(R.id.trainsselector).visibility = View.GONE
		// hide all
		val trainInfo = this.parent.findViewById(R.id.label) as TextView
		val couples = this.parent.findViewById(R.id.couples) as TextView
		val deparatures = this.parent.findViewById(R.id.deparatures) as TextView
		this.parent.findViewById(R.id.deparatureslabel).visibility = View.GONE
		val notes = this.parent.findViewById(R.id.notes) as TextView
		trainInfo.text = ""
		couples.text = ""
		notes.text = ""
		deparatures.text = ""
	}
}
