package automation.web.steps;

import com.aventstack.extentreports.Status;
import io.cucumber.java.en.And;

import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import automation.web.setup.BaseTest;
import automation.web.webpages.DashboardPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class DashboardSteps extends BaseTest {

    DashboardPage dashboard = new DashboardPage();
    public  List<String> productNames ;
    public static List<Double> productPrices;
    public  List<String> cartItemNames ;
    public  List<Double> cartItemPrices;


    @And("User Sort Products From Low to High")
    public void validateSort() throws IOException {
        sortProducts();
        List<Double> pricesAfter = commonFunctions.captureElementsAfterSorting(dashboard.PRODUCT_PRICE);
        commonFunctions.validateSorting( pricesAfter);

    }

    @And("User Add Three Random Products")
    public void addRandomProductsToCart() {
        try {
            List<WebElement> products = driver.findElements(dashboard.PRODUCT);
            if (products.isEmpty()) {
                commonFunctions.logStatus("No products found on the page", Status.FAIL, null);
                return;
            }
            Random random = new Random();
            HashSet<Integer> selectedProductIndexes = new HashSet<>();
            while (selectedProductIndexes.size() < 3) {
                selectedProductIndexes.add(random.nextInt(products.size()));
            }
             productNames = new ArrayList<>();
             productPrices = new ArrayList<>();
            for (Integer index : selectedProductIndexes) {
                WebElement product = products.get(index);
                String productName = product.findElement(dashboard.PRODUCT_NAME).getText();
                String productPrice = product.findElement(dashboard.PRODUCT_PRICE).getText();
                double price = Double.parseDouble(productPrice.replace("$", ""));
                productNames.add(productName);
                productPrices.add(price);
                WebElement addToCartButton = product.findElement(By.className("btn_inventory")); // Get the "Add to Cart" button for the current product
                commonFunctions.clickOnElement(addToCartButton, "Click on Add to Cart Button for " + productName);
            }
            commonFunctions.logStatus("Selected products: " + productNames, Status.INFO, null);
            commonFunctions.logStatus("Selected product prices: " + productPrices, Status.INFO, null);
        } catch (Exception ex) {
            commonFunctions.handleException("Error in adding random products to the cart", ex);
        }
    }

    @Then("Validate the cart with selected products")
    public void validateCardWithSelectedProducts(){
        validateCard(productNames, productPrices);
    }
    public void validateCard(List<String> productNames, List<Double> productPrices) {
        try {
            commonFunctions.clickOnElement(dashboard.CHECKOUT_ICON, "Click on checkout Icon");
            List<WebElement> cartItems = driver.findElements(dashboard.CHECKOUT_CARD_ITEM);
            if (cartItems.isEmpty()) {
                commonFunctions.logStatus("No items found in the cart", Status.FAIL, null);
                return;
            }
            cartItemNames = new ArrayList<>();
            cartItemPrices = new ArrayList<>();
            for (WebElement item : cartItems) {
                cartItemNames.add(item.findElement(dashboard.CHECKOUT_CARD_ITEM_NAME).getText());
                cartItemPrices.add(Double.valueOf(item.findElement(dashboard.CHECKOUT_CARD_ITEM_PRICE).getText()));
            }
            commonFunctions.logStatus("Cart item names: " + cartItemNames, Status.INFO, null);
            commonFunctions.logStatus("Cart item prices: " + cartItemPrices, Status.INFO, null);
            for (String name : cartItemNames) {
                if (!productNames.contains(name)) {
                    commonFunctions.logStatus("Mismatch in product name: " + name, Status.FAIL, null);
                }
                commonFunctions.logStatus("Product Names Is Matched  " + name, Status.PASS, null);
            }

            for (Double price : cartItemPrices) {
                if (!productPrices.contains(price)) {
                    commonFunctions.logStatus("Mismatch in product price: " + price, Status.FAIL, null);
                }
            }
        } catch (Exception ex) {
            commonFunctions.handleException("Error in validating the cart", ex);
        }
    }


    public void sortProducts() throws IOException {
        commonFunctions.selectDropdownOptionByValue(dashboard.SORT_ICON,"lohi","Sorting Results");
    }




}


