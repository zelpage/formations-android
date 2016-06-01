package zelpage.razeni;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class TrainView extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trainview);
		
		Intent i = this.getIntent();
		Uri data = i.getData();
		
		String trainnumber = data.getHost();
		
		
	}
}
