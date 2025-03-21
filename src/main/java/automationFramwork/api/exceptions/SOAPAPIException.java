package automationFramwork.api.exceptions;

/**
 * SOAP API Exception
 * <b>Author:</b> Nishad Chayanakhawa(CONID: c000177; n.arun.chayanakhawa@accenture.com)<br>
 * <b>Last Maintained in:</b> v1.2
 *
 */
public class SOAPAPIException extends Exception{
    private static final long serialVersionUID = -7280349297651335174L;
    public SOAPAPIException(String message) {
        super("SOAP request failed.\n" + message);
    }
}
