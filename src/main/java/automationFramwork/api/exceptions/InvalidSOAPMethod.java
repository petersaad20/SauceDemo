package automationFramwork.api.exceptions;

/**
 * Invalid SOAP Method
 * <b>Author:</b> Nishad Chayanakhawa(CONID: c000177; n.arun.chayanakhawa@accenture.com)<br>
 * <b>Last Maintained in:</b> v1.2
 */
public class InvalidSOAPMethod extends Exception{
    private static final long serialVersionUID = -8387259225834485529L;
    public InvalidSOAPMethod(String methodName) {
        super("SPAO API Method '" + methodName + "' is either invalid or not supported.\n" +
                "Currently supported methods are - GET, POST, PUT, PATCH, DELETE.\n" +
                "To add support, please contact Automation Team");
    }
}
