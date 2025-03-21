package backendservices;

import com.aventstack.extentreports.Status;
import automation.web.setup.BaseTest;
import automation.web.webutils.Utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;
import java.util.ArrayList;

import static com.aventstack.extentreports.Status.FAIL;
public class OracleDBConnection extends BaseTest {

    private static Connection connection = null;
    public Connection openConnection()  {
        if (connection == null) {
            try {
                String url = "jdbc:oracle:thin:@" + generalCofigsProps.getProperty("dbURL") + ":" + generalCofigsProps.getProperty("port") + "/IRCSTG";
                Class.forName("oracle.jdbc.OracleDriver");
                connection = DriverManager.getConnection(url, generalCofigsProps.getProperty("dbusername"), generalCofigsProps.getProperty("dbpassword"));
                logStatus("Connected to Oracle",Status.PASS,null);
            } catch (Exception e) {
                logStatus("failed to connect Oracle", Status.FAIL, e);
            }
        }
        return connection;
    }


    public ArrayList<ArrayList<Object>> executeQuery(Connection conn, String sqlQuery) throws SQLException {
        ArrayList<ArrayList<Object>> queryResults = new ArrayList<ArrayList<Object>>();
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sqlQuery);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                ArrayList<Object> row = new ArrayList<Object>();
                for (int i = 1; i < columnsNumber + 1; i++) {
                    Object columnValue = rs.getObject(i);
                    row.add(columnValue);
                }
                queryResults.add(row);
            }
            logStatus("Query: "+ sqlQuery +"executed successfully", Status.PASS, null); // Log successful execution
        } catch (SQLException e) {
            logStatus("SQL Error during query execution: " + e.getMessage(), Status.FAIL, e); // Log SQL exception
            throw e; // Re-throw the exception
        }
        return queryResults;
    }


    public ResultSet executeQueryAndGetRS(Connection conn, String sqlQuery) throws SQLException {
        try (Statement statement = conn.createStatement()) {
            return statement.executeQuery(sqlQuery);
        } catch (SQLException e) {
            logStatus("SQL Error during query execution: " + e.getMessage(), Status.FAIL, e);
            throw e;
        }
    }

    public int executeQueryUpdate(Connection conn, String sqlQuery) throws SQLException {
        try (Statement statement = conn.createStatement()) {
            int rowsAffected = statement.executeUpdate(sqlQuery);
            logStatus("SQL Update executed successfully. Rows affected: " + rowsAffected, Status.PASS, null);
            return rowsAffected;
        } catch (SQLException e) {
            logStatus("SQL Error during update execution: " + e.getMessage(), Status.FAIL, e);
            throw e;
        }
    }

    public void execute(Connection conn, String sqlQuery) throws SQLException {
        try (Statement statement = conn.createStatement()) {
            statement.execute(sqlQuery);
        } catch (SQLException e) {
            logStatus("SQL Error during command execution: " + e.getMessage(), Status.FAIL, e);
            throw e;
        }
    }

    public void closeDBConnection(Connection con) throws SQLException {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                logStatus("Database connection closed successfully", Status.PASS, null);
            }
        } catch (SQLException e) {
            logStatus("Error closing database connection: " + e.getMessage(), Status.FAIL, e);
            throw e;
        }
    }


