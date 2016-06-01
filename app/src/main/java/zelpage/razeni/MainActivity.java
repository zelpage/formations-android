package zelpage.razeni;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import zelpage.razeni.bo.Train;

public class MainActivity extends Activity {

    private GetTrainAsyncTask backgroundThread = null;
    private Train restoredTrain = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (savedInstanceState != null && savedInstanceState.containsKey("train")) {
            this.restoredTrain = (Train) savedInstanceState.get("train");
            backgroundThread = new GetTrainAsyncTask(((EditText) findViewById(R.id.trainnumtb)).getRootView(), restoredTrain);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(findViewById(R.id.trainnumtb).getWindowToken(), 0);
            backgroundThread.execute("");

        }

        ListView lv = (ListView) findViewById(R.id.trainsselector);
        lv.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                Train train = (Train) parent.getItemAtPosition(position);
                EditText traintNumberTB = (EditText) findViewById(R.id.trainnumtb);
                traintNumberTB.setText(train.getNumber());
                backgroundThread = new GetTrainAsyncTask(((EditText) findViewById(R.id.trainnumtb)).getRootView(), null);
                backgroundThread.execute(String.format("*%s*", train.getNumber()));
            }
        });


        EditText traintNumberTB = (EditText) findViewById(R.id.trainnumtb);
        traintNumberTB.setOnEditorActionListener(new OnEditorActionListener() {

            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                EditText rt = (EditText) arg0;

                if (arg1 == EditorInfo.IME_ACTION_DONE) {
                    try {
                        if (rt.getText().toString().compareTo("") == 0) {
                            return true;
                        }

                        //Train t = CommObject.getTrain(rt.getText().toString());
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(rt.getWindowToken(), 0);

                        backgroundThread = new GetTrainAsyncTask(arg0.getRootView(), null);
                        backgroundThread.execute(rt.getText().toString());

                    } catch (Exception ex) {
                        Toast.makeText(arg0.getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        try {
            if (this.backgroundThread != null && this.backgroundThread.get() != null) {
                outState.putSerializable("train", this.backgroundThread.getTrainResult());
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    
    
}
