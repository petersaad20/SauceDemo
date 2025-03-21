package automation.web.steps;

import automation.web.setup.BaseTest;
import automation.web.webpages.CheckoutPage;
import com.aventstack.extentreports.Status;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import com.github.javafaker.Faker;

import static automation.web.steps.DashboardSteps.productPrices;

public class CheckOutSteps extends BaseTest {
DashboardSteps dashboardSteps = new DashboardSteps();
    CheckoutPage checkoutPage = new CheckoutPage();
    public String removedProduct;
    public String removedPrice;
    private String randomFirstName;
    private String randomLastName;
    private String randomZipCode;


    @Given("Remove Most Expensive Product")
    public void removeMostExpensiveProduct() {
        try {
            // Find all the cart items
            List<WebElement> cartItems = driver.findElements(checkoutPage.CHECKOUT_CARD_ITEM);
            if (cartItems.isEmpty()) {
                commonFunctions.logStatus("No items found in the cart to remove.", Status.FAIL, null);
                return;
            }
            WebElement mostExpensiveProduct = null;
            double maxPrice = 0;

            // Loop through each cart item to find the most expensive product
            for (WebElement item : cartItems) {
                String priceStr = item.findElement(checkoutPage.CHECKOUT_CARD_ITEM_PRICE).getText();
                double price = Double.parseDouble(priceStr.replace("$", ""));
                if (price > maxPrice) {
                    maxPrice = price;
                    mostExpensiveProduct = item;
                }
            }

            if (mostExpensiveProduct != null) {
                // Click the 'remove from cart' button for the most expensive product
                WebElement removeButton = mostExpensiveProduct.findElement(checkoutPage.CHECKOUT_CARD_ITEM_REMOVE);
                removedProduct = mostExpensiveProduct.findElement(checkoutPage.CHECKOUT_CARD_ITEM_NAME).getText();
                removedPrice = mostExpensiveProduct.findElement(checkoutPage.CHECKOUT_CARD_ITEM_NAME).getText();

                commonFunctions.clickOnElement(removeButton, "Click on Remove Button for most expensive product");
                commonFunctions.logStatus("Most expensive product removed from the cart", Status.PASS, null);
            } else {
                commonFunctions.logStatus("No product found to remove from the cart.", Status.FAIL, null);
            }
            commonFunctions.logStatus("Cart items after removal:", Status.INFO, null);

        } catch (Exception ex) {
            commonFunctions.handleException("Error while removing the most expensive product", ex);
        }
    }

    @Then("Validate the cart After Removing the most expensive product and validate the cart")
    public void validateCartAfterRemovalExpensiveProduct() {
        validateCartAfterRemoval();
    }

    @Given("the user is on the checkout page")
    public void theUserIsOnTheCheckoutPage() {
        commonFunctions.clickOnElement(checkoutPage.CHECKOUT_BUTTON, "Click On CheckOut Button");
        Faker faker = new Faker();
        randomFirstName = faker.name().firstName();
        randomLastName = faker.name().lastName();
        randomZipCode = faker.address().zipCode();
    }

    @When("the user enters incorrect details and submits the form")
    public void theUserEntersIncorrectDetailsAndSubmitsTheForm() {
        commonFunctions.sendText(checkoutPage.CHECKOUT_FIRST_NAME, randomFirstName, "Fill First Name Field");
        commonFunctions.sendText(checkoutPage.CHECKOUT_LAST_NAME, randomLastName, "Fill Last Name Field");
        commonFunctions.clickOnElement(checkoutPage.CHECKOUT_CONTINUE_BUTTON, "Click on Continue Button");
    }

    @Then("the form validation error for the missing ZIP code should be displayed")
    public void theFormValidationErrorForTheMissingZIPCodeShouldBeDisplayed() {
        commonFunctions.verifyTextEquality(commonFunctions.getText(checkoutPage.CHECKOUT_ERROR_BUTTON,
                "Get Error MSG"),"Error: Postal Code is required");
    }

