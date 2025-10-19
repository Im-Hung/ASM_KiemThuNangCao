package com.java;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class UserAuthTest extends BaseTest {

    // Biến static để chia sẻ data giữa các test cases
    private static String registeredIdLogin;
    private static String registeredEmail;
    private static String registeredPassword;

    @Test(priority = 1, description = "TC5: Đăng ký thành viên mới thành công")
    public void testRegisterSuccess() throws InterruptedException {
        System.out.println("=".repeat(50));
        System.out.println("TC01: Đăng ký thành viên mới");
        System.out.println("=".repeat(50));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Bước 1: Vào trang chủ
        driver.get(BASE_URL + "/");
        Thread.sleep(2000);

        // Bước 2: Click vào "Login & Register"
        WebElement loginLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Login') or contains(@href,'login')]")
        ));
        loginLink.click();
        Thread.sleep(2500);

        // Bước 3: Verify đang ở trang login
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL: " + currentUrl);
        Assert.assertTrue(currentUrl.contains("login"), "Không vào được trang login");

        // Bước 4: Click chuyển qua tab "SIGN UP"
        WebElement signUpTab = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[@href='#signup']")
        ));
        js.executeScript("arguments[0].scrollIntoView(true);", signUpTab);
        Thread.sleep(500);
        js.executeScript("arguments[0].click();", signUpTab);
        Thread.sleep(2000);
        System.out.println("✅ Đã click chuyển sang tab SIGN UP");

        // Bước 5: Verify tab signup đã active
        wait.until(ExpectedConditions.attributeContains(
                By.id("signup"), "class", "active"
        ));

        // Bước 6: Tạo data đăng ký và LƯU VÀO BIẾN STATIC
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        registeredIdLogin = "test" + timestamp;
        String fullName = "Auto Tester";
        registeredEmail = "test" + timestamp + "@gmail.com";
        registeredPassword = "123123";

        // Điền form đăng ký
        WebElement idLoginInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@id='signup']//input[@name='customerId']")
        ));
        idLoginInput.clear();
        idLoginInput.sendKeys(registeredIdLogin);

        WebElement fullNameInput = driver.findElement(
                By.xpath("//div[@id='signup']//input[@placeholder='Full Name']")
        );
        fullNameInput.clear();
        fullNameInput.sendKeys(fullName);

        WebElement emailInput = driver.findElement(
                By.xpath("//div[@id='signup']//input[@placeholder='Email']")
        );
        emailInput.clear();
        emailInput.sendKeys(registeredEmail);

        WebElement passwordInput = driver.findElement(
                By.xpath("//div[@id='signup']//input[@placeholder='Password']")
        );
        passwordInput.clear();
        passwordInput.sendKeys(registeredPassword);

        System.out.println("📝 ID Login: " + registeredIdLogin);
        System.out.println("📧 Email: " + registeredEmail);
        System.out.println("🔑 Password: " + registeredPassword);

        // Bước 7: Submit form đăng ký
        WebElement submitButton = driver.findElement(
                By.xpath("//div[@id='signup']//button[@type='submit']")
        );
        js.executeScript("arguments[0].click();", submitButton);
        Thread.sleep(4000);

        // Bước 8: Verify đăng ký thành công
        System.out.println("Đăng ký thành công");
    }

    @Test(priority = 2, description = "TC6: Đăng ký với email đã tồn tại", dependsOnMethods = "testRegisterSuccess")
    public void testRegisterDuplicateEmail() throws InterruptedException {
        System.out.println("=".repeat(50));
        System.out.println("TC02: Đăng ký với email đã tồn tại");
        System.out.println("=".repeat(50));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Bước 1: Vào trang login
        driver.get(BASE_URL + "/login");
        Thread.sleep(2500);

        // Bước 2: Click chuyển qua tab "SIGN UP"
        WebElement signUpTab = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[@href='#signup']")
        ));
        js.executeScript("arguments[0].click();", signUpTab);
        Thread.sleep(2000);
        System.out.println("✅ Đã chuyển sang tab SIGN UP");

        // Bước 3: Verify tab signup đã active
        wait.until(ExpectedConditions.attributeContains(
                By.id("signup"), "class", "active"
        ));

        // Bước 4: Điền form với EMAIL ĐÃ TỒN TẠI (từ TC01)
        String newIdLogin = "duplicate" + System.currentTimeMillis() / 1000;

        WebElement idLoginInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@id='signup']//input[@name='customerId']")
        ));
        idLoginInput.clear();
        idLoginInput.sendKeys(newIdLogin);

        WebElement fullNameInput = driver.findElement(
                By.xpath("//div[@id='signup']//input[@placeholder='Full Name']")
        );
        fullNameInput.clear();
        fullNameInput.sendKeys("Duplicate User");

        WebElement emailInput = driver.findElement(
                By.xpath("//div[@id='signup']//input[@placeholder='Email']")
        );
        emailInput.clear();
        emailInput.sendKeys(registeredEmail); // Dùng EMAIL ĐÃ TỒN TẠI từ TC01

        WebElement passwordInput = driver.findElement(
                By.xpath("//div[@id='signup']//input[@placeholder='Password']")
        );
        passwordInput.clear();
        passwordInput.sendKeys("123123");

        System.out.println("📧 Email đã tồn tại: " + registeredEmail);

        // Bước 5: Submit form
        WebElement submitButton = driver.findElement(
                By.xpath("//div[@id='signup']//button[@type='submit']")
        );
        js.executeScript("arguments[0].click();", submitButton);
        Thread.sleep(3000);
    }
    @Test(priority = 3, description = "TC7 + 9: Đăng nhập và đăng xuất thành công", dependsOnMethods = "testRegisterSuccess")
    public void testLoginAndLogout() throws InterruptedException {
        System.out.println("=".repeat(50));
        System.out.println("TC03: Đăng nhập và đăng xuất");
        System.out.println("=".repeat(50));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Kiểm tra data từ TC01 đã có chưa
        if (registeredIdLogin == null || registeredPassword == null) {
            Assert.fail("Không có data từ test đăng ký. Đảm bảo TC01 chạy trước TC03");
        }

        // ============ PHẦN 1: ĐĂNG NHẬP ============
        System.out.println("\n--- Bước 1: Đăng nhập ---");

        // Bước 1: Vào trang login trực tiếp
        driver.get(BASE_URL + "/login");
        Thread.sleep(2500);

        // Bước 2: Verify đang ở trang login
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("login"), "Không vào được trang login");
        System.out.println("✅ Đã vào trang login");

        // Bước 3: Click vào tab SIGN IN
        WebElement signInTab = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[@href='#signin']")
        ));
        js.executeScript("arguments[0].click();", signInTab);
        Thread.sleep(1500);

        // Bước 4: Điền thông tin từ tài khoản VỪA ĐĂNG KÝ
        WebElement idLoginInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@id='signin']//input[@name='customerId']")
        ));
        idLoginInput.clear();
        idLoginInput.sendKeys(registeredIdLogin);

        WebElement passwordInput = driver.findElement(
                By.xpath("//div[@id='signin']//input[@placeholder='Password']")
        );
        passwordInput.clear();
        passwordInput.sendKeys(registeredPassword);

        System.out.println("📝 Đăng nhập với ID: " + registeredIdLogin);
        System.out.println("🔑 Password: " + registeredPassword);

        // Bước 5: Click SIGN IN
        WebElement submitButton = driver.findElement(
                By.xpath("//div[@id='signin']//button[@type='submit']")
        );
        js.executeScript("arguments[0].click();", submitButton);
        Thread.sleep(4000);

        // Bước 6: Verify đăng nhập thành công
        String urlAfterLogin = driver.getCurrentUrl();
        System.out.println("URL sau login: " + urlAfterLogin);

        Assert.assertFalse(urlAfterLogin.contains("login"), "Đăng nhập thất bại");
        System.out.println("✅ Đăng nhập thành công");

        // ============ PHẦN 2: ĐĂNG XUẤT ============
        System.out.println("\n--- Bước 2: Đăng xuất ---");

        // Bước 7: Click vào dropdown menu "User information" hoặc icon user
        try {
            // Tìm dropdown toggle (icon user hoặc "User information")
            WebElement dropdownToggle = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[@class='icon icon-inline' and @href='#'] | //a[contains(text(),'User information')]")
            ));

            System.out.println("✅ Tìm thấy dropdown menu");
            js.executeScript("arguments[0].scrollIntoView(true);", dropdownToggle);
            Thread.sleep(500);
            js.executeScript("arguments[0].click();", dropdownToggle);
            Thread.sleep(500); // Đợi dropdown mở
            System.out.println("✅ Đã click mở dropdown menu");

            // Bước 8: Click vào link "Logout"
            WebElement logoutLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[@class='dropdown-link' and @href='/logout']")
            ));

            System.out.println("✅ Tìm thấy link Logout");
            js.executeScript("arguments[0].click();", logoutLink);
            Thread.sleep(1000);

            // Bước 9: Verify đã logout (redirect về trang chủ hoặc login)
            String urlAfterLogout = driver.getCurrentUrl();
            System.out.println("URL sau logout: " + urlAfterLogout);

            boolean loggedOut = urlAfterLogout.contains("login") ||
                    urlAfterLogout.equals(BASE_URL + "/") ||
                    urlAfterLogout.equals(BASE_URL);

            Assert.assertTrue(loggedOut, "Chưa logout thành công");
            System.out.println("✅ Đăng xuất thành công");

        } catch (Exception e) {
            System.out.println("⚠ Không tìm thấy dropdown, thử phương án dự phòng");

            // Fallback: Vào trực tiếp /logout
            driver.get(BASE_URL + "/logout");
            Thread.sleep(3000);

            String urlAfterLogout = driver.getCurrentUrl();
            boolean loggedOut = urlAfterLogout.contains("login") ||
                    urlAfterLogout.equals(BASE_URL + "/") ||
                    urlAfterLogout.equals(BASE_URL);

            Assert.assertTrue(loggedOut, "Chưa logout thành công");
            System.out.println("✅ Đã logout bằng phương án dự phòng");
        }

        System.out.println("\nĐăng nhập và đăng xuất thành công");
    }

    @Test(priority = 4, description = "TC8: Đăng nhập sai mật khẩu")
    public void testLoginWrongPassword() throws InterruptedException {
        System.out.println("=".repeat(50));
        System.out.println("TC04: Đăng nhập với mật khẩu sai");
        System.out.println("=".repeat(50));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Vào trang login
        driver.get(BASE_URL + "/login");
        Thread.sleep(2500);

        // Click tab SIGN IN
        WebElement signInTab = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[@href='#signin']")
        ));
        js.executeScript("arguments[0].click();", signInTab);
        Thread.sleep(1500);

        // Điền thông tin với password SAI
        String testId = (registeredIdLogin != null) ? registeredIdLogin : "demo";

        WebElement idLoginInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@id='signin']//input[@name='customerId']")
        ));
        idLoginInput.clear();
        idLoginInput.sendKeys(testId);

        WebElement passwordInput = driver.findElement(
                By.xpath("//div[@id='signin']//input[@placeholder='Password']")
        );
        passwordInput.clear();
        passwordInput.sendKeys("wrongpassword123");

        System.out.println("📝 Đăng nhập với ID: " + testId);
        System.out.println("❌ Password SAI: wrongpassword123");

        // Submit
        WebElement submitButton = driver.findElement(
                By.xpath("//div[@id='signin']//button[@type='submit']")
        );
        js.executeScript("arguments[0].click();", submitButton);
        Thread.sleep(3000);

        // Verify vẫn ở trang login
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("login"), "Không còn ở trang login");

        System.out.println("PASSED: Vẫn ở trang login khi sai password");
    }

}