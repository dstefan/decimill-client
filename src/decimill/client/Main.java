package decimill.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * A command line utility that accepts
 * 
 * @author David Stefan
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
//        args = new String[]{"C:/Users/David/Dev/dmtools/apps/decimill-client/config", "-a", "compileModel", "-m", "1"};
//        args = new String[]{"C:/Users/David/Dev/dmtools/apps/decimill-client/config", "-a", "compileScenario", "-s", "1"};
//        args = new String[]{"C:/Users/David/Dev/dmtools/apps/decimill-client/config", "-a", "compileQuery", "-q", "3"};
//        args = new String[] {"C:/dev/projects/decimill/apps/decimill-client/config", "-a", "compileModel", "-m", "1"};
        
        if (args.length == 0) {
            System.out.println("Path to config file is missing");
            System.exit(-1);
        }
        
        Properties config = new Properties();

        try {
            InputStream configIn = new FileInputStream(args[0]);
            config.load(configIn);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }

        // Initialise database
        String mysqlUser = config.getProperty("mysql.user");
        String mysqlPassword = config.getProperty("mysql.password");
        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/decimill?useUnicode=true&characterEncoding=UTF-8&user=" + mysqlUser + "&password=" + mysqlPassword);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }

        // Initilise out stream
        PrintStream out = null;
        
        try {
            out = new PrintStream(System.out, true, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        
        Dispatcher dispatcher = new Dispatcher(conn, out, config);
        dispatcher.dispatch(args);
        
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

}
