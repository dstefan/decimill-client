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

            switch (key) {

                case "action": {
                    request.action = json.getString(key);
                    break;
                }

                case "model": {
                    JSONObject jsonModel = json.getJSONObject(key);
                    request.model = new Model();
                    request.model.id = jsonModel.getInt("id");
                    request.model.text = jsonModel.getString("text");
                    request.model.namespace = jsonModel.getString("namespace");
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
                
                case "scenario": {
                    JSONObject jsonScenario = json.getJSONObject(key);
                    request.scenario = new Scenario();
                    request.scenario.id = jsonScenario.getInt("id");
                    request.scenario.text = jsonScenario.getString("text");
                    request.scenario.namespace = jsonScenario.getString("namespace");
                    break;
                }
                
                case "scenarios": {
                    JSONArray jsonScenariosArray = json.getJSONArray(key);
                    request.scenarios = new ArrayList();
                    for (int i = 0; i < jsonScenariosArray.length(); i++) {
                        JSONObject jsonScenario = jsonScenariosArray.getJSONObject(i);
                        Scenario scenario = new Scenario();
                        scenario.id = jsonScenario.getInt("id");
                        scenario.text = jsonScenario.getString("text");
                        scenario.namespace = jsonScenario.getString("namespace");
                        request.scenarios.add(scenario);
                    }
                    break;
                }
                
                case "query": {
                    JSONObject jsonQuery = json.getJSONObject(key);
                    request.query = new Query();
                    request.query.id = jsonQuery.getInt("id");
                    request.query.text = jsonQuery.getString("text");
                    request.query.studyId = jsonQuery.getInt("studuId");
                    break;
                }
            }
        }

        return request;
    }

    @Override
    public String toString() {
        
        JSONObject json = new JSONObject();
        json.append("action", action);
        
        if (model != null) {
            json.put("model", new JSONObject());
            json.getJSONObject("model").append("id", model.id);
            json.getJSONObject("model").append("namespace", model.namespace);
            json.getJSONObject("model").append("text", model.text);
        }
        
        if (scenario != null) {
            json.put("scenario", new JSONObject());
            json.getJSONObject("scenario").append("id", scenario.id);
            json.getJSONObject("scenario").append("namespace", scenario.namespace);
            json.getJSONObject("scenario").append("text", scenario.text);
        }
        
        return json.toString();
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
    }
}
