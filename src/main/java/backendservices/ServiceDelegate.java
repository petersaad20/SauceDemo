package backendservices;

import backendservices.services.VoucherDetailsService;

public class ServiceDelegate {

    public int changeStatusOfClient() {
        int status = 0;
        try {
            VoucherDetailsService service = new VoucherDetailsService();
            service.getVoucherStatus();
        } catch (Exception e) {

        }
        return status;
    }
}
