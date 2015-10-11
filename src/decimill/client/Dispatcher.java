package decimill.client;

import java.io.PrintStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * @author David Stefan
 */
public class Dispatcher {

    private final Server server;

    public Dispatcher(Connection conn, PrintStream out, Properties config) {
        this.server = new Server(conn, out, config);
    }

    public void dispatch(String[] args) {

        int queryId = 0;
        int modelId = 0;
        int scenarioId = 0;
        String action = "";

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            switch (arg) {

                // Action parameter
                case "-a": {
                    if (args.length <= ++i) {
                        System.out.println("-a flag requires <action>");
                        System.exit(-1);
                    }
                    action = args[i];
                    break;
                }

                // Model ID parameter
                case "-m": {
                    if (args.length <= ++i) {
                        System.out.println("-m flag requires <modelId>");
                        System.exit(-1);
                    }
                    modelId = Integer.parseInt(args[i]);
                    break;
                }

                // Scenario ID parameter
                case "-s": {
                    if (args.length <= ++i) {
                        System.out.println("-s flag requires <scenarioId>");
                        System.exit(-1);
                    }
                    scenarioId = Integer.parseInt(args[i]);
                    break;
                }

                // Query ID parameter
                case "-q": {
                    if (args.length <= ++i) {
                        System.out.println("-q flag requires <queryId>");
                        System.exit(-1);
                    }
                    queryId = Integer.parseInt(args[i]);
                    break;
                }
            }
        }

        switch (action) {

            // Parse model
            case "compileModel": {
                if (modelId == 0) {
                    System.out.println("Action compileModel requires <modelId>");
                    System.exit(-1);
                }
                server.compileModel(modelId);
            }

            // Parse scenario
            case "compileScenario": {
                if (scenarioId == 0) {
                    System.out.println("Action compileScenario requires <scenarioId>");
                    System.exit(-1);
                }
                server.compileScenario(scenarioId);
            }

            // Parse query
            case "compileQuery": {
                if (queryId == 0) {
                    System.out.println("Action compileQuery requires <caseId>");
                    System.exit(-1);
                }
                server.compileQuery(queryId);
            }

            default: {
                System.out.println("Action '" + action + "' doesn't exist");
                System.exit(-1);
            }
        }
    }
}
