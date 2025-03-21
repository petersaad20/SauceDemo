package automation.web.common;

import com.aventstack.extentreports.Status;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.*;
import automation.web.setup.BaseTest;
import automation.web.webutils.Utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.PASS;
import static util.StaticVaraibles.*;

public class CommonFunctions extends BaseTest {

        public void navigateToURL(String URL) {
            try {
                driver.get(URL);
                if (Objects.requireNonNull(driver.getTitle()).isEmpty()) {
                    System.out.println("hetet");
                    ((HasAuthentication) driver).register(UsernameAndPassword.of("admin", "admin"));
                    driver.get(URL);
                }
            } catch (Exception e) {
                ((HasAuthentication) driver).register(UsernameAndPassword.of("admin", "admin"));
                // Retry the navigation after authentication
                driver.get(URL);

            }

            // Maximize window after navigating
            driver.manage().window().maximize();
            logStatus("Navigated to URL: " + URL, PASS, null);
        }

        public void checkURL(String URL)   {
        verifyTextEquality(driver.getCurrentUrl(), URL);
    }

    public void verifyAllElementsClickable(By parentLocator,int elementSize, String attribute)   {
        try {
            WebElement e = waitElementForMultipleConditions(
                    parentLocator, timeoutInSeconds, pollingIntervalInSeconds,
                    ExpectedConditions.elementToBeClickable(parentLocator));

            List<WebElement> elements = driver.findElements(parentLocator);
            if (elements.size() == elementSize) {
                logStatus("Found exactly 4 elements inside the container.", PASS, null);

                for (WebElement element : elements) {
                    WebElement childElement = waitElementForMultipleConditions(
                            parentLocator, timeoutInSeconds, pollingIntervalInSeconds,
                            ExpectedConditions.elementToBeClickable(element));

                    if (childElement != null && childElement.isEnabled() && childElement.isDisplayed()) {
                        logStatus("Element with text: " + e.getText() + " is displayed.", PASS, null);
                    } else {
                        logStatus("Element with text: " + e.getText() + " is NOT displayed.", FAIL, null);
                    }
                }
            } else {
                logStatus("Expected 4 elements, but found " + elements.size() + " elements.", FAIL, null);
            }
        } catch (Exception ex) {
            handleException("Error while verifying the elements inside the container.", ex);
        }
    }


    public WebElement waitElementForMultipleConditions(By by, long timeoutInSeconds, long pollingIntervalInSeconds, ExpectedCondition<?>... conditions) {
        Wait<WebDriver> fluentWait = getFluentWait(timeoutInSeconds, pollingIntervalInSeconds);
        WebElement element = null;
        for (ExpectedCondition<?> condition : conditions) {
            if (condition instanceof ExpectedCondition) {
                element = (WebElement) fluentWait.until(condition);
            } else {
                fluentWait.until(condition); // Wait for non-element conditions
            }
        }
        return element;
    }

    public static Wait<WebDriver> getFluentWait(long timeoutInSeconds, long pollingIntervalInSeconds) {
        return new FluentWait<>(driver).withTimeout(Duration.ofSeconds(timeoutInSeconds)).pollingEvery(Duration.ofSeconds(pollingIntervalInSeconds)).ignoring(StaleElementReferenceException.class, NoSuchElementException.class).ignoring(TimeoutException.class, ElementNotInteractableException.class).ignoring(NoSuchFrameException.class, NoSuchWindowException.class).ignoring(WebDriverException.class, ElementClickInterceptedException.class).ignoring(InvalidSelectorException.class, MoveTargetOutOfBoundsException.class).ignoring(JavascriptException.class, UnhandledAlertException.class);
    }

    public void handleException(String attribute, Exception ex) {
        if (ex instanceof NoSuchElementException || ex instanceof StaleElementReferenceException) {
            logStatus("Element " + attribute + " not found or is stale", FAIL, ex);
        } else if (ex instanceof TimeoutException) {
            logStatus("Timeout waiting for element " + attribute + " to be visible", FAIL, ex);
        } else {
            logStatus("Unexpected exception for this Element " + attribute + "", FAIL, ex);
        }
    }

