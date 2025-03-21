package automation.web.webutils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Pdf;
import org.openqa.selenium.PrintsPage;
import org.openqa.selenium.TakesScreenshot;
import automation.web.setup.BaseTest;
import org.openqa.selenium.print.PrintOptions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Utils extends BaseTest {
    public Utils()  {
        super();
    }

    public static void takeVisiableAreaSnapshot(String name) {

        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(srcFile, new File(screenshotDictionary + name + ".png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printPdfFile() throws IOException {
        ///////////////////////////////////////////////////////
        Pdf pdf = ((PrintsPage) driver).print(new PrintOptions());
        Files.write(Paths.get("./selenium.pdf"), OutputType.BYTES.convertFromBase64Png(pdf.getContent()));
    }


    public String takeVisiableAreaSnapshotCucumber(String logMessage) {
        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String sanitizedLogMessage = logMessage.replaceAll("[^a-zA-Z0-9]", "_");
            String screenshotPath = screenshotCucumberDictionary + sanitizedLogMessage + "_"  + ".png";
            FileUtils.copyFile(srcFile, new File(screenshotPath));
            byte[] screenshotBytes = Files.readAllBytes(new File(screenshotPath).toPath());
            currentScenario.attach(screenshotBytes, "image/png", logMessage);
        } catch (IOException e) {
            e.printStackTrace();
            String errorMessage = "Screenshot failed: " + e.getMessage();
           currentScenario.attach(errorMessage.getBytes(), "text/plain", "Screenshot Failure");
        }
        return screenshotPath;
    }
    public String takeVisiableAreaSnapshot(String path,String logMessage) {
        String screenPath="";
        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String sanitizedLogMessage = logMessage.replaceAll("[^a-zA-Z0-9]", "_");
            screenPath = path + sanitizedLogMessage + "_"  + ".png";
            FileUtils.copyFile(srcFile, new File(screenPath));
            byte[] screenshotBytes = Files.readAllBytes(new File(screenPath).toPath());
            currentScenario.attach(screenshotBytes, "image/png", logMessage);
        } catch (IOException e) {
            e.printStackTrace();
            String errorMessage = "Screenshot failed: " + e.getMessage();
            currentScenario.attach(errorMessage.getBytes(), "text/plain", "Screenshot Failure");
        }
        return screenPath;
    }
    public static void takeFullPageScreenshot(String name) {
    }

}
