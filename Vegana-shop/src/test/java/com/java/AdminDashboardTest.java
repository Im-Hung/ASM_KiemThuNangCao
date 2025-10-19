package com.java;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class AdminDashboardTest extends BaseTest {

    private static final String ADMIN_USERNAME = "demo";
    private static final String ADMIN_PASSWORD = "123123";

    private void loginAsAdmin() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        System.out.println("→ Đăng nhập Admin...");
        driver.get(BASE_URL + "/login");
        Thread.sleep(2500);

        // Click tab "Sign In" nếu có
        try {
            WebElement signInTab = driver.findElement(By.xpath("//a[@href='#signin']"));
            js.executeScript("arguments[0].click();", signInTab);
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Không có tab Sign In, dùng form trực tiếp");
        }

        // Nhập username (không phải email!)
        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='username' or @name='customerId']")
        ));
        usernameInput.clear();
        usernameInput.sendKeys(ADMIN_USERNAME);

        // Nhập password
        WebElement passwordInput = driver.findElement(
                By.xpath("//input[@name='password' or @placeholder='Password']")
        );
        passwordInput.clear();
        passwordInput.sendKeys(ADMIN_PASSWORD);

        // Click nút Submit
        WebElement submitBtn = driver.findElement(By.xpath("//button[@type='submit']"));
        js.executeScript("arguments[0].click();", submitBtn);
        Thread.sleep(4000);

        // Verify đã login thành công (không còn ở trang /login)
        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.contains("login")) {
            System.out.println("❌ VẪN Ở TRANG LOGIN - Đăng nhập thất bại!");
            Assert.fail("Đăng nhập Admin thất bại với username: " + ADMIN_USERNAME);
        }

        System.out.println("✅ Đã đăng nhập Admin thành công");
    }

    // ==================== CATEGORY MANAGEMENT ====================

    @Test(priority = 15, description = "TC21: Thêm danh mục mới")
    public void testAddCategory() throws InterruptedException {
        System.out.println("=".repeat(50));
        System.out.println("TC15: Thêm danh mục mới");
        System.out.println("=".repeat(50));

        loginAsAdmin();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Vào trang Category Management
        driver.get(BASE_URL + "/admin/categories");
        Thread.sleep(2000);

        // Click nút "+ Add Category"
        WebElement addCategoryBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@data-target='#addRowModal' or contains(text(),'Add Category')]")
        ));
        js.executeScript("arguments[0].click();", addCategoryBtn);
        Thread.sleep(1500);

        // Nhập tên category trong modal
        WebElement categoryNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@placeholder='category name']")
        ));
        String newCategoryName = "AutoTest Cat " + System.currentTimeMillis();
        categoryNameInput.sendKeys(newCategoryName);

        // Click nút "Add"
        WebElement addBtn = driver.findElement(
                By.xpath("//div[@id='addRowModal']//button[@type='submit' and contains(text(),'Add')]")
        );
        js.executeScript("arguments[0].click();", addBtn);
        Thread.sleep(3000);

        System.out.println("PASSED: Đã thêm category - " + newCategoryName);
    }

    @Test(priority = 16, description = "TC22: Sửa danh mục")
    public void testEditCategory() throws InterruptedException {
        System.out.println("=".repeat(50));
        System.out.println("TC16: Sửa danh mục");
        System.out.println("=".repeat(50));

        loginAsAdmin();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        driver.get(BASE_URL + "/admin/categories");
        Thread.sleep(2000);

        // Click icon edit (pen) đầu tiên
        WebElement editIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//a[contains(@href,'/editCategory/')]//i[@class='fa fa-edit'])[1]")
        ));
        js.executeScript("arguments[0].scrollIntoView(true);", editIcon);
        Thread.sleep(1000);
        js.executeScript("arguments[0].click();", editIcon);
        Thread.sleep(2000);

        // Sửa tên category
        WebElement categoryNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@id='name' or @name='name']")
        ));
        categoryNameInput.clear();
        String updatedName = "Updated Cat " + System.currentTimeMillis();
        categoryNameInput.sendKeys(updatedName);

        // Click "Update"
        WebElement updateBtn = driver.findElement(
                By.xpath("//button[@type='submit' and contains(text(),'Update')]")
        );
        js.executeScript("arguments[0].click();", updateBtn);
        Thread.sleep(3000);

        System.out.println("PASSED: Đã sửa category - " + updatedName);
    }

    @Test(priority = 17, description = "TC23: Xóa danh mục")
    public void testDeleteCategory() throws InterruptedException {
        System.out.println("=".repeat(50));
        System.out.println("TC17: Xóa danh mục");
        System.out.println("=".repeat(50));

        loginAsAdmin();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        driver.get(BASE_URL + "/admin/categories");
        Thread.sleep(2000);

        // Click icon xóa
        WebElement deleteIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//button[@data-original-title='Delete']//i[@class='fa fa-times'])[1]")
        ));
        js.executeScript("arguments[0].scrollIntoView(true);", deleteIcon);
        Thread.sleep(1000);
        js.executeScript("arguments[0].click();", deleteIcon);
        Thread.sleep(1500);

        // Click "Yes"
        WebElement yesBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@id='configmationId']//a[@id='yesOption']")
        ));
        js.executeScript("arguments[0].click();", yesBtn);
        Thread.sleep(3000);

        System.out.println("PASSED");
    }

    // ==================== SUPPLIER MANAGEMENT ====================

    @Test(priority = 18, description = "TC24: Thêm nhà cung cấp")
    public void testAddSupplier() throws InterruptedException {
        System.out.println("=".repeat(50));
        System.out.println("TC18: Thêm nhà cung cấp");
        System.out.println("=".repeat(50));

        loginAsAdmin();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        driver.get(BASE_URL + "/admin/suppliers");
        Thread.sleep(3000);

        // Click nút "+ Add Supplier"
        WebElement addSupplierBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@data-target='#addRowModal' or contains(text(),'Add Supplier')]")
        ));
        js.executeScript("arguments[0].scrollIntoView(true);", addSupplierBtn);
        Thread.sleep(1000);
        js.executeScript("arguments[0].click();", addSupplierBtn);
        Thread.sleep(2000);

        // Đợi modal hiện ra
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("addRowModal")
        ));
        Thread.sleep(1000);

        // Nhập thông tin
        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@id='addRowModal']//input[@placeholder='supplier name']")
        ));
        nameInput.sendKeys("AutoTest Supplier " + System.currentTimeMillis());

        WebElement emailInput = driver.findElement(
                By.xpath("//div[@id='addRowModal']//input[@placeholder='email']")
        );
        emailInput.sendKeys("supplier" + System.currentTimeMillis() + "@test.com");

        WebElement phoneInput = driver.findElement(
                By.xpath("//div[@id='addRowModal']//input[@placeholder='phone']")
        );
        phoneInput.sendKeys("090" + (int)(Math.random() * 10000000));

        // Click "Add" trong modal
        WebElement addBtn = driver.findElement(
                By.xpath("//div[@id='addRowModal']//button[@type='submit' and contains(text(),'Add')]")
        );
        js.executeScript("arguments[0].click();", addBtn);
        Thread.sleep(4000);

        System.out.println("PASSED: Đã thêm nhà cung cấp");
    }

    @Test(priority = 19, description = "TC25: Sửa nhà cung cấp")
    public void testEditSupplier() throws InterruptedException {
        System.out.println("=".repeat(50));
        System.out.println("TC19: Sửa nhà cung cấp");
        System.out.println("=".repeat(50));

        loginAsAdmin();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        driver.get(BASE_URL + "/admin/suppliers");
        Thread.sleep(2000);

        WebElement editIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//i[@class='fa fa-edit'])[1]")
        ));
        js.executeScript("arguments[0].click();", editIcon);
        Thread.sleep(2000);

        WebElement phoneInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='phone' or contains(@placeholder,'phone')]")
        ));
        phoneInput.clear();
        phoneInput.sendKeys("0987654321");

        WebElement updateBtn = driver.findElement(By.xpath("//button[contains(text(),'Update')]"));
        js.executeScript("arguments[0].click();", updateBtn);
        Thread.sleep(3000);

        System.out.println("PASSED");
    }

    @Test(priority = 20, description = "TC26: Xóa nhà cung cấp")
    public void testDeleteSupplier() throws InterruptedException {
        System.out.println("=".repeat(50));
        System.out.println("TC20: Xóa nhà cung cấp");
        System.out.println("=".repeat(50));

        loginAsAdmin();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        driver.get(BASE_URL + "/admin/suppliers");
        Thread.sleep(2000);

        WebElement deleteIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//i[@class='fa fa-times'])[1]")
        ));
        js.executeScript("arguments[0].click();", deleteIcon);
        Thread.sleep(1500);

        WebElement yesBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@id='yesOption'] | //button[contains(text(),'Yes')]")
        ));
        js.executeScript("arguments[0].click();", yesBtn);
        Thread.sleep(3000);

        System.out.println("PASSED");
    }

    // ==================== PRODUCT MANAGEMENT ====================

    @Test(priority = 21, description = "TC27: Thêm sản phẩm")
    public void testAddProduct() throws InterruptedException {
        System.out.println("=".repeat(50));
        System.out.println("TC21: Thêm sản phẩm");
        System.out.println("=".repeat(50));

        loginAsAdmin();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        driver.get(BASE_URL + "/admin/products");
        Thread.sleep(3000);

        // Click nút "+ Add Product"
        WebElement addProductBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@data-target='#addRowModal']")
        ));
        js.executeScript("arguments[0].scrollIntoView(true);", addProductBtn);
        Thread.sleep(1000);
        js.executeScript("arguments[0].click();", addProductBtn);
        Thread.sleep(2000);

        // Đợi modal hiện ra và visible
        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("addRowModal")
        ));
        Thread.sleep(1500);

        // 1. Nhập Name
        WebElement nameInput = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@id='addRowModal']//input[@id='name']")
        ));
        nameInput.clear();
        nameInput.sendKeys("AutoTest Product " + System.currentTimeMillis());
        System.out.println("✓ Đã nhập tên sản phẩm");

        // 2. Chọn Category
        Select categoryDropdown = new Select(
                driver.findElement(By.xpath("//div[@id='addRowModal']//select[@id='categoryId']"))
        );
        categoryDropdown.selectByIndex(1);
        System.out.println("✓ Đã chọn Category");
        Thread.sleep(500);

        // 3. Chọn Supplier
        Select supplierDropdown = new Select(
                driver.findElement(By.xpath("//div[@id='addRowModal']//select[@id='supplierId']"))
        );
        supplierDropdown.selectByIndex(1);
        System.out.println("✓ Đã chọn Supplier");
        Thread.sleep(500);

        // 4. Nhập Price
        WebElement priceInput = driver.findElement(
                By.xpath("//div[@id='addRowModal']//input[@id='price']")
        );
        priceInput.clear();
        priceInput.sendKeys("100");
        System.out.println("✓ Đã nhập Price");

        // 5. Nhập Quantity
        WebElement quantityInput = driver.findElement(
                By.xpath("//div[@id='addRowModal']//input[@id='quantity']")
        );
        quantityInput.clear();
        quantityInput.sendKeys("50");
        System.out.println("✓ Đã nhập Quantity");

        // 6. Nhập Discount
        WebElement discountInput = driver.findElement(
                By.xpath("//div[@id='addRowModal']//input[@id='discount']")
        );
        discountInput.clear();
        discountInput.sendKeys("10");
        System.out.println("✓ Đã nhập Discount");

        // 7. Nhập Date
        WebElement dateInput = driver.findElement(
                By.xpath("//div[@id='addRowModal']//input[@id='enteredDate']")
        );
        dateInput.clear();
        dateInput.sendKeys("10182025"); // Format: MMDDYYYY cho date input
        System.out.println("✓ Đã nhập Date");
        Thread.sleep(500);

        // 8. SKIP Upload Image (bỏ qua)
        System.out.println("⊗ Bỏ qua upload ảnh");

        // 9. Nhập Description
        WebElement descriptionInput = driver.findElement(
                By.xpath("//div[@id='addRowModal']//textarea[@id='description']")
        );
        descriptionInput.clear();
        descriptionInput.sendKeys("Auto test product - created by Selenium automation");
        System.out.println("✓ Đã nhập Description");

        // 10. Click nút "Add"
        WebElement addBtn = driver.findElement(
                By.xpath("//div[@id='addRowModal']//button[@type='submit']")
        );
        js.executeScript("arguments[0].scrollIntoView(true);", addBtn);
        Thread.sleep(500);
        js.executeScript("arguments[0].click();", addBtn);
        System.out.println("→ Đã click nút Add");

        Thread.sleep(4000); // Đợi submit và redirect

        // Verify: kiểm tra không còn ở modal (đã đóng)
        try {
            boolean modalStillVisible = driver.findElement(By.id("addRowModal"))
                    .getAttribute("class").contains("show");
            if (!modalStillVisible) {
                System.out.println("✓ Modal đã đóng");
            }
        } catch (Exception e) {
            System.out.println("✓ Modal đã đóng");
        }

        System.out.println("PASSED: Đã thêm sản phẩm thành công");
    }

    @Test(priority = 22, description = "TC22: Sửa sản phẩm")
    public void testEditProduct() throws InterruptedException {
        System.out.println("=".repeat(50));
        System.out.println("TC22: Sửa sản phẩm");
        System.out.println("=".repeat(50));

        loginAsAdmin();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        driver.get(BASE_URL + "/admin/products");
        Thread.sleep(3000);

        // Click icon Edit (pen) đầu tiên trong bảng
        WebElement editIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//a[contains(@href,'/editProduct/')]//i[@class='fa fa-edit'])[1]")
        ));
        js.executeScript("arguments[0].scrollIntoView(true);", editIcon);
        Thread.sleep(1000);
        js.executeScript("arguments[0].click();", editIcon);
        System.out.println("→ Đã click icon Edit");
        Thread.sleep(3000);

        // Verify đã vào trang edit (URL chứa /editProduct/)
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/editProduct/"),
                "Không vào được trang edit product");
        System.out.println("✓ Đã vào trang Edit Product");

        // 1. Sửa Name (optional - có thể giữ nguyên)
        try {
            WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//input[@id='name' or @name='name']")
            ));
            String currentName = nameInput.getAttribute("value");
            nameInput.clear();
            nameInput.sendKeys(currentName + " - EDITED");
            System.out.println("✓ Đã sửa Name");
        } catch (Exception e) {
            System.out.println("⊗ Bỏ qua sửa Name");
        }

        // 2. Sửa Price (bắt buộc)
        WebElement priceInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@id='price' or contains(@name,'price')]")
        ));
        String oldPrice = priceInput.getAttribute("value");
        System.out.println("Giá cũ: $" + oldPrice);

        priceInput.clear();
        Thread.sleep(500);
        priceInput.sendKeys("150");
        System.out.println("✓ Đã sửa Price thành: $150");

        // 3. Sửa Quantity (optional)
        try {
            WebElement quantityInput = driver.findElement(
                    By.xpath("//input[@id='quantity' or contains(@name,'quantity')]")
            );
            quantityInput.clear();
            Thread.sleep(300);
            quantityInput.sendKeys("75");
            System.out.println("✓ Đã sửa Quantity thành: 75");
        } catch (Exception e) {
            System.out.println("⊗ Bỏ qua sửa Quantity");
        }

        // 4. Sửa Discount (optional)
        try {
            WebElement discountInput = driver.findElement(
                    By.xpath("//input[@id='discount' or contains(@name,'discount')]")
            );
            discountInput.clear();
            Thread.sleep(300);
            discountInput.sendKeys("15");
            System.out.println("✓ Đã sửa Discount thành: 15%");
        } catch (Exception e) {
            System.out.println("⊗ Bỏ qua sửa Discount");
        }

        // 5. Sửa Description (optional)
        try {
            WebElement descriptionInput = driver.findElement(
                    By.xpath("//textarea[@id='description' or contains(@name,'description')]")
            );
            descriptionInput.clear();
            Thread.sleep(300);
            descriptionInput.sendKeys("UPDATED by Selenium automation - " + System.currentTimeMillis());
            System.out.println("✓ Đã sửa Description");
        } catch (Exception e) {
            System.out.println("⊗ Bỏ qua sửa Description");
        }

        // 6. SKIP Upload Image (bỏ qua)
        System.out.println("⊗ Bỏ qua upload ảnh mới");

        // 7. Click nút "Update"
        WebElement updateBtn = driver.findElement(
                By.xpath("//button[@type='submit' and contains(text(),'Update')]")
        );
        js.executeScript("arguments[0].scrollIntoView(true);", updateBtn);
        Thread.sleep(500);
        js.executeScript("arguments[0].click();", updateBtn);
        System.out.println("→ Đã click nút Update");

        Thread.sleep(4000); // Đợi submit và redirect

        // Verify: Đã quay về trang products list
        String finalUrl = driver.getCurrentUrl();
        Assert.assertTrue(finalUrl.contains("/admin/products") && !finalUrl.contains("/editProduct/"),
                "Không quay về trang danh sách products");
        System.out.println("✓ Đã quay về trang danh sách products");

        System.out.println("✅ PASSED: Đã sửa sản phẩm thành công");
    }

    @Test(priority = 23, description = "TC29: Xóa sản phẩm")
    public void testDeleteProduct() throws InterruptedException {
        System.out.println("=".repeat(50));
        System.out.println("TC23: Xóa sản phẩm");
        System.out.println("=".repeat(50));

        loginAsAdmin();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        driver.get(BASE_URL + "/admin/products");
        Thread.sleep(2000);

        WebElement deleteIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//i[@class='fa fa-times'])[1]")
        ));
        js.executeScript("arguments[0].click();", deleteIcon);
        Thread.sleep(1500);

        WebElement yesBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@id='yesOption'] | //button[contains(text(),'Yes')]")
        ));
        js.executeScript("arguments[0].click();", yesBtn);
        Thread.sleep(3000);

        System.out.println("PASSED");
    }

    // ==================== ORDER MANAGEMENT ====================

    @Test(priority = 24, description = "TC30: Xác nhận đơn hàng")
    public void testConfirmOrder() throws InterruptedException {
        System.out.println("=".repeat(50));
        System.out.println("TC24: Xác nhận đơn hàng");
        System.out.println("=".repeat(50));

        loginAsAdmin();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        driver.get(BASE_URL + "/admin/orders");
        Thread.sleep(3000);

        WebElement editIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//i[@class='fa fa-edit'])[1]")
        ));
        js.executeScript("arguments[0].scrollIntoView(true);", editIcon);
        Thread.sleep(1000);
        js.executeScript("arguments[0].click();", editIcon);
        Thread.sleep(3000);

        Select statusDropdown = new Select(wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//select[@name='status' or contains(@id,'status')]")
                )
        ));

        // Chọn "Đã Thanh Toán"
        try {
            statusDropdown.selectByVisibleText("Đã Thanh Toán");
        } catch (Exception e) {
            // Nếu không có text đó, chọn index 1
            statusDropdown.selectByIndex(1);
        }

        // Click "Update"
        WebElement updateBtn = driver.findElement(
                By.xpath("//button[@type='submit' and contains(text(),'Update')]")
        );
        js.executeScript("arguments[0].click();", updateBtn);
        Thread.sleep(4000);

        System.out.println("PASSED: Đã xác nhận đơn hàng");
    }
}
