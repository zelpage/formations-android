package zelpage.razeni;

import java.util.ArrayList;
import java.util.List;

import zelpage.razeni.bo.Couple;
import zelpage.razeni.bo.Train;
import zelpage.razeni.bo.Vehicle;

import android.os.AsyncTask;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Async task for getting train
 * 
 * @author jindrichbr
 */
public class GetTrainAsyncTask extends AsyncTask<String, Void, List<Train>> {

	private View parent;
	private Train trainResult;
	private TrainFormations api;

	public GetTrainAsyncTask(View p, Train t) {
		this.parent = p;
		this.trainResult = t;

		this.api = new TrainFormations();
	}

	@Override
	protected List<Train> doInBackground(String... trainNumber) {
		if (this.trainResult == null) {
			try {
				List<Train> trains = api.getTrain(trainNumber[0]);
				return trains;

			} catch (Exception ex) {
				return new ArrayList<Train>();
			}
		} else {
			List<Train> r = new ArrayList<Train>();
			r.add(this.trainResult);
			return r;
		}
	}

	@Override
	protected void onPostExecute(List<Train> result) {

		this.parent.findViewById(R.id.progress).setVisibility(View.GONE);

		if (result.size() == 1) {

			EditText traintNumberTB = (EditText) this.parent
					.findViewById(R.id.trainnumtb);
			traintNumberTB.setText("");

			this.trainResult = result.get(0);
			// process response
			TextView trainInfo = (TextView) this.parent
					.findViewById(R.id.label);
			TextView couples = (TextView) this.parent
					.findViewById(R.id.couples);
			TextView deparatures = (TextView) this.parent
					.findViewById(R.id.deparatures);
			this.parent.findViewById(R.id.deparatureslabel).setVisibility(View.VISIBLE);
			TextView notes = (TextView) this.parent.findViewById(R.id.notes);

			if (this.trainResult.getName().compareTo("") == 0) {
				trainInfo.setText(String.format("%s %s",
						this.trainResult.getCategory(),
						this.trainResult.getNumber()));
			} else {
				trainInfo.setText(String.format("%s %s '%s'",
						this.trainResult.getCategory(),
						this.trainResult.getNumber(),
						this.trainResult.getName()));
			}
			trainInfo.invalidate();

			// main couples
			String htmlData = "";
			if (this.trainResult.getCouples().size() == 1) {
				Couple c = this.trainResult.getCouples().get(0);

				for (Vehicle v : c.getVehicles()) {
					htmlData += String.format("<b>%s</b> <i>%s</i>",
							v.getName(), v.getAnnotation());
					htmlData += "<br/>";
				}
			} else {
				for (Couple c : this.trainResult.getCouples()) {

					if (c.getFromStation() != null && c.getToStation() != null) {
						htmlData += String.format(
								"Řazení v úseku %s - %s<br/>",
								c.getFromStation(), c.getToStation());
					}
					for (Vehicle v : c.getVehicles()) {
						htmlData += String.format("<b>%s</b> <i>%s</i>",
								v.getName(), v.getAnnotation());
						htmlData += "<br/>";
					}
					htmlData += "<br/>";
				}
				htmlData = htmlData.substring(0, htmlData.lastIndexOf("<br/>"));
			}
			couples.setText(Html.fromHtml(htmlData));

			String nText = "";
			for (String singleNote : this.trainResult.getNotes()) {
				nText += singleNote + "\n";
			}
			notes.setText(nText);

			nText = "";
			for (String singleNote : this.trainResult.getDepartures()) {
				nText += singleNote + "\n";
			}
			deparatures.setText(nText);
			return;
		} else if (result.isEmpty()) {
			Toast.makeText(this.parent.getContext(), "Žádný vlak nenalezen",
					Toast.LENGTH_LONG).show();
		} else {
			ListView lv = (ListView) this.parent
					.findViewById(R.id.trainsselector);
			lv.setDividerHeight(1);

			ArrayAdapter<Train> data = new ArrayAdapter<Train>(
					this.parent.getContext(), R.layout.listitem, result);
			lv.setAdapter(data);
			lv.setVisibility(View.VISIBLE);
		}

		// hide all
		TextView trainInfo = (TextView) this.parent.findViewById(R.id.label);
		TextView couples = (TextView) this.parent.findViewById(R.id.couples);
		TextView deparatures = (TextView) this.parent
				.findViewById(R.id.deparatures);
		this.parent.findViewById(R.id.deparatureslabel)
				.setVisibility(View.GONE);
		TextView notes = (TextView) this.parent.findViewById(R.id.notes);
		trainInfo.setText("");
		couples.setText("");
		notes.setText("");
		deparatures.setText("");

	}

	@Override
	protected void onPreExecute() {
		this.parent.findViewById(R.id.progress).setVisibility(View.VISIBLE);
		this.parent.findViewById(R.id.trainsselector).setVisibility(View.GONE);
		// hide all
		TextView trainInfo = (TextView) this.parent.findViewById(R.id.label);
		TextView couples = (TextView) this.parent.findViewById(R.id.couples);
		TextView deparatures = (TextView) this.parent
				.findViewById(R.id.deparatures);
		this.parent.findViewById(R.id.deparatureslabel)
				.setVisibility(View.GONE);
		TextView notes = (TextView) this.parent.findViewById(R.id.notes);
		trainInfo.setText("");
		couples.setText("");
		notes.setText("");
		deparatures.setText("");
	}

	public Train getTrainResult() {
		return trainResult;
	}
}
