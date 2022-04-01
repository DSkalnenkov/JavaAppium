package tests;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import lib.CoreTestCase;
import lib.Platform;
import lib.ui.*;
import lib.ui.factories.ArticlePageObjectFactory;
import lib.ui.factories.MyListsPageObjectFactory;
import lib.ui.factories.NavigationUIFactory;
import lib.ui.factories.SearchPageObjectFactory;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import java.util.List;
@Epic(value = "Tests for list")
public class MyListsTests extends CoreTestCase {
    public static final String
            name_of_folder = "Learning programming",
            login = "DSkalnenkov",
            password = "Skalnenkov898989!";

    @Test
    @Features(value = {@Feature(value="List"), @Feature(value="Article")})
    @DisplayName("Save first article to the favorite list")
    @Description("We save firsta article to the favorite list")
    @Step("Starting test testSaveFirstArticleToMyList")
    @Severity(value=SeverityLevel.NORMAL)
    public void testSaveFirstArticleToMyList()
    {
        SearchPageObject SearchPageObject = SearchPageObjectFactory.get(driver);

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Java");
        SearchPageObject.clickByArticleWithSubstring("bject-oriented programming language");

        ArticlePageObject ArticlePageObject = ArticlePageObjectFactory.get(driver);
        ArticlePageObject.waitForTitleElement();
        String article_title = ArticlePageObject.getArticleTitle();

        if (Platform.getInstance().isAndroid()) {
            ArticlePageObject.addArticleToMyList(name_of_folder);
        } else {
            ArticlePageObject.addArticleToMySaved();
        }
        if (Platform.getInstance().isMW()) {
            AuthorizationPageObject Auth = new AuthorizationPageObject(driver);
            Auth.clickAuthButton();
            Auth.enterLoginData(login, password);
            Auth.submitForm();

            ArticlePageObject.waitForTitleElement();
            Assert.assertEquals("We are not on the same page after login.",
                    article_title,
                    ArticlePageObject.getArticleTitle()
            );
            ArticlePageObject.addArticleToMySaved();
        }

        ArticlePageObject.closeArticle();

        NavigationUI NavigationUI = NavigationUIFactory.get(driver);
        NavigationUI.openNavigation();
        NavigationUI.clickMyList();

        MyListsPageObject MyListsPageObject = MyListsPageObjectFactory.get(driver);
        if (Platform.getInstance().isAndroid()) {
            MyListsPageObject.openFolderByName(name_of_folder);
        }

        MyListsPageObject.swipeByArticleToDelete(article_title);
    }

    @Test
    @Features(value = {@Feature(value="List"), @Feature(value="Article")})
    @DisplayName("Save two articles to the favorite list and delete the first one")
    @Description("We save  two articles to the favorite list and delete the first one")
    @Step("Starting test testSaveTwoArticlesToMyListAndDeleteFirst")
    @Severity(value=SeverityLevel.NORMAL)
    public void testSaveTwoArticlesToMyListAndDeleteFirst() {
        SearchPageObject SearchPageObject = SearchPageObjectFactory.get(driver);

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Java");
        SearchPageObject.clickByArticleWithSubstring("bject-oriented programming language");

        ArticlePageObject ArticlePageObject = ArticlePageObjectFactory.get(driver);
        ArticlePageObject.waitForTitleElement();
        String article_title = ArticlePageObject.getArticleTitle();

        if (Platform.getInstance().isAndroid()) {
            ArticlePageObject.addArticleToMyList(name_of_folder);
        } else {
            ArticlePageObject.addArticleToMySaved();
        }

        if (Platform.getInstance().isMW()) {
            AuthorizationPageObject Auth = new AuthorizationPageObject(driver);
            Auth.clickAuthButton();
            Auth.enterLoginData(login, password);
            Auth.submitForm();
            ArticlePageObject.waitForTitleElement();
            Assert.assertEquals("We are not on the same page after login.",
                    article_title,
                    ArticlePageObject.getArticleTitle()
            );
            ArticlePageObject.addArticleToMySaved();
        }

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Conga");
        SearchPageObject.clickByArticleWithSubstring("uban drum");

        NavigationUI NavigationUI = NavigationUIFactory.get(driver);
        NavigationUI.openNavigation();
        NavigationUI.clickMyList();

        MyListsPageObject MyListsPageObject = MyListsPageObjectFactory.get(driver);
        if (Platform.getInstance().isAndroid()) {
            MyListsPageObject.openFolderByName(name_of_folder);
        }

        MyListsPageObject.swipeByArticleToDelete(article_title);

        Assert.assertFalse(MyListsPageObject.ifHrefExistInWebElementList("wiki/Java_(programming_language)", MyListsPageObject.getListOfElements()));

        Assert.assertTrue(MyListsPageObject.ifHrefExistInWebElementList("wiki/Conga", MyListsPageObject.getListOfElements()));
    }
}


