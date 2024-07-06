package com.project.JewelryMS.WebDriverTesting;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AutomationTest {
    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        // Set the path to the ChromeDriver executable
        //Note: The Path in setProperty can differs. Make adjustments accordingly.
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
    }
    @Test
    public void testTitle() {
        // Web Address
        driver.get("http://174.138.72.129/");
        String title = driver.getTitle();
        //List the title of the page, if the title is "Vite + React", the test is successful
        System.out.println("Page title is: " + title);
        assertEquals("Vite + React", title);
    }

    @Test
    public void testLogin() throws InterruptedException {
        // Test login for the web application
        driver.get("http://174.138.72.129/login");

        // Locate and fill the username field
        WebElement usernameField = driver.findElement(By.id("username"));
        usernameField.sendKeys("string");
        Thread.sleep(2000);

        // Locate and fill the password field
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("string");
        Thread.sleep(2000);

        // Locate and click the login button
        WebElement loginButton = driver.findElement(By.cssSelector(".login__form__left__buttonLogin"));
        loginButton.click();
        Thread.sleep(2000);

        // Wait for the success notification
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement successNotification = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".ant-notification-notice-success")));
        System.out.println("Success notification displayed.");

        // Verify the success notification message
        WebElement successMessage = successNotification.findElement(By.cssSelector(".ant-notification-notice-message"));
        assertTrue(successMessage.getText().contains("Thành công"));
        System.out.println("Login successful, verified success notification.");
        Thread.sleep(5000);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
