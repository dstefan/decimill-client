package decimill.client.results;

/**
 * @author David Stefan
 */
public class Result {

    private final String body;
    private final boolean isError;
    
    public Result(String body, boolean error) {
        this.body = body;
        this.isError = error;
    }
    
    public Result(String body) {
        this(body, false);
    }
}
