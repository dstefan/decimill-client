package decimill.server;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author David
 */
public class Request {

    public String action;
    public Model model;
    public Scenario scenario;
    public Query query;
    public ArrayList<Model> models;
    public ArrayList<Scenario> scenarios;

    public static Request parseRequest(String requestString)
            throws JSONException {

        JSONObject json = new JSONObject(requestString);
        String[] keys = JSONObject.getNames(json);

        Request request = new Request();

        for (String key : keys) {
            
            // TODO: Add case for query, scenario and an array of scenarios
            
            switch (key) {
                case "action": {
                    request.action = json.getString(key);
                    System.out.println("action: " + request.action);
                    break;
                }
                case "model": {
                    JSONObject jsonModel = json.getJSONObject(key);
                    request.model = new Model();
                    request.model.id = jsonModel.getInt("id");
                    request.model.text = jsonModel.getString("text");
                    request.model.namespace = jsonModel.getString("namespace");
                    System.out.println("Model namespace: " + request.model.namespace);
                    break;
                }
                case "models": {
                    JSONArray jsonModelsArray = json.getJSONArray(key);
                    request.models = new ArrayList();
                    for (int i = 0; i < jsonModelsArray.length(); i++) {
                        JSONObject jsonModel = jsonModelsArray.getJSONObject(i);
                        Model model = new Model();
                        model.id = jsonModel.getInt("id");
                        model.text = jsonModel.getString("text");
                        model.namespace = jsonModel.getString("namespace");
                        request.models.add(model);
                    }
                    break;
                }
            }
        }
        
        return request;
    }
    
    @Override
    public String toString() {
        return "Request";
    }

    public static class Model {

        public int id;
        public String text;
        public String namespace;
    }

    public static class Scenario {

        public int id;
        public String text;
        public String namespace;
    }

    public static class Query {

        public int id;
        public int studyId;
        public String text;
        public String compiled;
    }
}
