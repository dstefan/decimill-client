package decimill.server;

import org.json.JSONObject;

/**
 * @author David
 */
public class Response {
    
    public final String status;
    public final String body;
    public final String path;
    
    public Response(String status, String body, String path) {
        this.status = status;
        this.body = body;
        this.path = path;
    }
    
    public Response(String status, String body) {
        this(status, body, "");
    }
    
    @Override
    public String toString() {
        
        JSONObject json = new JSONObject();
        json.put("status", status);
        json.put("body", body);
        json.put("path", path);
        
        return json.toString();
    }
}
