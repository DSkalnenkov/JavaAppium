package lib.ui;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import io.qameta.allure.Attachment;
import lib.Platform;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.regex.Pattern;

public class MainPageObject {
    protected RemoteWebDriver driver;

    public MainPageObject(RemoteWebDriver driver)
    {
        this.driver = driver;
    }

    public WebElement waitForElementPresent(String locator, String error_message, long timeoutInSeconds)
    {
        By by = this.getLocatorByString(locator);
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        Sleep(1000);
        return wait.until(
                ExpectedConditions.presenceOfElementLocated(by)
        );
    }
    public WebElement waitForElementPresent(String locator, String error_message)
    {
        return waitForElementPresent(locator, error_message, 5);
    }

    public WebElement waitForElementAndClick(String locator, String error_message, long timeoutInSeconds)
    {
        WebElement element = waitForElementPresent(locator, error_message, timeoutInSeconds);
        element.click();
        return element;
    }

    public WebElement waitForElementAndSendKeys(String locator, String value, String error_message, long timeoutInSeconds)
    {
        WebElement element = waitForElementPresent(locator, error_message, timeoutInSeconds);
        element.sendKeys(value);
        return element;
    }

    public boolean waitForElementNotPresent(String locator, String error_message, long timeoutInSeconds)
    {
        By by = this.getLocatorByString(locator);
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + '\n');
        return wait.until(
                ExpectedConditions.invisibilityOfElementLocated(by)
        );
    }

    public WebElement waitForElementAndClear(String locator, String error_message, long timeoutInSeconds)
    {
        WebElement element = waitForElementPresent(locator, error_message, timeoutInSeconds);
        element.clear();
        return element;
    }

    public String  assertElementHasText(String locator, String expected_text, String error_message, long timeoutInSeconds)
    {
        WebElement element = waitForElementPresent(locator, error_message, timeoutInSeconds);
        String actual_text = element.getAttribute("text");
        Assert.assertEquals(
                "We see unexpected text",
                expected_text,
                actual_text
        );
        return actual_text;
    }

    public void swipeUp(int timeOfSwipe)
    {
        if (driver instanceof  AppiumDriver){
            TouchAction action = new TouchAction((AppiumDriver) driver);
            Dimension size = driver.manage().window().getSize();
            int x = size.width / 2;
            int start_y = (int) (size.height * 0.8);
            int end_y = (int) (size.height * 0.2);
            Duration duration_of_swipe = Duration.ofMillis(timeOfSwipe);
            PointOption start_point = PointOption.point(x, start_y);
            PointOption end_point = PointOption.point(x, end_y);
            action.press(start_point).waitAction(WaitOptions.waitOptions(duration_of_swipe)).moveTo(end_point).release().perform();
        } else {
            System.out.println("Method swipeUp() do nothing for platform " + Platform.getInstance().getPlatformVar());
        }

    }

    public void swipeUpQuick()
    {
        swipeUp(200);
    }

    public void scrollWebPageUp() {
        if (Platform.getInstance().isMW()) {
            JavascriptExecutor JSExecutor = (JavascriptExecutor) driver;
            JSExecutor.executeScript("window.scrollBy(0,250)");
        } else {
            System.out.println("Method scrollWebPageUp() do nothing for platform " + Platform.getInstance().getPlatformVar());
        }
    }

    public void scrollWebPageTillElementNotVisible(String locator, String error_message, int max_swipes) {
        int already_swiped = 0;
        WebElement element = this.waitForElementPresent(locator, error_message);

        while (!this.isElementLocatedOnTheScreen(locator)) {
            while (!this.isElementLocatedOnTheScreen(locator)){
                scrollWebPageUp();
                ++already_swiped;
                if (already_swiped > max_swipes) {
                    Assert.assertTrue(error_message, element.isDisplayed());
                }
            }
        }


    }

    public void swipeUpToFindElement(String locator, String error_message, int max_swipes)
    {
        By by = this.getLocatorByString(locator);
        int already_swiped = 0;
        while (driver.findElements(by).size() == 0){
            if (already_swiped > max_swipes){
                waitForElementPresent(locator, "Cannot find element by swiping up. \n" + error_message, 0);
                return;
            }
            swipeUpQuick();
            ++already_swiped;
        }
    }

   public void swipeUpTillElementAppear(String locator, String error_message, int max_swipes)
   {
       int already_swiped = 0;
       while (!this.isElementLocatedOnTheScreen(locator))
       {
           if(already_swiped > max_swipes){
               Assert.assertTrue(error_message, this.isElementLocatedOnTheScreen(locator));
           }
           swipeUpQuick();
           ++already_swiped;
       }
   }

   public boolean isElementLocatedOnTheScreen(String locator)
    {
        int element_locator_by_y = this.waitForElementPresent(locator, "Cannot find element by locator", 1).getLocation().getY();
        if(Platform.getInstance().isMW()){
            JavascriptExecutor JSExecutor = (JavascriptExecutor) driver;
            Object js_result = JSExecutor.executeScript("return window.pageYOffset");
            element_locator_by_y -= Integer.parseInt(js_result.toString());
        }
        int screen_size_by_y = driver.manage().window().getSize().getHeight();
        return element_locator_by_y < screen_size_by_y;
    }

    protected void swipeElementToLeft(String locator, String error_message) {
        if (driver instanceof AppiumDriver) {
        WebElement element = waitForElementPresent(
                locator,
                error_message,
                10);

        int left_x = element.getLocation().getX();
        int right_x = left_x + element.getSize().getWidth();
        int upper_y = element.getLocation().getY();
        int lower_y = upper_y + element.getSize().getHeight();
        int middle_y = (upper_y + lower_y) / 2;

        PointOption start_point = PointOption.point(right_x, middle_y);
        PointOption finish_point = PointOption.point(left_x, middle_y);
        WaitOptions wait_duration_of_swipe = WaitOptions.waitOptions(Duration.ofMillis(300));
        TouchAction action = new TouchAction((AppiumDriver) driver);
        action.press(start_point);
        action.waitAction(wait_duration_of_swipe);

        if (Platform.getInstance().isAndroid()) {
            action.moveTo(finish_point);
        } else {
            int offset_x = (-1 * element.getSize().getHeight());
            PointOption finish_point_iOS =  PointOption.point(offset_x, 0);
            action.moveTo(finish_point_iOS);
        }

        action.release();
        action.perform();
    } else {
            System.out.println("Method swipeElementToLeft() do nothing for platform " + Platform.getInstance().getPlatformVar());
        }
    }

    public int getAmountOfElements(String locator, long timeoutInSeconds)
    {
        By by = this.getLocatorByString(locator);
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        List elements = driver.findElements(by);
        return elements.size();
    }

    public boolean isElementPresent(String locator){
        return getAmountOfElements(locator, 5) > 0;
    }

    public void tryClickElementWithFewAttempts(String locator, String  error_message, int amount_of_attempts) {
        int current_attempts = 0;
        boolean need_more_attempts = true;

        while (need_more_attempts){
            try {
                this.waitForElementAndClick(locator, error_message, 1);
                need_more_attempts = false;
            } catch (Exception e) {
                if (current_attempts > amount_of_attempts) {
                    this.waitForElementAndClick(locator, error_message, 1);
                }
            }
            ++current_attempts;
        }
    }

    public void assertElementsNotPresent(String locator, String error_message)
    {
        int amount_of_elements = getAmountOfElements(locator, 15);
        if (amount_of_elements > 0) {
            String default_message = "An element '" + locator + "' supposed to be not present";
            throw new AssertionError(default_message + " " + error_message);
        }
    }

    public String waitForElementAndGetAttribute(String locator, String attribute, String error_message, long timeoutInSeconds)
    {
        WebElement element = waitForElementPresent(locator, error_message, timeoutInSeconds);
        return element.getAttribute(attribute);
    }

    private By getLocatorByString(String locator_with_type)
    {
        String[] exploded_locator = locator_with_type.split(Pattern.quote(":"), 2);
        String by_type = exploded_locator[0];
        String locator = exploded_locator[1];

        if (by_type.equals("xpath")) {
            return By.xpath(locator);
        } else if (by_type.equals("id")) {
            return By.id(locator);
        } else if (by_type.equals("css")) {
            return By.cssSelector(locator);
        } else {
            throw new IllegalArgumentException("Cannot get type of locator: " + locator_with_type );
        }
    }

    protected void Sleep(int timeInMilliseconds)
    {
        try
        {
            Thread.sleep(timeInMilliseconds);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }

    public List<WebElement> getListOfElements(){
        By by = getLocatorByString("css:a.title");
        List<WebElement> listOfElements = driver.findElements(by);
        return  listOfElements;
    }

    public boolean ifHrefExistInWebElementList(String localHref, List<WebElement> listOfWebElements){
        String fullHref = "https://en.m.wikipedia.org/" + localHref;
        int i = 0;
        while (i < listOfWebElements.size()){
            String  curHref = listOfWebElements.get(i).getAttribute("href");
            if (fullHref.equals(curHref)) {
                return true;
            }
            i++;
        }
        return false;
    }

    public String takeScreenshot(String name)
    {
        TakesScreenshot ts = (TakesScreenshot)this.driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String path = System.getProperty("user.dir") + "/" + name + "_screenshot.png";
        try {
            FileUtils.copyFile(source, new File(path));
            System.out.println("The screenshot was taken: " + path);
        } catch (Exception e) {
            System.out.println("Cannot take screenshot. Error: " + e.getMessage());
        }
        return path;
    }

    @Attachment
    public static byte[] screenshot(String path){
        byte[] bytes = new byte[0];

        try{
            bytes = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            System.out.println("Cannot get bytes from screenshot. Error: " + e.getMessage());
        }
        return  bytes;
    }
}