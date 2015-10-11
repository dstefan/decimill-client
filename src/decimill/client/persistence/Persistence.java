package decimill.client.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @author David
 */
public class Persistence {

    public static class Model {

        public final int id;
        public final String text;
        public final String namespace;

        public Model(int id, String text, String namespace) {
            this.id = id;
            this.text = text;
            this.namespace = namespace;
        }
    }
    
    public static class Scenario {
        
        public final int id;
        public final String text;
        public final String namespace;

        public Scenario(int id, String text, String namespace) {
            this.id = id;
            this.text = text;
            this.namespace = namespace;
        }
    }

    public static class Query {

        public final int id;
        public final int studyId;
        public final String text;
        public final String compiled;

        public Query(int id, int studyId, String text, String compiled) {
            this.id = id;
            this.studyId = studyId;
            this.text = text;
            this.compiled = compiled;
        }
    }
    
    public static Persistence.Model loadModel(Connection conn, int id) {

        String text = null;
        String namespace = null;
        
        Statement stmt;
        ResultSet res;

        try {
            stmt = conn.createStatement();
            res = stmt.executeQuery("SELECT * FROM `model` WHERE `id` = " + id);
            if (res.next()) {
                text = res.getString("text");
                namespace = res.getString("namespace");
            }
            res.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println();
        }

        if (text == null) {
            return null;
        }
        
        return new Persistence.Model(id, text, namespace);
    }

    public static ArrayList<Persistence.Model> loadAllModels(Connection conn, int studyId) {

        ArrayList<Persistence.Model> models = new ArrayList<>();
        Statement stmt;
        ResultSet res;

        try {
            stmt = conn.createStatement();
            res = stmt.executeQuery("SELECT * FROM `model` WHERE `studyId` = " + studyId);
            while (res.next()) {
                int id = res.getInt("id");
                String text = res.getString("text");
                String namespace = res.getString("namespace");
                models.add(new Persistence.Model(id, text, namespace));
            }
            res.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return models;
    }
    
    public static Persistence.Scenario loadScenario(Connection conn, int scenarioId) {

        String text = null;
        String namespace = null;
        Statement stmt;
        ResultSet res;

        try {
            stmt = conn.createStatement();
            res = stmt.executeQuery("SELECT * FROM `scenario` WHERE `id` = " + scenarioId);
            while (res.next()) {
                text = res.getString("text");
                namespace = res.getString("namespace");
            }
            res.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println();
        }

        return new Persistence.Scenario(scenarioId, text, namespace);
    }

    public static ArrayList<Persistence.Scenario> loadAllScenarios(Connection conn, int caseId) {

        ArrayList<Persistence.Scenario> scenarios = new ArrayList<>();
        Statement stmt;
        ResultSet res;

        try {
            stmt = conn.createStatement();
            res = stmt.executeQuery("SELECT * FROM `scenario` WHERE `studyId` = " + caseId);
            while (res.next()) {
                int id = res.getInt("id");
                String text = res.getString("text");
                String namespace = res.getString("namespace");
                scenarios.add(new Persistence.Scenario(id, text, namespace));
            }
            res.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return scenarios;
    }
    
    public static Persistence.Query loadQuery(Connection conn, int id) {

        Persistence.Query query = null;

        Statement stmt;
        ResultSet res;

        try {
            stmt = conn.createStatement();
            res = stmt.executeQuery("SELECT * FROM `query` WHERE `id` = " + id);
            while (res.next()) {
                int studyId = res.getInt("studyId");
                String text = res.getString("text");
                String compiled = res.getString("compiled");
                query = new Persistence.Query(id, studyId, text, compiled);
            }
            res.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return query;
    }
}
