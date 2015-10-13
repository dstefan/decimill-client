package decimill.server;

import org.json.JSONObject;

/**
 * @author David Stefan
 */
public class CompilerExceptionResponse extends Response {

    public final int line;
    public final int charPos;
    public final String target;
    public final int targetId;

    public CompilerExceptionResponse(String message, int line, int charPos, String target, int targetId) {
        super("Error", message);
        this.line = line;
        this.charPos = charPos;
        this.target = target;
        this.targetId = targetId;
    }

    public CompilerExceptionResponse(String message, int line, int charPos) {
        this(message, line, charPos, null, 0);
    }
    
    @Override
    public String toString() {
        
        JSONObject json = new JSONObject();
        json.put("status", status);
        json.put("body", body);
        json.put("line", line);
        json.put("charPos", charPos);
        json.put("target", target);
        json.put("targetId", targetId);
        
        return json.toString();
    }
}
