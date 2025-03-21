package exceptions;

public class InvalidRESTMethod extends Exception{
    private static final long serialVersionUID = 7864495898282997657L;
    public InvalidRESTMethod(String methodName) {
        super("REST API Method '" + methodName + "' is either invalid or not supported.\n" +
                "Currently supported methods are - GET, POST, PUT, PATCH, DELETE, OPTIONS.\n" +
                "To add support, please contact Automation Team");
    }
}
