package zelpage.razeni.bo;

import java.io.Serializable;
import org.json.JSONObject;

/**
 * Vehicle Business object
 * @author jindrichbr
 */
public class Vehicle implements Serializable{
    private String name;
    private String annotation;

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Creates Vehicle object from Json object
     * @param json json object
     * @return new Vehicle object
     */
    public static Vehicle fromJson(JSONObject json){
        Vehicle result = new Vehicle();
        result.setName(json.optString("Name"));
        result.setAnnotation(json.optString("Annotation"));
        return result;
    }
}
