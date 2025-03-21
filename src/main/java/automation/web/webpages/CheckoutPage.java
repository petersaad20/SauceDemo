package automation.web.webpages;

import automation.web.setup.BaseTest;
import org.openqa.selenium.By;

public class CheckoutPage extends BaseTest {

    public By CHECKOUT_CARD_ITEM = By.className("cart_item");
    public By CHECKOUT_CARD_ITEM_NAME = By.className("inventory_item_name");
    public By CHECKOUT_CARD_ITEM_PRICE = By.className("inventory_item_price");
    public By CHECKOUT_CARD_ITEM_REMOVE = By.className("cart_button");
    public By CHECKOUT_BUTTON = By.className("checkout_button");
    public By CHECKOUT_FIRST_NAME = By.id("first-name");
    public By CHECKOUT_LAST_NAME = By.id("last-name");
    public By CHECKOUT_ZIP_CODE= By.id("postal-code");
    public By CHECKOUT_CONTINUE_BUTTON = By.className("cart_button");
    public By CHECKOUT_ERROR_BUTTON = By.xpath("//h3[normalize-space()='Error: Postal Code is required']");
    public By CHECKOUT_REVIEW_HEADER = By.className("subheader");
    public By CHECKOUT_SUBTOTAL_AMOUNT =By.className("summary_subtotal_label");
    public By CHECKOUT_TAX_AMOUNT =By.className("summary_tax_label");
    public By CHECKOUT_TOTAL_AMOUNT =By.className("summary_total_label");
    public By CHECKOUT_FINISH =By.className("btn_action");
    public By CHECKOUT_SUCESS_SCREEN =By.className("complete-header");


}