    @When("the user corrects the ZIP code and submits the form again")
    public void theUserCorrectsTheZIPCodeAndSubmitsTheFormAgain() {
        commonFunctions.sendText(checkoutPage.CHECKOUT_FIRST_NAME, randomFirstName, "Fill First Name Field");
        commonFunctions.sendText(checkoutPage.CHECKOUT_FIRST_NAME, randomLastName, "Fill Last Name Field");
        commonFunctions.sendText(checkoutPage.CHECKOUT_ZIP_CODE,randomZipCode,"Fill ZipCode Filed");
        commonFunctions.clickOnElement(checkoutPage.CHECKOUT_CONTINUE_BUTTON, "Click on Continue Button");
    }

    @Then("the user should be redirected to the checkout overview page")
    public void theUserShouldBeRedirectedToTheCheckoutOverviewPage() {
        commonFunctions.checkURL("https://www.saucedemo.com/v1/checkout-step-two.html");
        commonFunctions.isElementDisplayed(checkoutPage.CHECKOUT_REVIEW_HEADER, "Check If Products Element is displayed");
    }

    @And("the user should see the correct total price including tax")
    public void theUserShouldSeeTheCorrectTotalPriceIncludingTax() {
        double subTotalPrice=0.0;
        for(double number : productPrices){
            subTotalPrice+=number;
        }
        double price = Double.parseDouble( commonFunctions.getText(checkoutPage.CHECKOUT_SUBTOTAL_AMOUNT,"Get Sub Total Price")
                .replace("Item total: $", ""));
        double invoice = Double.parseDouble( commonFunctions.getText(checkoutPage.CHECKOUT_TAX_AMOUNT,"Get Invoice Price")
                .replace("Tax: $", ""));
        double totalPrice = Double.parseDouble( commonFunctions.getText(checkoutPage.CHECKOUT_TOTAL_AMOUNT,"Get Total Price")
                .replace("Total: $", ""));

        double taxInvoice = Math.round(price * 0.08 * 100) / 100.0;
        commonFunctions.verifyDoubleEquality(price,subTotalPrice);
        commonFunctions.verifyDoubleEquality(invoice,taxInvoice);
        commonFunctions.verifyDoubleEquality(price+invoice,totalPrice);
    }

    @When("the user confirms the order")
    public void theUserConfirmsTheOrder() {
        commonFunctions.clickOnElement(checkoutPage.CHECKOUT_FINISH,"Click on Finish Button");
    }
    @Then("the order confirmation message should be displayed")
    public void theOrderConfirmationMessageShouldBeDisplayed() {
        commonFunctions.checkURL("https://www.saucedemo.com/v1/checkout-complete.html");
        commonFunctions.isElementDisplayed(checkoutPage.CHECKOUT_SUCESS_SCREEN, "Check If Success Msg is displayed");


    }

    public void validateCartAfterRemoval() {
        List<String> cartItemNamesAfter = captureCartItemNames();
        List<String> cartItemPricesAfter = captureCartItemPrices();
        if (!cartItemNamesAfter.contains(removedProduct)) {
            commonFunctions.logStatus("Product removed correctly: " + removedProduct, Status.PASS, null);
        } else {
            commonFunctions.logStatus("Error: Product still present: " + removedProduct, Status.FAIL, null);
        }
        if (!cartItemPricesAfter.contains(removedPrice)) {
            commonFunctions.logStatus("Price removed correctly: " + removedPrice, Status.PASS, null);
        } else {
            commonFunctions.logStatus("Error: Price still present: " + removedPrice, Status.FAIL, null);
        }

    }

    public List<String> captureCartItemNames() {
        List<WebElement> cartItems = driver.findElements(checkoutPage.CHECKOUT_CARD_ITEM);
        List<String> cartItemNames = new ArrayList<>();

        for (WebElement item : cartItems) {
            cartItemNames.add(item.findElement(checkoutPage.CHECKOUT_CARD_ITEM_NAME).getText());
        }
        return cartItemNames;
    }

    public List<String> captureCartItemPrices() {
        List<WebElement> cartItems = driver.findElements(checkoutPage.CHECKOUT_CARD_ITEM);
        List<String> cartItemPrices = new ArrayList<>();

        for (WebElement item : cartItems) {
            cartItemPrices.add(item.findElement(checkoutPage.CHECKOUT_CARD_ITEM_PRICE).getText());
        }
        return cartItemPrices;
    }
}


