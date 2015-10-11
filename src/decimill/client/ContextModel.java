package decimill.client;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author David Stefan
 */
public class ContextModel {

    public static String loadModel(Connection conn, String modelId) {

        String modelText = null;
        Statement stmt;
        ResultSet res;

        try {
            stmt = conn.createStatement();
            res = stmt.executeQuery("SELECT * FROM `model` WHERE `id` = '" + modelId + "'");
            while (res.next()) {
                modelText = res.getString("text");
            }
            res.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println();
        }

        return modelText;
    }
}
