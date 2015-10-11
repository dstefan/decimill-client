package decimill.client.results;

/**
 * @author David Stefan
 */
public class CompilerExceptionResult extends Result {

    private final int line;
    private final int charPos;
    private final String target;
    private final int targetId;
    
    public CompilerExceptionResult(String message, int line, int charPos, String target, int targetId) {
        super(message, true);
        this.line = line;
        this.charPos = charPos;
        this.target = target;
        this.targetId = targetId;
    }
    
    public CompilerExceptionResult(String message, int line, int charPos) {
        this(message, line, charPos, null, 0);
    }
}
