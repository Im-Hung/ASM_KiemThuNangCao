package com.java;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
    protected WebDriver driver;
    protected final String BASE_URL = "http://localhost:8080";

    @BeforeMethod
    public void setUp() {
        System.out.println("🔧 Setting up WebDriver...");

        ChromeOptions options = new ChromeOptions();
        String ciMode = System.getProperty("ci.mode", "false");

        if ("true".equals(ciMode)) {
            System.out.println("Running in CI mode (headless)");
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
        } else {
            System.out.println("💻 Running in local mode (with browser UI)");
        }

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void tearDown() {
        System.out.println("\nCleaning up...");
        if (driver != null) {
            driver.quit();
            System.out.println("WebDriver closed");
        }
    }
}
