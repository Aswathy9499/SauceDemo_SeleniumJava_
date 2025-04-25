package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.*;
import utils.CustomElement;

public class LoginTests {

    WebDriver driver;
    

    @BeforeMethod
    public void openBrowser() {
        driver = new ChromeDriver(); // Open Chrome
        driver.manage().window().maximize(); // Maximize the window
        driver.get("https://www.saucedemo.com/"); // Go to website
    }
    
    @AfterMethod
    public void closeBrowser() {
        driver.quit(); // Close the browser
    }

    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return new Object[][]{
                {"standard_user", "secret_sauce", true},                //Valid Username and password
                {"locked_out_user", "secret_sauce", false},				//Locked Username and password
                {"standard_user", "wrongpass", false},					//Valid Username and Incorrect password
                {"", "secret_sauce", false},    						//Empty Username
                {"standard_user", "", false}							//Empty password
        };
    }

    @Test(dataProvider = "loginData")
    public void checkLogin(String username, String password, boolean shouldPass) {
        // Find and enter username
        WebElement usernameBox = driver.findElement(By.id("user-name"));
        usernameBox.clear();
        usernameBox.sendKeys(username);

        // Find and enter password
        WebElement passwordBox = driver.findElement(By.id("password"));
        passwordBox.clear();
        passwordBox.sendKeys(password);

        // Click login button
        driver.findElement(By.id("login-button")).click();
        
        if (shouldPass) {
        	System.out.println("Login successful for username: "+username+ " and password: "+password);
        } else {
            System.out.println("Login failed for username: "+username+ " and password: "+password);           
        }
        
        // Check local storage value
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String backtraceGuid = (String) js.executeScript("return window.localStorage.getItem('backtrace-guid');"); 
        String backtraceLastActive = (String) js.executeScript("return window.localStorage.getItem('backtrace-last-active');"); 
        System.out.println("Local Storage Values are as below:");
    	System.out.println("Back Trace Guid values is: "+backtraceGuid);
    	System.out.println("Back Trace Last Active value is: "+backtraceLastActive);
    }
    

    @Test 
    public void checkSessionCookie() {
        // Login with valid details
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        // Get cookie
        Cookie cookie = driver.manage().getCookieNamed("session-username");
        System.out.println("Cookie is: "+cookie);
        if (cookie != null) {
            Assert.assertEquals(cookie.getValue(), "standard_user");
        } else {
            Assert.fail("Cookie not found");
        }
    }   
    

    @Test
    public void showCustomGetTextUsage() {
    	driver.findElement(By.id("user-name")).sendKeys("locked_out_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        WebElement element = driver.findElement(By.xpath("//h3[contains(text(),'Epic sadface')]"));
        String text = CustomElement.getText(element);
        System.out.println("Error Message captured: " +text);
    }
}
