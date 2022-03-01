package lib.ui.mobile_web;

import lib.ui.NavigationUI;
import org.openqa.selenium.remote.RemoteWebDriver;

public class MWNavigationUI extends NavigationUI {

    static {
        MY_LISTS_LINK = "css:a>span.mw-ui-icon-minerva-watchlist";
        OPEN_NAVIGATION = "css:#mw-mf-main-menu-button";
        DELETE_FROM_LIST_BUTTON = "li.page-summary[title='{TITLE}']>a.watched";
    }
    public MWNavigationUI(RemoteWebDriver driver)
    {
        super(driver);
    }
}
