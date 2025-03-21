package backendservices.deos;

import backendservices.OracleDBConnection;
import com.aventstack.extentreports.Status;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class VoucherDetailsDEO {

    public void getVoucherStatus() throws SQLException {
        OracleDBConnection conn = new OracleDBConnection();
        try {
            Connection consumerConnection = conn.openConnection();
            String query = "";
            conn.logStatus("Execute query: " + query, Status.INFO, null);
            conn.logStatus("SQL Command executed successfully: " + query, Status.PASS, null);
            ArrayList<ArrayList<Object>> result = conn.executeQuery(consumerConnection, query.toString());
            conn.logStatus("Query result: " + result, Status.PASS, null); // Log successful execution
            conn.closeDBConnection(consumerConnection);
        } catch (SQLException e) {
            conn.logStatus("SQL Exception occurred: " + e.getMessage(), Status.FAIL, e);
        }
    }

}
