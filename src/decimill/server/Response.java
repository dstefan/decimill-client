
package decimill.server;

import org.json.JSONObject;

/**
 * @author David
 */
public class Response {
    
    public String status;
    public String body;
    
    public Response(String status, String body) {
        this.status = status;
        this.body = body;
    }
    
    @Override
    public String toString() {
        
        JSONObject json = new JSONObject();
        json.put("status", status);
        json.put("body", body);
        
        return json.toString();
    }
}
