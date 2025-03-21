package automationFramwork.api.exceptions;

/**
 * <b>Exception:</b> TestDataHandlingException<br>
 * <b>Description:</b> Handles failures related to Test Data Handling error. Below are the errors
 * handled-<br>
 * Error opening excel at '{workbookPath}' location<br>
 * Error closing excel.<br>
 * <b>Author:</b> Nishad Chayanakhawa(CONID: c000177; n.arun.chayanakhawa@accenture.com)<br>
 * <b>Last Maintained in:</b> v1.0
 *
 */
public class TestDataHandlingException extends Exception{
    private static final long serialVersionUID = -7652608498841711073L;

    public TestDataHandlingException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