    public void logStatus(String message, Status status, Exception ex) {
        Utils utils = new Utils();
        if (reportingType.equalsIgnoreCase("extend report")) {
            switch (status) {
                case PASS, INFO:
                    log.info(message);
                    logger.log(status, message);
                    break;
                case FAIL:
                    log.error(message, ex.getMessage());
                    logger.log(FAIL, message + ex.getMessage());
                    ex.printStackTrace();
                    break;
                default:
                    log.error("Unexpected log status: {}", status);
            }
        } else {
            switch (status) {
                case PASS:
                    currentScenario.log("PASS: " + message);
                    utils.takeVisiableAreaSnapshotCucumber(message);
                    break;
                case INFO:
                    currentScenario.log("Info: " + message);
                    utils.takeVisiableAreaSnapshotCucumber(message);
                    break;
                case FAIL:
                    // Check if an exception was provided
                    if (ex != null) {
                       currentScenario.log(message + ": " + ex.getMessage());
                        // Attach the stack trace or exception message for detailed report
                        currentScenario.attach(ex.getMessage().getBytes(), "text/plain", "Exception Details");
                        // Optionally, attach the stack trace in case of an exception
                        StringWriter sw = new StringWriter();
                        ex.printStackTrace(new PrintWriter(sw));
                        currentScenario.attach(sw.toString().getBytes(), "text/plain", "Stack Trace");
                        utils.takeVisiableAreaSnapshotCucumber(message);
                        // Mark the scenario as failed (this will propagate the failure in the report)
                        currentScenario.log("FAILURE: " + message + ": " + ex.getMessage());
                        // Throw a RuntimeException to explicitly mark the test as failed (if not already failed)
                        throw new RuntimeException(message + ": " + ex.getMessage());

                    } else {
                        // In case the exception is null, log an unexpected status message
                        String unexpectedMessage = "Unexpected failure status: " + status + " for " + message;
                       currentScenario.log(unexpectedMessage);

                        // Optionally, attach additional details if needed
                      currentScenario.attach(unexpectedMessage.getBytes(), "text/plain", "Unexpected Status Details");

                        // Take a snapshot in case of an unexpected failure
                        utils.takeVisiableAreaSnapshotCucumber("Unexpected Status Failure");
                        throw new RuntimeException(message);
                    }

                default:
                    // Handle any unknown status, log it as a warning
                    String unknownStatusMessage = "Unknown status encountered: " + status;
                    currentScenario.log(unknownStatusMessage);
                    currentScenario.attach(unknownStatusMessage.getBytes(), "text/plain", "Unknown Status Details");

                    // Optionally, capture a snapshot for debugging
                    utils.takeVisiableAreaSnapshotCucumber("Unknown Status Snapshot");
                    throw new RuntimeException(unknownStatusMessage);
            }
        }
    }

    private void handleVisibilityStatus(WebElement e, String attribute)   {
        if (e.isDisplayed()) {
            logStatus("Element " + attribute + " is Displayed", PASS, null);
        } else {
            logStatus("Element " + attribute + " isn't Displayed", FAIL, null);
        }
    }

    public boolean isElementDisplayed(By by, String attribute)   {
        try {
            WebElement e = waitElementForMultipleConditions(by, timeoutInSeconds, pollingIntervalInSeconds, ExpectedConditions.visibilityOfElementLocated(by));
            handleVisibilityStatus(e, attribute);
            return true;
        } catch (NoSuchElementException | StaleElementReferenceException ex) {
            handleException(attribute, ex);
        }
        return false;
    }

    public void checkIfImageIsBroken(By by, String imageAttribute) {
        WebElement image = waitElementForMultipleConditions(by, timeoutInSeconds, pollingIntervalInSeconds, ExpectedConditions.visibilityOfElementLocated(by));  // Replace with appropriate method to wait for image element

        try {
            if (image != null && image.isDisplayed() && image.isEnabled()) {
                String imageUrl = image.getAttribute("src");
                if (isImageBroken(imageUrl)) {
                    logStatus("Image " + imageAttribute + " is broken", FAIL, null);
                } else {
                    logStatus("Image " + imageAttribute + " is not broken", PASS, null);
                }
            } else {
                logStatus("Image " + imageAttribute + " is not visible or enabled", FAIL, null);
            }
        } catch (Exception ex) {
            handleException(imageAttribute, ex);
        }
    }

