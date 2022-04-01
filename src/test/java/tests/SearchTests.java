package tests;

import java.util.List;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import lib.CoreTestCase;
import lib.ui.SearchPageObject;
import lib.ui.factories.SearchPageObjectFactory;
import org.junit.Assert;
import org.junit.Test;

@Epic(value="Test for searching functions")
public class SearchTests extends CoreTestCase {
    public SearchTests() {
    }

    @Test
    @Features(value = {@Feature(value="Search")})
    @DisplayName("Search article by title")
    @Description("We type 'Java' into search box and check results of searching by substring 'bject-oriented programming language'")
    @Step("Starting test testSearch")
    @Severity(value=SeverityLevel.BLOCKER)

    public void testSearch() {
        SearchPageObject SearchPageObject = SearchPageObjectFactory.get(driver);
        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Java");
        SearchPageObject.waitForSearchResult("bject-oriented programming language");
    }

    @Test
    @Features(value = {@Feature(value="Search")})
    @DisplayName("Cancel article search by clicking of Cancel button")
    @Description("We set focus to search box and cancel searching by clicking on cancel button")
    @Step("Starting test testCancelSearch")
    public void testCancelSearch() {
        SearchPageObject SearchPageObject = SearchPageObjectFactory.get(driver);
        SearchPageObject.initSearchInput();
        SearchPageObject.waitForCancelButtonToAppear();
        SearchPageObject.clickCancelSearch();
        SearchPageObject.waitForCancelButtonToDisappear();
    }

    @Test
    @Features(value = {@Feature(value="Search")})
    @DisplayName("Amount of the search results")
    @Description("We count amount of articles in the result list")
    @Step("Starting test testAmountOfNotEmptySearch")
    public void testAmountOfNotEmptySearch() {
        SearchPageObject SearchPageObject = SearchPageObjectFactory.get(driver);
        SearchPageObject.initSearchInput();
        String search_line = "Linking Park Discography";
        SearchPageObject.typeSearchLine(search_line);
        int amount_of_search_results = SearchPageObject.getAmountOfFoundArticles();
        Assert.assertTrue("We've found to few results", amount_of_search_results > 0);
    }

    @Test
    @Features(value = {@Feature(value="Search")})
    @DisplayName("Wrong keyword for searching")
    @Description("We count amount of articles in the result list after typing a wrong into searching box")
    @Step("Starting test testAmountOfNotEmptySearch")
    public void testAmountOfEmptySearch() {
        SearchPageObject SearchPageObject = SearchPageObjectFactory.get(driver);
        SearchPageObject.initSearchInput();
        String search_line = "afsdfafdasgweg";
        SearchPageObject.typeSearchLine(search_line);
        SearchPageObject.waitForEmptyResultsLabel();
        SearchPageObject.assertThereIsNoResultOfSearch();
    }

    @Test
    @Features(value = {@Feature(value="Search")})
    @DisplayName("Reset of searching results")
    @Description("We type word 'Conga' in searching box, check correctness of the found article and reset the result list")
    @Step("Starting test testSearchTextAndCancel")
    public void testSearchTextAndCancel() {
        SearchPageObject SearchPageObject = SearchPageObjectFactory.get(driver);
        SearchPageObject.initSearchInput();
        String search_line = "Conga";
        SearchPageObject.typeSearchLine(search_line);
        SearchPageObject.waitForSearchResult("Cuban drum");
        int resultSearchCount = SearchPageObject.getSearchList();
        SearchPageObject.waitForCancelButtonToAppear();
        SearchPageObject.clickCancelSearch();
        int resultSearchCountAfterSearchCancel = SearchPageObject.getSearchList();
        Assert.assertTrue("Count of search results is not more than one", resultSearchCount > 1);
        Assert.assertEquals("Count of search results still exists after the search cancel", 0, resultSearchCountAfterSearchCancel);
    }
}
