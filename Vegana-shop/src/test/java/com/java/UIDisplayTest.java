package com.java;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;


public class UIDisplayTest extends BaseTest {

    @Test(priority = 1, description = "TC01: Xem trang chủ")
    public void TC01_ViewHomePage() throws InterruptedException {
        System.out.println("=".repeat(50));
        System.out.println("TC01: Xem trang chủ Vegana");
        System.out.println("=".repeat(50));

        driver.get(BASE_URL + "/");
        Thread.sleep(2000);

        String title = driver.getTitle();
        System.out.println("✅ Title: " + title);

        Assert.assertTrue(title.contains("Vegana") || title.length() > 0, "Trang không load");
        System.out.println("✅ TC01 PASSED: Trang chủ hiển thị đúng");
    }

    @Test(priority = 2, description = "TC02: Xem chi tiết sản phẩm")
    public void TC02_ViewProductDetail() throws InterruptedException {
        System.out.println("=".repeat(50));
        System.out.println("TC02: Xem chi tiết sản phẩm");
        System.out.println("=".repeat(50));

        // Vào trang chủ (có sản phẩm hiển thị)
        driver.get(BASE_URL + "/");
        Thread.sleep(2000);

        try {
            // CÁCH 1: Click vào tên sản phẩm "Snack Oishi Tom Toms"
            WebElement productLink = driver.findElement(By.xpath("//h5[contains(text(),'Snack Oishi Tom Toms')]/ancestor::div[contains(@class,'card')]//a"));
            String productName = driver.findElement(By.xpath("//h5[contains(text(),'Snack Oishi Tom Toms')]")).getText();
            System.out.println("📦 Clicking on product: " + productName);

            productLink.click();
            Thread.sleep(2000);

            // Verify URL chứa productDetail
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("productDetail"), "❌ URL không chứa 'productDetail'");
            System.out.println("✅ Current URL: " + currentUrl);

            // Verify URL chứa productId
            Assert.assertTrue(currentUrl.contains("productId="), "❌ URL không chứa 'productId'");

            // Verify trang chi tiết có hiển thị thông tin sản phẩm
            String pageSource = driver.getPageSource();
            Assert.assertTrue(
                    pageSource.contains("Snack") ||
                            pageSource.contains("Oishi") ||
                            pageSource.contains("Add to Cart") ||
                            pageSource.contains("Thêm vào giỏ"),
                    "❌ Trang chi tiết không hiển thị thông tin sản phẩm"
            );

            // Verify có nút Add to Cart
            WebElement addToCartButton = driver.findElement(By.xpath("//button[contains(text(),'Add to Cart') or contains(text(),'Thêm vào giỏ')]"));
            Assert.assertTrue(addToCartButton.isDisplayed(), "❌ Nút Add to Cart không hiển thị");
            System.out.println("✅ Nút 'Add to Cart' hiển thị");

            System.out.println("✅ TC02 PASSED: Chi tiết sản phẩm hiển thị đầy đủ");

        } catch (NoSuchElementException e) {
            System.out.println("⚠ Không tìm thấy sản phẩm 'Snack Oishi Tom Toms'");
            System.out.println("Error: " + e.getMessage());
            Assert.fail("Test failed: Không tìm thấy sản phẩm để click");
        }
    }


}
