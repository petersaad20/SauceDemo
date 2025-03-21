package util;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static automation.web.webutils.ConfigUtils.getProperty;


public class OracleDBUtil  {
    private static Connection connection = null;

    public static Connection getConnection() throws IOException {
        if (connection == null) {
            String dbURL = getProperty("dbURL");
            String port = getProperty("port");
            String dbUsername = getProperty("dbusername");
            String dbPassword = getProperty("dbpassword");
            try {
                String url = "jdbc:oracle:thin:@" + dbURL + ":" + port + "/IRCSTG";
                String username = dbUsername;
                String password = dbPassword;
                Class.forName("oracle.jdbc.OracleDriver");
                System.out.println(dbURL);
                connection = DriverManager.getConnection(url, username, password);
            } catch (Exception e) {
//                logStatus("failed to connect Oracle", Status.FAIL, e);
            }
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
//              logStatus("Oracle connection closed", Status.PASS, null);
            }
        } catch (Exception e) {
//        logStatus("failed to close Oracle", Status.PASS, e);
        }
    }

    public static ResultSet executeQuery(String query) {
        ResultSet resultSet = null;
        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
//          logStatus("Execute this query: " + query, Status.PASS, null);
        } catch (Exception e) {
//          logStatus("failed to execute this query: " + query, Status.PASS, e);
        }
        return resultSet;
    }

}
