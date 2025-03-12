import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

public class WebShopTests {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "https://demowebshop.tricentis.com/";
    private static final String PASSWORD = "Password123";
    private String EMAIL;
    
    public static void createNewUser(String email) {
        WebDriver driverSetup = new ChromeDriver();
        try {
            WebDriverWait waitSetup = new WebDriverWait(driverSetup, Duration.ofSeconds(10));
            
            driverSetup.get(BASE_URL);
            
            driverSetup.findElement(By.xpath("//a[text()='Log in']")).click();
            
            driverSetup.findElement(By.xpath("//a[text()='Register']")).click();
            
            driverSetup.findElement(By.id("gender-male")).click();
            driverSetup.findElement(By.id("FirstName")).sendKeys("John");
            driverSetup.findElement(By.id("LastName")).sendKeys("Doe");
            driverSetup.findElement(By.id("Email")).sendKeys(email);
            driverSetup.findElement(By.id("Password")).sendKeys(PASSWORD);
            driverSetup.findElement(By.id("ConfirmPassword")).sendKeys(PASSWORD);
            
            driverSetup.findElement(By.id("register-button")).click();
            
            //Wait for registration to complete
            waitSetup.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@class='result' and contains(text(), 'completed')]")));
                
                driverSetup.findElement(By.xpath("//input[@value='Continue']")).click();
                
                System.out.println("User created successfully: " + email);
            } finally {
                driverSetup.quit();
            }
        }
        
    @Before
    public void setUp() {
        this.EMAIL = "user_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
        createNewUser(this.EMAIL);
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }
    
    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    @Test
    public void loginTest() throws IOException {
        performLoginTest();
    }
    
    private boolean login(String email, String password) {
        driver.findElement(By.xpath("//a[text()='Log in']")).click();
        
        driver.findElement(By.id("Email")).sendKeys(email);
        driver.findElement(By.id("Password")).sendKeys(password);
        driver.findElement(By.xpath("//input[@value='Log in']")).click();
        
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[@class='account' and contains(text(), '" + email + "')]")));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void logout() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@class='ico-logout']")));
        driver.findElement(By.xpath("//a[@class='ico-logout']")).click();
    }

    private void changePassword(String newPassword) {
        driver.findElement(By.xpath("//a[@class='account']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='Change password']"))).click();
        
        driver.findElement(By.id("OldPassword")).sendKeys(PASSWORD);
        driver.findElement(By.id("NewPassword")).sendKeys(newPassword);
        driver.findElement(By.id("ConfirmNewPassword")).sendKeys(newPassword);
        driver.findElement(By.xpath("//input[@value='Change password']")).click();
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[@class='result' and contains(text(), 'Password was changed')]")));
    }

    private void performLoginTest() throws IOException {
        
        driver.get(BASE_URL);
        
        login(EMAIL, PASSWORD);
      
        String newPassword = "NewPassword123";
        changePassword(newPassword);
        
        logout();

        if(login(EMAIL, PASSWORD)){
            throw new IOException("Login with old password was accepted");
        } else {
            System.out.println("Login with old password failed");
        }

        if(!login(EMAIL, newPassword)){
            throw new IOException("Login with new password failed");
        } else {
            System.out.println("Login with new password succeeded");
        }
        
        System.out.println("Test completed successfully");
    }
}