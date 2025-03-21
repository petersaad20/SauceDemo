package automationFramwork.api.exceptions;

/**
 * <b>Exception:</b> BrowserNotSupportedException<br>
 * <b>Description:</b> Handles failures caused when browser executable driver is not found.<br>
 * <b>Author:</b> Nishad Chayanakhawa(CONID: c000177; n.arun.chayanakhawa@accenture.com)<br>
 * <b>Last Maintained in:</b> v1.0
 *
 */
public class BrowserExecutableNotFoundException extends Exception{
    private static final long serialVersionUID = -4003398282104304646L;

    public BrowserExecutableNotFoundException(String browserName) {
        super("Browser executable '" + browserName + "' was not found." +
                "\nPlease make sure the same is placed at './src/test/resources/Drivers'.");
    }
}
