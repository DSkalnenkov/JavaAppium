package tests;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import lib.CoreTestCase;
import lib.Platform;
import lib.ui.WelcomePageObject;
import org.junit.Test;


@Epic("Welcome screen tests")
public class GetStartedTest extends CoreTestCase {

    @Test
    @Features(value = {@Feature(value="Welcome screen"), @Feature(value="Article"), @Feature(value = "Search")})
    @DisplayName("Passing through the welcome page")
    @Description("We are passing through the welcome page when iOS app is started")
    @Step("Starting test testPassThroughWelcome")
    @Severity(value = SeverityLevel.MINOR)
    public void testPassThroughWelcome() {

        if (Platform.getInstance().isAndroid() || Platform.getInstance().isMW()) {
            return;
        }

        WelcomePageObject WelcomePage = new WelcomePageObject(driver);

        WelcomePage.waitForLearnMoreLink();
        WelcomePage.clickNextButton();

        WelcomePage.waitForNewWayToExploreText();
        WelcomePage.clickNextButton();

        WelcomePage.waitForAddOrEditPreferredLangText();
        WelcomePage.clickNextButton();

        WelcomePage.waitForMoreAboutDataCollectedText();
        WelcomePage.clickGetStartedButton();
    }
}
