package com.example;

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
            
            driverSetup.findElement(By.linkText("Log in")).click();
            
            driverSetup.findElement(By.linkText("Register")).click();
            
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
    
    private void login(String email, String password) {
        driver.findElement(By.linkText("Log in")).click();
        
        driver.findElement(By.id("Email")).sendKeys(email);
        driver.findElement(By.id("Password")).sendKeys(password);
        driver.findElement(By.xpath("//input[@value='Log in']")).click();
        

    }

    private void logout() {
        driver.findElement(By.xpath("//a[@class='ico-logout']")).click();
    }

    private void performLoginTest() throws IOException {
        
        driver.get(BASE_URL);
        
        login(EMAIL, PASSWORD);
      
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//a[@class='account' and contains(text(), '" + EMAIL + "')]")));

        logout();

        System.out.println("Test completed successfully");
    }
}