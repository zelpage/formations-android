package zelpage.razeni.bo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * BO for razeni query
 * @author jindrichbr
 */
public class Request {

    private String trainNumberQuery;

    public String getTrainNumberQuery() {
        return trainNumberQuery;
    }

    public void setTrainNumberQuery(String trainNumberQuery) {
        this.trainNumberQuery = trainNumberQuery;
    }

    public JSONObject ToJson() throws JSONException {
        JSONObject result = new JSONObject();
        result.put("TrainNumberQuery", this.trainNumberQuery);

        return result;
    }
}
