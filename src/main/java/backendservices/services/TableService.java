package backendservices.services;

import backendservices.deos.VoucherDetailsDEO;
import com.aventstack.extentreports.Status;

import java.sql.SQLException;

import static backendservices.OracleDBConnection.logStatus;

public class VoucherDetailsService {
    public void getVoucherStatus() throws SQLException {
        VoucherDetailsDEO vo = new VoucherDetailsDEO();
        vo.getVoucherStatus();
    }




}
