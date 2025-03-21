package automation.web.webpages;

import automation.web.setup.BaseTest;
import org.openqa.selenium.By;

public class SearchPage extends BaseTest {
    public By SEARCH_FIELD = By.id("searchBar");
    public By SUBMIT_BUTTON = By.id("submit");
    public By SEARCH_RESULTS = By.xpath("//div[@class='user-result']");
    public By USER_NAME = By.xpath("//div[@class='user-result']//h2");
    public By USER_EMAIL = By.xpath("//div[@class='user-result']//p[@class='email']");





}