//    public void logStatus(String message, Status status, Exception ex) {
//        Utils utils = new Utils();
//        if (reportingType.equalsIgnoreCase("testng")) {
//            switch (status) {
//                case PASS:
//                    log.info(message);
//                    logger.log(status, message);
//                    break;
//                case FAIL:
//                    log.error(message, ex.getMessage());
//                    logger.log(FAIL, message + ex.getMessage());
//                    ex.printStackTrace();
//                    break;
//                case INFO:
//                    log.info(message);
//                    logger.log(status, message);
//                    break;
//                default:
//                    log.error("Unexpected log status: {}", status);
//            }
//        } else {
//            switch (status) {
//                case PASS:
//                    currentScenario.log("PASS: " + message);
//                    break;
//
//                case FAIL:
//                    if (ex != null) {
//                        currentScenario.log(message + ": " + ex.getMessage());
//                        // Attach the stack trace or exception message for detailed report
//                        currentScenario.attach(ex.getMessage().getBytes(), "text/plain", "Exception Details");
//                        // Optionally, attach the stack trace in case of an exception
//                        StringWriter sw = new StringWriter();
//                        ex.printStackTrace(new PrintWriter(sw));
//                        currentScenario.attach(sw.toString().getBytes(), "text/plain", "Stack Trace");
//                        utils.takeVisiableAreaSnapshotCucumber(message);
//                        // Mark the scenario as failed (this will propagate the failure in the report)
//                        currentScenario.log("FAILURE: " + message + ": " + ex.getMessage());
//                        // Throw a RuntimeException to explicitly mark the test as failed (if not already failed)
//                        throw new RuntimeException(message + ": " + ex.getMessage());
//
//                    } else {
//                        // In case the exception is null, log an unexpected status message
//                        String unexpectedMessage = "Unexpected failure status: " + status + " for " + message;
//                        currentScenario.log(unexpectedMessage);
//
//                        // Optionally, attach additional details if needed
//                        currentScenario.attach(unexpectedMessage.getBytes(), "text/plain", "Unexpected Status Details");
//
//                        // Take a snapshot in case of an unexpected failure
//                        utils.takeVisiableAreaSnapshotCucumber("Unexpected Status Failure");
//                        throw new RuntimeException(message);
//                    }
//
//                default:
//                    // Handle any unknown status, log it as a warning
//                    String unknownStatusMessage = "Unknown status encountered: " + status;
//                    currentScenario.log(unknownStatusMessage);
//                    currentScenario.attach(unknownStatusMessage.getBytes(), "text/plain", "Unknown Status Details");
//
//                    // Optionally, capture a snapshot for debugging
//                    utils.takeVisiableAreaSnapshotCucumber("Unknown Status Snapshot");
//                    throw new RuntimeException(unknownStatusMessage);
//            }
//        }
//    }


    public static void logStatus(String message, Status status, Exception ex) {
        Utils utils = new Utils();
        if (reportingType.equalsIgnoreCase("extend report")) {
            switch (status) {
                case PASS:
                    log.info(message);
                    logger.log(status, message);
                    break;
                case FAIL:
                    log.error(message, ex.getMessage());
                    logger.log(FAIL, message + ex.getMessage());
                    ex.printStackTrace();
                    break;
                case INFO:
                    log.info(message);
                    logger.log(status, message);
                    break;
                default:
                    log.error("Unexpected log status: {}", status);
            }
        } else {
            switch (status) {
                case PASS:
                    currentScenario.log("PASS: " + message);
                    break;
                case INFO:
                    currentScenario.log("PASS: " + message);
                    break;
                case FAIL:
                    if (ex != null) {
                        currentScenario.log(message + ": " + ex.getMessage());
                        // Attach the exception message for detailed report
                        currentScenario.attach(ex.getMessage().getBytes(), "text/plain", "Exception Details");
                        // Optionally, attach the stack trace in case of an exception
                        StringWriter sw = new StringWriter();
                        ex.printStackTrace(new PrintWriter(sw));
                        currentScenario.attach(sw.toString().getBytes(), "text/plain", "Stack Trace");
                        // Log the failure without taking a screenshot
                        currentScenario.log("FAILURE: " + message + ": " + ex.getMessage());
                        // Throw a RuntimeException to explicitly mark the test as failed
                        throw new RuntimeException(message + ": " + ex.getMessage());

                    } else {
                        // In case the exception is null, log an unexpected status message
                        String unexpectedMessage = "Unexpected failure status: " + status + " for " + message;
                        currentScenario.log(unexpectedMessage);

                        // Optionally, attach additional details if needed
                        currentScenario.attach(unexpectedMessage.getBytes(), "text/plain", "Unexpected Status Details");

                        throw new RuntimeException(message);
                    }

                default:
                    // Handle any unknown status, log it as a warning
                    String unknownStatusMessage = "Unknown status encountered: " + status;
                    currentScenario.log(unknownStatusMessage);
                    currentScenario.attach(unknownStatusMessage.getBytes(), "text/plain", "Unknown Status Details");

                    throw new RuntimeException(unknownStatusMessage);
            }
        }
    }



}
