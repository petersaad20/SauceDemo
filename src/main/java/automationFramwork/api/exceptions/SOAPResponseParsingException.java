package automationFramwork.api.exceptions;

/**
 * SOAP API Parsing Exception
 * <b>Author:</b> Nishad Chayanakhawa(CONID: c000177; n.arun.chayanakhawa@accenture.com)<br>
 * <b>Last Maintained in:</b> v1.2
 *
 */
public class SOAPResponseParsingException extends Exception{
    private static final long serialVersionUID = -7041984696741922087L;
    public SOAPResponseParsingException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
