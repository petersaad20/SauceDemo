package setupWeb;


import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
public class setupWebBrowser {

    @Test
    @Parameters({"BROWSER","URL"})
    public void setmethod(String browser, String URL)
    {
        System.setProperty("BROWSER",browser);
        System.setProperty("URL",URL);
    }

}
