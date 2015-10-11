package decimill.client;

import decimill.client.results.Result;
import decimill.client.results.CompilerExceptionResult;
import com.google.gson.Gson;
import decimill.Context;
import decimill.FunctionsCollection;
import decimill.client.persistence.Persistence;
import decimill.lang.DM_Object;
import decimill.model.Model;
import decimill.model.ModelCompiler;
import decimill.model.Node;
import decimill.parser.CompilerException;
import decimill.query.QueryCompiler;
import decimill.scenario.Scenario;
import decimill.scenario.ScenarioCompiler;
import java.io.File;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import jviz.Dot;
import jviz.JvizException;

/**
 * @author David Stefan
 */
public class Server {

    private final Connection conn;
    private final PrintStream out;
    private final Properties config;

    public Server(Connection conn, PrintStream out, Properties config) {
        this.conn = conn;
        this.out = out;
        this.config = config;
    }

    public void compileModel(int id) {

        Persistence.Model modeldb = Persistence.loadModel(conn, id);

        if (modeldb == null) {
            err("Model id " + id + " doesn't exist.");
        }

        Model model = new Model(modeldb.id, modeldb.namespace);
        String modelText = "";

        try {
            modelText = ModelCompiler.compile(model, modeldb.text);
        } catch (CompilerException e) {
            err(new CompilerExceptionResult(e.getMessage(), e.getLine(), e.getCharPos()));
        }

        Dot dot = new Dot();

        try {
            String dotOutPath = config.getProperty("dot.out");
            File dir = new File(dotOutPath + model.getId());

            if (!dir.exists()) {
                dir.mkdir();
            } else {
                for (File file : dir.listFiles()) {
                    file.delete();
                }
            }

            if (model.hasNodes()) {
                dot.png(model.toDot(), dotOutPath + model.getId() + "/full.png");
            }

            for (Node node : model.getNodes().values()) {
                dot.png(node.toDot(), dotOutPath + model.getId() + "/" + node.getId() + ".png");
            }
        } catch (JvizException e) {
            err(e.getMessage());
        }

        out(modelText);
    }

    public void compileScenario(int id) {
        
        Persistence.Scenario scenariodb = Persistence.loadScenario(conn, id);

        if (scenariodb == null) {
            err("Scenario id " + id + " doesn't exist.");
        }

        Scenario scenario = new Scenario(scenariodb.id, scenariodb.namespace);
        String scenarioText = "";

        try {
            scenarioText = ScenarioCompiler.compile(scenario, scenariodb.text);
        } catch (CompilerException e) {
            err(new CompilerExceptionResult(e.toString(), e.getLine(), e.getCharPos()));
        }

        out(scenarioText);
    }

    public void compileQuery(int id) {

        Persistence.Query querydb = Persistence.loadQuery(conn, id);

        if (querydb == null) {
            err("Query id " + id + " doesn't exist.");
        }

        int studyId = querydb.studyId;

        ArrayList<Persistence.Model> modelsdb = Persistence.loadAllModels(conn, studyId);
        ArrayList<Persistence.Scenario> scenarios = Persistence.loadAllScenarios(conn, studyId);

        DM_Object.sampleSize = 10000;
        Context ctx = new Context();
        
        // Add functions to the context
        FunctionsCollection.add(ctx);

        for (Persistence.Model modeldb : modelsdb) {

            int modelId = modeldb.id;
            String namespace = modeldb.namespace;
            Model m = new Model(modelId, namespace);

            try {
                ModelCompiler.compile(m, modeldb.text);
                ctx.addModel(m);
            } catch (CompilerException e) {
                err(new CompilerExceptionResult(e.getMessage(), e.getLine(), e.getCharPos(), "model", m.getId()));
            }
        }

        for (Persistence.Scenario scenario : scenarios) {
            
            int scenarioId = scenario.id;
            String namespace = scenario.namespace;
            Scenario s = new Scenario(scenarioId, namespace);

            try {
                ScenarioCompiler.compile(s, scenario.text);
                ctx.addScenario(s);
            } catch (CompilerException e) {
                err(new CompilerExceptionResult(e.getMessage(), e.getLine(), e.getCharPos(), "scenario", s.getId()));
            }
        }

        String compiled = "";

        try {
            compiled = QueryCompiler.compile(ctx, querydb.text);
        } catch (CompilerException e) {
            err(new CompilerExceptionResult(e.getMessage(), e.getLine(), e.getCharPos()));
        }

        out(compiled);
    }

    private void closeConn() {
        try {
            conn.close();
        } catch (SQLException e) {
            Gson gson = new Gson();
            out.println(gson.toJson(new Result(e.getMessage(), true)));
            System.exit(0);
        }
    }

    private void out(String body) {
        closeConn();
        Gson gson = new Gson();
        out.println(gson.toJson(new Result(body)));
        out.close();
        System.exit(0);
    }

    private void out(Result res) {
        closeConn();
        Gson gson = new Gson();
        out.println(gson.toJson(res));
        out.close();
        System.exit(0);
    }

    private void err(String error) {
        closeConn();
        Gson gson = new Gson();
        out.println(gson.toJson(new Result(error, true)));
        out.close();
        System.exit(0);
    }

    private void err(Result res) {
        closeConn();
        Gson gson = new Gson();
        out.println(gson.toJson(res));
        out.close();
        System.exit(0);
    }
}
