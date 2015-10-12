
package decimill.server;

/**
 * @author David
 */
public class DecimillServer {

    /**
     * @param args
     */
    public static void main(String[] args) {
        
        // Make sure that the number of parameters is exactly one
        if (args.length != 1) {
            System.out.println("Example usage: \"java -jar decimill-server.jar 88\"");
            System.exit(-1);
        }
        
        int portNumber = 0;
        
        // Try to parse the first parameter into a port number
        try {
            portNumber = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Port number must be an integer");
            System.exit(-1);
        }
        
        
    }
    
}
