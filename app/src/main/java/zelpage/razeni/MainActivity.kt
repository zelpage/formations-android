package zelpage.razeni

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView.OnItemClickListener
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView.OnEditorActionListener
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.longToast
import java.util.concurrent.ExecutionException

class MainActivity : Activity(), AnkoLogger {

	private lateinit var backgroundThread : GetTrainAsyncTask
	private lateinit var restoredTrain : Train

	/** Called when the activity is first created.  */
	public override fun onCreate(savedInstanceState : Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.main)

		if (savedInstanceState != null && savedInstanceState.containsKey("train")) {
			this.restoredTrain = savedInstanceState.get("train") as Train
			backgroundThread = GetTrainAsyncTask((findViewById(R.id.trainnumtb) as EditText).rootView, restoredTrain)
			val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
			imm.hideSoftInputFromWindow(findViewById(R.id.trainnumtb).windowToken, 0)
			backgroundThread.execute("")

		}

		val lv = findViewById(R.id.trainsselector) as ListView

		lv.onItemClickListener = OnItemClickListener { parent, view, position, l ->
			val train : Train = parent.getItemAtPosition(position) as Train
			val traintNumberTB : EditText = findViewById(R.id.trainnumtb) as EditText

			traintNumberTB.setText(train.number)

			backgroundThread = GetTrainAsyncTask((findViewById(R.id.trainnumtb) as EditText).rootView, null)
			backgroundThread.execute(String.format("*%s*", train.number))
		}

		val traintNumberTB = findViewById(R.id.trainnumtb) as EditText
		traintNumberTB.setOnEditorActionListener(OnEditorActionListener { arg0, arg1, arg2 ->
			val rt = arg0 as EditText

			if (arg1 == EditorInfo.IME_ACTION_DONE) {
				try {
					if ("".compareTo(rt.text.toString()) == 0) {
						return@OnEditorActionListener true
					}

					val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
					imm.hideSoftInputFromWindow(rt.windowToken, 0)

					backgroundThread = GetTrainAsyncTask(arg0.getRootView(), null)
					backgroundThread.execute(rt.text.toString())

				} catch (ex : Exception) {
					longToast(ex.message!!)
				}

				true
			} else {
				false
			}
		})
	}

	override fun onSaveInstanceState(outState : Bundle) {
		try {
			if (this.backgroundThread.get() != null) {
				outState.putSerializable("train", this.backgroundThread.trainResult)
			}
		} catch (ex : InterruptedException) {
			info(null, ex)
		} catch (ex : ExecutionException) {
			info(null, ex)
		}

	}

	override fun onPause() {
		super.onPause()
	}

}
