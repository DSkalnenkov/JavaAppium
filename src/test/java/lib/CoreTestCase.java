package lib;

import io.appium.java_client.AppiumDriver;


import java.io.FileOutputStream;
import java.time.Duration;
import java.util.Properties;

import io.qameta.allure.Step;
import junit.framework.TestCase;
import lib.ui.WelcomePageObject;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class CoreTestCase{
        protected RemoteWebDriver driver;

        @Before
        @Step("Run driver and session")
        public void setUp() throws Exception
        {
            driver = Platform.getInstance().getDriver();
            this.createAllurePropertyFile();
            this.rotateScreenPortrait();
            this.skipWelcomePageForIOSApp();
            this.openWikiWebPageForMobileWeb();
        }

        @After
        @Step("Remove driver and session")
        public void tearDown()
        {
            this.driver.quit();
        }

        @Step("Rotate screen to the portrait mode")
        protected void rotateScreenPortrait()
        {
            if (driver instanceof AppiumDriver) {
                AppiumDriver driver = (AppiumDriver) this.driver;
                driver.rotate(ScreenOrientation.PORTRAIT);
            } else {
                System.out.println("Method rotateScreenPortrait() does nothing for platform " + Platform.getInstance().getPlatformVar());
            }

        }

        @Step("Rotate screen to the landscape mode")
        protected void rotateScreenLandscape()
        {
            if (driver instanceof AppiumDriver) {
                AppiumDriver driver = (AppiumDriver) this.driver;
                driver.rotate(ScreenOrientation.LANDSCAPE);
            } else {
                System.out.println("Method rotateScreenLandscape() does nothing for platform " + Platform.getInstance().getPlatformVar());
            }
        }

        @Step("Open Wikipedia URL for Mobile Web (this method does nothing for Android and iOS)")
        protected void openWikiWebPageForMobileWeb()
        {
            if(Platform.getInstance().isMW()){
                driver.get("https://en.m.wikipedia.org");
            } else {
            System.out.println("Method openWikiWebPageForMobileWeb() do nothing for platform " + Platform.getInstance().getPlatformVar());
            }
        }

        @Step("Send mobile app to background (this method does nothing for Mobile Web)")
        protected void backgroundApp(int seconds)
        {
            if (driver instanceof AppiumDriver) {
                AppiumDriver driver = (AppiumDriver) this.driver;
                driver.runAppInBackground(Duration.ofSeconds(2L));
            } else {
                System.out.println("Method backgroundApp() does nothing for platform " + Platform.getInstance().getPlatformVar());
            }

        }

        @Step("Skip welcome page screen for iOS")
        private void  skipWelcomePageForIOSApp()
        {
            if(Platform.getInstance().isIOS()){
                AppiumDriver driver = (AppiumDriver) this.driver;
                WelcomePageObject WelcomePageObject = new WelcomePageObject(driver);
                WelcomePageObject.clickSkip();
            }
        }

        private void createAllurePropertyFile()
        {
            String path = System.getProperty("allure.results.directory");
            try {
                Properties props =  new Properties();
                FileOutputStream fos = new FileOutputStream(path + "/environment.properties");
                props.setProperty("Environment", Platform.getInstance().getPlatformVar());
                props.store(fos, "See https://github.com/allure-framework/allure-app/wiki/Enviroment");
                fos.close();
            } catch (Exception e) {
                System.err.println("IO problem when writing allure properties file");
                e.printStackTrace();
            }
        }
}




