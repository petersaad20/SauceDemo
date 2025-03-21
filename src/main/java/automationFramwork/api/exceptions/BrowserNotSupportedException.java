package automationFramwork.api.exceptions;

/**
 * <b>Exception:</b> BrowserNotSupportedException<br>
 * <b>Description:</b> Handles failures caused by unsupported browser.<br>
 * <b>Author:</b> Nishad Chayanakhawa(CONID: c000177; n.arun.chayanakhawa@accenture.com)<br>
 * <b>Last Maintained in:</b> v1.0
 *
 */
public class BrowserNotSupportedException extends Exception{
    private static final long serialVersionUID = -3281268744917339112L;
    public BrowserNotSupportedException(String browserName) {
        super("Browser '" + browserName + "' is not supported." +
                "\nCurrently supported browsers are: 'Chrome'" +
                "\nPlease contact Automation team to add support for '" + browserName + "' browser");
    }
}
