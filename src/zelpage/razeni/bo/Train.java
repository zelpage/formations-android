package zelpage.razeni.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Business object for train data
 * @author jindrichbr
 */
public class Train implements Serializable {

    /**
     * train name
     */
    private String name;
    /**
     * train number
     */
    private String number;
    /**
     * train notes
     */
    private List<String> notes;
    /**
     * train deparatures in some stations
     */
    private List<String> deparatures;
    private List<Couple> couples;
    private String category;

    public List<Couple> getCouples() {
        return couples;
    }

    public void setCouples(List<Couple> couples) {
        this.couples = couples;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<String> getDeparatures() {
        return deparatures;
    }

    public void setDeparatures(List<String> deparatures) {
        this.deparatures = deparatures;
    }

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

    /**
     * Creates Train object from its json representation
     * @param json object
     * @return Train object
     */
    public static Train FromJson(JSONObject json) throws JSONException {
        Train t = new Train();

        t.setName(json.optString("TrainName"));
        t.setNumber(json.optString("ID"));
        t.setCategory(json.optString("Category"));

        JSONArray depArray = json.optJSONArray("Deparatures");
        List<String> resDeparatures = new ArrayList<String>();
        for (int index = 0; index < depArray.length(); index++) {
            resDeparatures.add(depArray.optString(index));
        }
        t.setDeparatures(resDeparatures);

        JSONArray noteArray = json.optJSONArray("Notes");
        List<String> resNotes = new ArrayList<String>();
        for (int index = 0; index < noteArray.length(); index++) {
            resNotes.add(noteArray.optString(index));
        }
        t.setNotes(resNotes);

        JSONArray jsonCouples = json.optJSONArray("Couples");
        List<Couple> resCouples = new ArrayList<Couple>();
        for (int index = 0; index < jsonCouples.length(); index++) {
            resCouples.add(Couple.fromJson(jsonCouples.getJSONObject(index)));
        }
        t.setCouples(resCouples);

        return t;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", this.category, this.number, this.name == null ? "" : this.name);
    }
}
