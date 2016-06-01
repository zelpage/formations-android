package zelpage.razeni;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import zelpage.razeni.bo.Request;
import zelpage.razeni.bo.Train;

/**
 * Class for retrieving data from ZP
 * @author jindrichbr
 */
public class CommObject {

    /**
     * Url address of data provider endpoint
     */
    public static final String DATA_PROVIDER_URL = "http://www.polenius-laboratories.cz/RazeniProvider.ashx";

    /**
     * Get Train by its number
     * @param trainNumber train number
     * @return Train Business Objects. 1 if exact match, 0 if none found, more if ambiguous
     */
    public static List<Train> getTrain(String trainNumber) throws Exception {
        Request req = new Request();
        req.setTrainNumberQuery(trainNumber);
        
        JSONObject response = new JSONObject(  doServerCall(DATA_PROVIDER_URL, req.ToJson()));
        ArrayList<Train> result = new ArrayList<Train>();
        
        int traincount = response.getInt("MatchCount");
        if (traincount == 1){
            result.add(Train.Companion.fromJson(response.getJSONObject("MatchingTrain")));
            return  result;
        }else if (traincount == 0){
            return result;
        }else{
            JSONArray candidates = response.getJSONArray("SelectableTrains");
            for(int index = 0;index<candidates.length();index++){
                Train t = new Train();
                t.setNumber(candidates.getJSONObject(index).getString("ID"));
                t.setCategory(candidates.getJSONObject(index).getString("Category"));
                t.setName(candidates.getJSONObject(index).getString("TrainName"));
                result.add(t);
            }
        }
        
        return result;
        
    }

    /**
     * Volání backend serveru
     * @param url HTTP GET adresa 
     * @param request json request
     * @return výsledek volání (raw text)
     * @throws Exception pokud dojde k chybě
     */
    private static String doServerCall(String url, JSONObject request) throws Exception {
        try {
            HttpPost wsRequest = new HttpPost(url);
            System.out.println("Backend server call url> " + wsRequest.getURI().toString());
            
            //insert json request body
            HttpEntity requestEntity = new StringEntity(request.toString());
            wsRequest.setEntity(requestEntity);
            
            HttpClient wsClient = new DefaultHttpClient();
            HttpResponse wsResponse = wsClient.execute(wsRequest);

            InputStreamReader isr = new InputStreamReader(wsResponse.getEntity().getContent());
            int r = isr.read();
            StringBuilder sb = new StringBuilder();
            while (r != -1) {
                sb.append((char) r);
                r = isr.read();
            }
            isr.close();
            System.out.println("Backend server call response> " + sb.toString());
            return sb.toString();

        } catch (Exception ex) {
            throw new Exception("Chyba při volání backend serveru", ex);
        }
    }
}
