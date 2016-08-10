package zelpage.razeni

import android.app.Activity
import android.net.Uri
import android.os.Bundle

class TrainView : Activity() {
	public override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.trainview)

		val data : Uri = this.intent.data

		val trainnumber = data.host
	}
}
