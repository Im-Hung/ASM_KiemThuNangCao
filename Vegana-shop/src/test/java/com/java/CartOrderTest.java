package com.java;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class CartOrderTest extends BaseTest {

    private static final String TEST_ID = "muahang";
    private static final String TEST_PASSWORD = "123123";

    private void loginBeforeTest() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        System.out.println("→ Đang đăng nhập...");
        driver.get(BASE_URL + "/login");
        Thread.sleep(2500);

        WebElement signInTab = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[@href='#signin']")
        ));
        js.executeScript("arguments[0].click();", signInTab);
        Thread.sleep(1500);

        WebElement idInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@id='signin']//input[@name='customerId']")
        ));
        idInput.clear();
        idInput.sendKeys(TEST_ID);

        WebElement passwordInput = driver.findElement(By.xpath("//div[@id='signin']//input[@placeholder='Password']"));
        passwordInput.clear();
        passwordInput.sendKeys(TEST_PASSWORD);

        WebElement submitBtn = driver.findElement(By.xpath("//div[@id='signin']//button[@type='submit']"));
        js.executeScript("arguments[0].click();", submitBtn);
        Thread.sleep(4000);

        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.contains("login")) {
            Assert.fail("Đăng nhập thất bại. Kiểm tra credentials: " + TEST_ID);
        }

        System.out.println("✅ Đã đăng nhập thành công");
    }

    @Test(priority = 10, description = "TC10: Thêm sản phẩm vào giỏ hàng")
    public void testAddToCart() throws InterruptedException {
        System.out.println("=".repeat(50));
        System.out.println("TC10: Thêm sản phẩm vào giỏ hàng");
        System.out.println("=".repeat(50));

        loginBeforeTest();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        driver.get(BASE_URL + "/");
        Thread.sleep(4000); // Đợi slider load

        // Scroll xuống section "Trending products"
        WebElement trendTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[contains(text(),'Trending products')]")
        ));
        js.executeScript("arguments[0].scrollIntoView(true);", trendTitle);
        Thread.sleep(3000); // Đợi slider xuất hiện

        // Thử nhiều cách tìm nút Add to Cart
        WebElement addToCart = null;

        try {
            // Cách 1: Tìm tất cả link Add to Cart và lấy link VISIBLE đầu tiên
            System.out.println("→ Tìm nút Add to Cart...");
            List<WebElement> addToCartLinks = driver.findElements(
                    By.xpath("//a[contains(@href,'addToCart')]")
            );

            System.out.println("Tìm thấy " + addToCartLinks.size() + " link Add to Cart");

            for (WebElement link : addToCartLinks) {
                if (link.isDisplayed() && link.isEnabled()) {
                    addToCart = link;
                    System.out.println("✅ Tìm thấy link Add to Cart visible");
                    break;
                }
            }

            if (addToCart == null) {
                // Cách 2: Click trực tiếp bằng JavaScript (bỏ qua visibility)
                System.out.println("⚠ Không tìm thấy link visible, thử click bằng JS...");
                addToCart = addToCartLinks.get(0);
            }

        } catch (Exception e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
            Assert.fail("Không tìm thấy nút Add to Cart");
        }

        // Scroll và click
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", addToCart);
        Thread.sleep(1500);

        try {
            addToCart.click();
        } catch (Exception e) {
            // Fallback: Click bằng JavaScript
            System.out.println("⚠ Click thường thất bại, dùng JS click...");
            js.executeScript("arguments[0].click();", addToCart);
        }

        Thread.sleep(3000);
        System.out.println("✅ Đã click Add to Cart");

        // Click vào icon giỏ hàng
        WebElement cartIcon = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("ul.right-widget a[href='/carts']"))
        );

        js.executeScript("arguments[0].scrollIntoView(true);", cartIcon);
        Thread.sleep(1000);

        try {
            cartIcon.click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", cartIcon);
        }

        Thread.sleep(3000);
        System.out.println("✅ Đã click icon giỏ hàng");

        // Verify
        String url = driver.getCurrentUrl();
        Assert.assertTrue(url.contains("/cart") || url.contains("/carts"), "Không vào được trang giỏ hàng");

        System.out.println("✅ TC10 PASSED");
    }

    @Test(priority = 11, description = "TC11: Chỉnh sửa số lượng")
    public void testUpdateCartQuantity() throws InterruptedException {
        System.out.println("=".repeat(50));
        System.out.println("TC11: Chỉnh sửa số lượng");
        System.out.println("=".repeat(50));

        loginBeforeTest();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        driver.get(BASE_URL + "/");
        Thread.sleep(4000);

        WebElement trendTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[contains(text(),'Trending products')]")
        ));
        js.executeScript("arguments[0].scrollIntoView(true);", trendTitle);
        Thread.sleep(3000);

        List<WebElement> addToCartLinks = driver.findElements(By.xpath("//a[contains(@href,'addToCart')]"));
        WebElement addToCart = addToCartLinks.stream().filter(WebElement::isDisplayed).findFirst().orElse(addToCartLinks.get(0));

        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", addToCart);
        Thread.sleep(1500);
        js.executeScript("arguments[0].click();", addToCart);
        Thread.sleep(3000);

        WebElement cartIcon = driver.findElement(By.cssSelector("ul.right-widget a[href='/carts']"));
        js.executeScript("arguments[0].click();", cartIcon);
        Thread.sleep(3000);

        WebElement quantityInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@type='number']")
        ));

        String initialQuantity = quantityInput.getAttribute("value");
        System.out.println("Số lượng ban đầu: " + initialQuantity);

        quantityInput.clear();
        int newQuantity = Integer.parseInt(initialQuantity) + 1;
        quantityInput.sendKeys(String.valueOf(newQuantity));
        js.executeScript("arguments[0].dispatchEvent(new Event('change'));", quantityInput);
        Thread.sleep(2000);

        System.out.println("✅ TC11 PASSED");
    }

    @Test(priority = 12, description = "TC12: Xóa sản phẩm")
    public void testRemoveFromCart() throws InterruptedException {
        System.out.println("=".repeat(50));
        System.out.println("TC12: Xóa sản phẩm");
        System.out.println("=".repeat(50));

        loginBeforeTest();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        driver.get(BASE_URL + "/");
        Thread.sleep(4000);

        WebElement trendTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[contains(text(),'Trending products')]")
        ));
        js.executeScript("arguments[0].scrollIntoView(true);", trendTitle);
        Thread.sleep(3000);

        List<WebElement> addToCartLinks = driver.findElements(By.xpath("//a[contains(@href,'addToCart')]"));
        WebElement addToCart = addToCartLinks.stream().filter(WebElement::isDisplayed).findFirst().orElse(addToCartLinks.get(0));
        js.executeScript("arguments[0].click();", addToCart);
        Thread.sleep(3000);

        WebElement cartIcon = driver.findElement(By.cssSelector("ul.right-widget a[href='/carts']"));
        js.executeScript("arguments[0].click();", cartIcon);
        Thread.sleep(3000);

        WebElement deleteBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//i[@class='fas fa-trash-alt']")
        ));
        js.executeScript("arguments[0].click();", deleteBtn);
        Thread.sleep(2000);

        WebElement yesBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("yesOption")));
        js.executeScript("arguments[0].click();", yesBtn);
        Thread.sleep(2000);

        System.out.println("✅ TC12 PASSED");
    }

    @Test(priority = 13, description = "TC13: Đặt hàng thành công")
    public void testCheckoutSuccess() throws InterruptedException {
        System.out.println("=".repeat(50));
        System.out.println("TC13: Đặt hàng thành công");
        System.out.println("=".repeat(50));

        loginBeforeTest();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        driver.get(BASE_URL + "/");
        Thread.sleep(4000);

        WebElement trendTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[contains(text(),'Trending products')]")
        ));
        js.executeScript("arguments[0].scrollIntoView(true);", trendTitle);
        Thread.sleep(3000);

        List<WebElement> addToCartLinks = driver.findElements(By.xpath("//a[contains(@href,'addToCart')]"));
        WebElement addToCart = addToCartLinks.stream().filter(WebElement::isDisplayed).findFirst().orElse(addToCartLinks.get(0));
        js.executeScript("arguments[0].click();", addToCart);
        Thread.sleep(3000);

        WebElement cartIcon = driver.findElement(By.cssSelector("ul.right-widget a[href='/carts']"));
        js.executeScript("arguments[0].click();", cartIcon);
        Thread.sleep(3000);

        WebElement checkoutBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@href='/checkout']")
        ));
        js.executeScript("arguments[0].click();", checkoutBtn);
        Thread.sleep(2000);

        WebElement receiverInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@placeholder='Full name']")
        ));
        receiverInput.sendKeys("Auto Test User");

        driver.findElement(By.xpath("//input[@placeholder='Address']")).sendKeys("123 Test St");
        driver.findElement(By.xpath("//input[@placeholder='Phone Number']")).sendKeys("0123456789");
        driver.findElement(By.xpath("//input[@placeholder='Description']")).sendKeys("Auto test");

        WebElement placeOrderBtn = driver.findElement(By.xpath("//button[@type='submit']"));
        js.executeScript("arguments[0].click();", placeOrderBtn);
        Thread.sleep(4000);

        System.out.println("✅ TC13 PASSED");
    }

    @Test(priority = 14, description = "TC14: Đặt hàng thiếu thông tin ")
    public void testCheckoutEmptyAddress() throws InterruptedException {
        System.out.println("=".repeat(50));
        System.out.println("TC14: Validation address");
        System.out.println("=".repeat(50));

        loginBeforeTest();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        driver.get(BASE_URL + "/");
        Thread.sleep(4000);

        WebElement trendTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[contains(text(),'Trending products')]")
        ));
        js.executeScript("arguments[0].scrollIntoView(true);", trendTitle);
        Thread.sleep(3000);

        List<WebElement> addToCartLinks = driver.findElements(By.xpath("//a[contains(@href,'addToCart')]"));
        WebElement addToCart = addToCartLinks.stream().filter(WebElement::isDisplayed).findFirst().orElse(addToCartLinks.get(0));
        js.executeScript("arguments[0].click();", addToCart);
        Thread.sleep(3000);

        WebElement cartIcon = driver.findElement(By.cssSelector("ul.right-widget a[href='/carts']"));
        js.executeScript("arguments[0].click();", cartIcon);
        Thread.sleep(3000);

        WebElement checkoutBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/checkout']")));
        js.executeScript("arguments[0].click();", checkoutBtn);
        Thread.sleep(2000);

        driver.findElement(By.xpath("//input[@placeholder='Full name']")).sendKeys("Auto Test");
        driver.findElement(By.xpath("//input[@placeholder='Address']")).clear(); // BỎ TRỐNG
        driver.findElement(By.xpath("//input[@placeholder='Phone Number']")).sendKeys("0123456789");

        WebElement placeOrderBtn = driver.findElement(By.xpath("//button[@type='submit']"));
        js.executeScript("arguments[0].click();", placeOrderBtn);
        Thread.sleep(2000);

        String url = driver.getCurrentUrl();
        Assert.assertTrue(url.contains("checkout"), "Vẫn ở checkout");

        System.out.println("✅ TC14 PASSED");
    }
}