    public boolean isImageBroken(String imageUrl) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(imageUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            return responseCode != 200; // If response code is not 200, the image is broken
        } catch (Exception e) {
            return true; // If any exception occurs, the image is considered broken
        }
    }

    public void selectDropdownOptionByValue(By by, String value, String attribute)   {
        WebElement e = waitElementForMultipleConditions(by, timeoutInSeconds, pollingIntervalInSeconds, ExpectedConditions.visibilityOfElementLocated(by), ExpectedConditions.elementToBeClickable(by));
        try {
            if (e != null && e.isEnabled()) {
                Select sel = new Select(e);
                // Check if the value exists in the dropdown options
                List<WebElement> options = sel.getOptions();
                boolean valueFound = options.stream().anyMatch(option -> option.getAttribute("value").equals(value));
                if (valueFound) {
                    sel.selectByValue(value);
                    logStatus("Successfully selected value: " + value + "", PASS, null);
                } else {
                    logStatus("Valuenot found in the dropdown." + value + "", FAIL, null);
                }
            } else {
                logStatus("Dropdown element is either not displayed or not enabled.", FAIL, null);
            }
        } catch (NoSuchElementException | StaleElementReferenceException ex) {
            handleException(attribute, ex);
        }
    }

    public List<Double> captureElementsAfterSorting(By by) {
        List<Double> prices = new ArrayList<>();
        try {
            commonFunctions.waitElementForMultipleConditions(by, timeoutInSeconds, pollingIntervalInSeconds,
                    ExpectedConditions.visibilityOfElementLocated(by));

            List<WebElement> products = driver.findElements(by);
            for (WebElement product : products) {
                String priceText = product.getText();
                logStatus("Captured price: " + priceText, Status.INFO, null);
                prices.add(parsePrice(priceText));
            }
        } catch (Exception ex) {
            handleException("Failed to capture product prices before sorting", ex);
        }
        return prices;
    }

    public boolean validateSorting( List<Double> pricesAfter) {
        boolean isSorted = true;
        try {
            for (int i = 0; i < pricesAfter.size() - 1; i++) {
                if (pricesAfter.get(i) > pricesAfter.get(i+1)) {
                    isSorted = false;
                    break;
                }
            }
            if (isSorted) {
                logStatus("Products are sorted correctly from low to high.", PASS, null);
            } else {
                logStatus("Sorting failed. Products are not sorted correctly.", FAIL, null);
            }
        } catch (Exception ex) {
            handleException("Failed to validate sorting", ex);
        }
        return isSorted;
    }

    private Double parsePrice(String priceText) {
        try {
            return Double.parseDouble(priceText.replace("$", "").trim());
        } catch (NumberFormatException ex) {
            logStatus("Failed to parse price: " + priceText, FAIL, ex);
            return 0.0;
        }
    }

    public List<String> getOptionsTextFromDropdown(By by, String value, String attribute)   {
        List<String> opt = null;
        try {
            // Wait until the element is clickable (interactable)
            WebElement e = waitElementForMultipleConditions(by, timeoutInSeconds, pollingIntervalInSeconds, ExpectedConditions.visibilityOfElementLocated(by), ExpectedConditions.elementToBeClickable(by));
            if (e != null && e.isEnabled()) {
                Select sel = new Select(e);
                List<WebElement> options = sel.getOptions();
                boolean valueFound = options.stream().anyMatch(option -> option.getAttribute("value").equals(value));
                if (valueFound) {
                    opt = sel.getOptions().stream().map(WebElement::getText).collect(Collectors.toList());
                    logStatus("Successfully retrieved all values from the dropdown: " + value, PASS, null);

                } else {
                    logStatus("failed to  get all value from DDL: +value+", FAIL, null);

                }
            } else {
                logStatus("Dropdown element is either not displayed or not enabled.", FAIL, null);
            }
        } catch (NoSuchElementException | StaleElementReferenceException ex) {
            handleException(attribute, ex);
        }
        return opt;
    }


    ///////////////////////////////mouseactions///////////////////////////////

    public void mouseHover(By by, String value)   {
        try {
            WebElement elementToHover = waitElementForMultipleConditions(by, timeoutInSeconds, pollingIntervalInSeconds, ExpectedConditions.visibilityOfElementLocated(by), ExpectedConditions.elementToBeClickable(by));
            if (elementToHover != null && elementToHover.isEnabled()) {
                action = new Actions(driver);
                action.moveToElement(elementToHover).build().perform();
                logStatus("Mouse hover performed on element: " + value, PASS, null);
            } else {
                logStatus("Element not visible or not interactable for hover: " + value, FAIL, null);
            }
        } catch (Exception ex) {
            handleException(value, ex);
        }
    }



    ///////////////////////////////Actions///////////////////////////////

    public void clickOnElement(By by, String attribute)   {
        WebElement e = waitElementForMultipleConditions(by, timeoutInSeconds, pollingIntervalInSeconds, ExpectedConditions.visibilityOfElementLocated(by), ExpectedConditions.elementToBeClickable(by));
        try {
            if (e != null && e.isEnabled()) {
                e.click();
                logStatus("Element " + attribute + " is clickable", PASS, null);
            } else {
                logStatus("Element " + attribute + " isn't  clickable", FAIL, null);
            }
        } catch (Exception ex) {
            handleException(attribute, ex);
        }
    }

    public boolean checkPageLoadTime(int time) {
            boolean flag=true;
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Long loadTime = (Long) js.executeScript(
                "return window.performance.timing.loadEventEnd - window.performance.timing.navigationStart;");
            if (loadTime > time) {
                System.out.println("herererere");
                flag=false;
                logStatus("Page load took too long: " + loadTime + "ms", FAIL, null);
            } else {
                logStatus("Page loaded successfully in " + loadTime + "ms", PASS, null);
            }
           return flag;
    }

    public void clickOnElementBYListOfLocators(By... submitButtons) {
        for (By button : submitButtons) {
            WebElement e = null;
            try {
                // Wait for the element to be clickable (explicit wait for each element)
                e = waitElementForMultipleConditions(button, timeoutInSeconds, pollingIntervalInSeconds, ExpectedConditions.elementToBeClickable(button));

                // If the element is found and is displayed/enabled, click it
                if (e != null && e.isDisplayed() && e.isEnabled()) {
                    e.click();
                    return; // Exit the method after clicking the first clickable element
                }
            } catch (Exception ex) {
                // Ignore any exception (don't log it, just try the next locator)
                continue;
            }
        }
        // Exit silently if no element was clicked
    }

    public void isSelected(By by, String attribute)   {
        WebElement e = waitElementForMultipleConditions(by, timeoutInSeconds, pollingIntervalInSeconds, ExpectedConditions.visibilityOfElementLocated(by), ExpectedConditions.elementToBeClickable(by));
        try {
            if (e != null && e.isSelected()) {
                logStatus("Element " + attribute + " is selected", PASS, null);
            } else {
                logStatus("Element " + attribute + " isn't  selected", FAIL, null);
            }
        } catch (Exception ex) {
            handleException(attribute, ex);
        }
    }

    public void isDisplayed(By by, String attribute)   {
        WebElement e = waitElementForMultipleConditions(by, timeoutInSeconds, pollingIntervalInSeconds, ExpectedConditions.visibilityOfElementLocated(by), ExpectedConditions.elementToBeClickable(by));
        try {
            if (e != null && e.isDisplayed()) {
                logStatus("Element " + attribute + " is displayed", PASS, null);
            } else {
                logStatus("Element " + attribute + " isn't  displayed", FAIL, null);
            }
        } catch (Exception ex) {
            handleException(attribute, ex);
        }
    }

    public void clickOnElement(WebElement e, String attribute)   {
        try {
            if (e != null && e.isEnabled()) {
                e.click();
                logStatus("Element " + attribute + " is clickable", PASS, null);
            } else {
                logStatus("Element " + attribute + " isn't  clickable", FAIL, null);
            }
        } catch (Exception ex) {
            handleException(attribute, ex);
        }
    }

    public void sendText(By inputLocator, String value, String attribute)  {
        try {
            WebElement inputField = waitElementForMultipleConditions(inputLocator, timeoutInSeconds, pollingIntervalInSeconds, ExpectedConditions.visibilityOfElementLocated(inputLocator), ExpectedConditions.elementToBeClickable(inputLocator));
            if (inputField != null && inputField.isDisplayed() && inputField.isEnabled()) {
                inputField.clear();
                inputField.sendKeys(value);
                logStatus("The field '" + attribute + "' was filled successfully with value: ", PASS, null);
            } else {
                logStatus("The field '" + attribute + "' is either not visible or not enabled for input", PASS, null);
            }
        } catch (Exception ex) {
            handleException(attribute, ex);
        }
    }


    public String getText(By by, String attribute)   {
        try {
            WebElement e = waitElementForMultipleConditions(by, timeoutInSeconds, pollingIntervalInSeconds,
                    ExpectedConditions.visibilityOfElementLocated(by),
                    ExpectedConditions.elementToBeClickable(by));
            if (e != null && e.isDisplayed() && e.isEnabled()) {
                String elementText = e.getText().trim();
                if (!elementText.isEmpty()) {
                    logStatus("Text from element '" + e.getText() + "' retrieved successfully: " + elementText, PASS, null);
                    return elementText;
                } else {
                    logStatus("Text from element is not retrieved successfully: " + elementText, PASS, null);
                    return null;
                }
            } else {
                logStatus("The element '" + e.getText() + "' is not displayed or enabled.", FAIL, null);
                return null;
            }
        } catch (Exception ex) {
            handleException(attribute, ex);
            return null;
        }
    }

    public void verifyTextEquality(String actualText, String expectedText)
    {
        try {
            System.out.println("current URL"+actualText);
            if (actualText.isEmpty() || expectedText.isEmpty()) {
                logStatus("actualText or  expectedText is null", FAIL, null);
            } else if (actualText.equals(expectedText)) {
                logStatus("Actual Text: " + actualText + " is equal to " + "Expected Text: " + expectedText, PASS, null);
            }
            else{
                logStatus("Actual Text: " + actualText + " isn't equal to " + "Expected Text: " + expectedText, FAIL, null);
            }
        } catch (Exception ex) {
            handleException("Actual Text: " + actualText + " isn't  equal to " + "Expected Text: " + expectedText, ex);
        }
    }
    public void verifyDoubleEquality(double actualDouble, double expectedDouble)   {
        try {
              if (actualDouble==expectedDouble) {
                logStatus("Actual Double: " + actualDouble + " is equal to " + "Expected Double: " + expectedDouble, PASS, null);
            }
            else{
                logStatus("Actual Text: " + actualDouble + " isn't equal to " + "Expected Text: " + expectedDouble, FAIL, null);
            }
        } catch (Exception ex) {
            handleException("Actual Text: " + actualDouble + " isn't  equal to " + "Expected Text: " + expectedDouble, ex);
        }
    }


    public void verifyTextContain(String actualText, String expectedText)   {
        try {
            if (actualText == null || expectedText == null) {
                logStatus(String.format("Null value detected: actualText='%s', expectedText='%s'", actualText, expectedText), FAIL, null);
            } else if (actualText.contains(expectedText)) {
                logStatus("Actual Text: " + actualText + " is equal to " + "Expected Text: " + expectedText, PASS, null);
            }
        } catch (Exception ex) {
            handleException("Actual Text: " + actualText + " isn't  equal to " + "Expected Text: " + expectedText, ex);
        }
    }

    public void verifyElementsClickable(By... locators)   {
        try {
            if (locators == null || locators.length == 0) {
                logStatus("No locators provided to check", FAIL, null);
                return;
            }

            // Loop through each locator
            for (By locator : locators) {
                // Find all elements for this locator
                List<WebElement> elements = driver.findElements(locator);

                // If no elements are found, log it as a failure
                if (elements.isEmpty()) {
                    logStatus("No elements found for locator: " + locator, FAIL, null);
                } else {
                    // Verify each element
                    for (WebElement element : elements) {
                        if (element != null && element.isDisplayed() && element.isEnabled()) {
                            logStatus("Element found and is clickable: " + element.toString(), PASS, null);
                        } else {
                            logStatus("Element is NOT clickable: " + element.toString(), FAIL, null);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            handleException("Error verifying clickable elements", ex);
        }
    }

    public String getPageTitle() {

        try {
            System.out.println(driver.getTitle() + "Radi");
            return driver.getTitle();

        } catch (Exception e1) {
            logger.log(FAIL, "Can't get Page Title due to Exception");
            e1.printStackTrace();
            return null;
        }
    }

    public String getTooltipText(By by, String attribute)   {
        String tooltipText = null;
        try {
            WebElement e = waitElementForMultipleConditions(by, timeoutInSeconds, pollingIntervalInSeconds, ExpectedConditions.visibilityOfElementLocated(by), ExpectedConditions.elementToBeClickable(by));

            if (e != null && e.isDisplayed()) {
                tooltipText = e.getAttribute("title");  // Tooltips are typically stored in the "title" attribute

                if (tooltipText != null && !tooltipText.isEmpty()) {
                    logger.log(PASS, "Successfully retrieved tooltip text for element '" + attribute + "': " + tooltipText);
                    return tooltipText.trim();
                } else {
                    logger.log(Status.WARNING, "Tooltip text for element '" + attribute + "' is empty or not available.");
                }
            } else {
                logger.log(FAIL, "Element '" + attribute + "' is not displayed or is not clickable.");
            }
        } catch (Exception ex) {
            handleException(attribute, ex);
        }

        return null;
    }

    private void waitForPageLoaded(WebDriver driver, String pageDescription)   {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            wait.until(webDriver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
            logStatus("Page '" + pageDescription + "' has loaded successfully.", PASS, null);
        } catch (TimeoutException e) {
            logStatus("Timeout occurred while waiting for the page  '" + pageDescription + "' to load.", FAIL, e);
        } catch (WebDriverException e) {
            logStatus("WebDriver error while waiting for page '" + pageDescription + "' to load.", FAIL, e);
            e.printStackTrace();
        } catch (Exception e) {
            logStatus("An unexpected error occurred while waiting for page '" + pageDescription + "' to load.", FAIL, e);
            e.printStackTrace();
        }
    }

    /////////////////////////////////////JS Execution//////////////////////////////////

    public void scrollToElement(By by, String attribute)   {
        WebElement element = waitElementForMultipleConditions(by, timeoutInSeconds, pollingIntervalInSeconds, ExpectedConditions.visibilityOfElementLocated(by));
        try {
            if (element != null && element.isDisplayed()) {
                if (driver instanceof JavascriptExecutor) {
                    // If driver supports JavascriptExecutor, use JavaScript to scroll to the element
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    js.executeScript("arguments[0].scrollIntoView(true);", element);
                } else {
                    // Fallback to Actions for scrolling if JavascriptExecutor is not available
                    action = new Actions(driver);
                    action.moveToElement(element).perform();
                }
                logStatus("Successfully scrolled to element: " + attribute, PASS, null);
            } else {
                logStatus("Element " + attribute + " isn't visible or displayed", FAIL, null);
            }
        } catch (Exception e) {
            handleException(attribute, e);
        }
    }

    public void scrollToTop(String attribute)   {
        try {
            if (driver instanceof JavascriptExecutor) {
                // If driver supports JavascriptExecutor, use JavaScript to scroll to the top
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("window.scrollTo(0, 0);");
                logStatus("Successfully scrolled to the top using JavaScript: " + attribute, PASS, null);
            } else {
                // Fallback method: use Actions or SendKeys if JavascriptExecutor is not available
                if (driver instanceof WebDriver) {
                    action = new Actions(driver);
                    action.sendKeys(Keys.HOME).perform();
                    logStatus("Successfully scrolled to the top using Actions/SendKeys: " + attribute, PASS, null);
                } else {
                    Actions actions = new Actions(driver);
                    actions.sendKeys(Keys.PAGE_UP).perform();  // Try PAGE_UP key to scroll up
                    logStatus("Successfully scrolled up using Actions/SendKeys (PAGE_UP): " + attribute, PASS, null);
                    logStatus("Neither JavascriptExecutor nor Actions/SendKeys are supported for scrolling to the top: " + attribute, FAIL, null);
                }
                logStatus("Neither JavascriptExecutor nor Actions/SendKeys are supported for scrolling to the top: " + attribute, FAIL, null);
            }
        } catch (Exception e) {
            handleException(attribute, e);
        }
    }

    public void scrollToBottom(String attribute)   {
        try {
            if (driver instanceof JavascriptExecutor) {
                // If driver supports JavascriptExecutor, use JavaScript to scroll to the bottom
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
                logStatus("Successfully scrolled to the bottom using JavaScript: " + attribute, PASS, null);
            } else {
                // Fallback method: use Actions or SendKeys if JavascriptExecutor is not available
                if (driver instanceof WebDriver) {
                    action = new Actions(driver);
                    action.sendKeys(Keys.END).perform(); // Using the END key to scroll down
                    logStatus("Successfully scrolled to the bottom using Actions/SendKeys (END): " + attribute, PASS, null);
                } else {
                    // In case neither JavascriptExecutor nor Actions/SendKeys are supported
                    logStatus("Neither JavascriptExecutor nor Actions/SendKeys are supported for scrolling to the bottom: " + attribute, FAIL, null);
                }
            }
        } catch (Exception e) {
            handleException(attribute, e);
        }
    }

    public void clickUsingJS(By by, String attribute)   {
        WebElement element = waitElementForMultipleConditions(by, timeoutInSeconds, pollingIntervalInSeconds, ExpectedConditions.elementToBeClickable(by));
        try {
            if (element != null && element.isEnabled()) {
                if (driver instanceof JavascriptExecutor) {
                    // If driver supports JavascriptExecutor, use JavaScript to click the element
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    js.executeScript("arguments[0].click();", element);  // JavaScript click
                    logStatus("Element " + attribute + " clicked using JavaScript", PASS, null);
                } else {
                    logStatus("JavascriptExecutor is not available for clicking the element: " + attribute, FAIL, null);
                }
            } else {
                logStatus("Element " + attribute + " isn't clickable", FAIL, null);
            }
        } catch (Exception e) {
            handleException(attribute, e);
        }
    }

    public String getJavaScriptVariable(String script, String attribute)   {
        try {
            if (driver instanceof JavascriptExecutor) {
                // If driver supports JavascriptExecutor, execute the JavaScript to get the variable
                JavascriptExecutor js = (JavascriptExecutor) driver;
                String result = (String) js.executeScript(script);
                logStatus("Successfully retrieved JavaScript variable: " + attribute, PASS, null);
                return result;
            } else {
                logStatus("JavascriptExecutor is not available to retrieve JavaScript variable: " + attribute, FAIL, null);
                return null;
            }
        } catch (Exception e) {
            handleException(attribute, e);
            return null;
        }
    }


    /////////////////////////////////////Radio button//////////////////////////////////
    public void checkIfElementIsCheked(WebElement e, String attributeName, boolean flag) {
        try {
            if (e.isSelected() == flag) {
                logger.log(PASS, "Element " + attributeName + " is highlighted = " + e.isSelected());
            } else {
                logger.log(FAIL, "Element " + attributeName + " is highlighted = " + e.isSelected());
            }
        } catch (Exception e1) {
            logger.log(FAIL, "Element " + attributeName + " didn't selected successfullssssy");
            e1.printStackTrace();
        }
    }

    ///////////////////////////////////// //////////////////////////////////
    public void navigateToURLWithVariousScreen(String URL,String device,int width ,int height)   {
        driver.get(URL);
        testOnScreenSize(width, height, device);
        logStatus("Navigated to URL: " + URL, PASS, null);
    }

    public void testOnScreenSize(int width, int height, String screenName)   {
        // Set the window size to the specified screen size
        driver.manage().window().setSize(new Dimension(width, height));
        logStatus("Testing on " + screenName + " with resolution: " + width + "x" + height, PASS, null);

    }


}
