package automation.web.webpages;

import automation.web.setup.BaseTest;
import org.openqa.selenium.By;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DashboardPage extends BaseTest {

    public By SORT_ICON = By.xpath("//*[@id=\"inventory_filter_container\"]/select");
    public By PRODUCT = By.className("inventory_item");
    public By PRODUCT_NAME = By.className("inventory_item_name");
    public By PRODUCT_PRICE = By.className("inventory_item_price");
    public By CARD_BUTTON = By.xpath("//*[@class=\"btn_primary btn_inventory\"]");
    public By CHECKOUT_ICON = By.xpath("//a[@class='shopping_cart_link fa-layers fa-fw']//*[name()='svg']//*[name()='path' and contains(@fill,'currentCol')]");
    public By CHECKOUT_CARD_ITEM = By.className("cart_item");
    public By CHECKOUT_CARD_ITEM_NAME = By.className("inventory_item_name");
    public By CHECKOUT_CARD_ITEM_PRICE = By.className("inventory_item_price");
    public By CHECKOUT_CARD_ITEM_REMOVE = By.className("cart_button");



}
