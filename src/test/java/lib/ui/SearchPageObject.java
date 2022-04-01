package lib.ui;

import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

abstract public class SearchPageObject extends MainPageObject {
     protected static String
     SEARCH_RESULT,
     SEARCH_INIT_ELEMENT,
     SEARCH_INPUT,
     SEARCH_CANCEL_BUTTON,
     SEARCH_RESULT_BY_SUBSTRING_TPL,
     SEARCH_RESULT_ELEMENT,
     SEARCH_EMPTY_RESULT_ELEMENT;

    public SearchPageObject(RemoteWebDriver driver)
    {
        super(driver);
    }

    /* TEMPLATED METHODs */
    private static String getResultSearchElement(String substring)
    {
        return SEARCH_RESULT_BY_SUBSTRING_TPL.replace("{SUBSTRING}", substring);
    }

    /* TEMPLATED METHODs */

    @Step("Initializing the search field")
    public void initSearchInput() {
        this.waitForElementPresent(SEARCH_INIT_ELEMENT, "Cannot find search input after clicking search init element");
        this.waitForElementAndClick(SEARCH_INIT_ELEMENT, "Cannot find and click search init element", 5L);
    }

    @Step("Waiting for button to cancel search result")
    public void waitForCancelButtonToAppear() {
        this.waitForElementPresent(SEARCH_CANCEL_BUTTON, "Cannot find search cancel button", 15L);
    }

    @Step("Waiting for search cancel button to disappear")
    public void waitForCancelButtonToDisappear() {
        this.waitForElementNotPresent(SEARCH_CANCEL_BUTTON, "Search cancel button is still present", 15L);
    }

    @Step("Clicking button to cancel search results")
    public void clickCancelSearch() {
        this.waitForElementAndClick(SEARCH_CANCEL_BUTTON, "Cannot find and click cancel button", 5L);
    }

    @Step("Typing '{search_line}' to the search line")
    public void typeSearchLine(String search_line) {
        this.waitForElementAndSendKeys(SEARCH_INPUT, search_line, "Cannot find and type into search input", 5L);
    }

    @Step("Waiting for search result")
    public void waitForSearchResult(String substring) {
        String search_result_xpath = getResultSearchElement(substring);
        this.waitForElementPresent(search_result_xpath, "Cannot find search result" + substring);
    }

    @Step("Waiting for search result and select an article by substring in article title")
    public void clickByArticleWithSubstring(String substring) {
        String search_result_xpath = getResultSearchElement(substring);
        this.waitForElementAndClick(search_result_xpath, "Cannot find and click search result" + substring, 10L);
    }

    @Step("Get amount of found articles")
    public int getAmountOfFoundArticles() {
        this.waitForElementPresent(SEARCH_RESULT_ELEMENT, "Cannot find anything by the request ", 15L);
        return this.getAmountOfElements(SEARCH_RESULT_ELEMENT, 15L);
    }

    @Step("Waiting for empty result label")
    public void waitForEmptyResultsLabel() {
        this.waitForElementPresent(SEARCH_EMPTY_RESULT_ELEMENT, "Cannot find empty result element", 15L);
    }

    @Step("Making sure there are no results for the search")
    public void assertThereIsNoResultOfSearch() {
        this.assertElementsNotPresent(SEARCH_RESULT_ELEMENT, "We supposed not to find any results");
    }

    @Step("Getting a search list")
    public int getSearchList() {
        return getAmountOfElements(SEARCH_RESULT_ELEMENT, 10);
    }
}