package org.carlook.tests;

import io.github.bonigarcia.seljup.SeleniumExtension;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@ExtendWith(SeleniumExtension.class)
public class LoginTest {

    private static WebDriver driver;

    public LoginTest(){
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        driver = new ChromeDriver();
    }


    public void login(String email, String pw) {
        driver.get("http://localhost:8080/");

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("loginButton")));

        WebElement emailField = driver.findElement(By.id("emailField"));
        emailField.sendKeys(email);

        WebElement pwField = driver.findElement(By.id("pwField"));
        pwField.sendKeys(pw);

        WebElement loginButton = driver.findElement(By.id("loginButton"));
        loginButton.click();
    }

    @Test
    public void loginKunde() {
        login("mb@fake.com", "passwort");

        WebDriverWait wait = new WebDriverWait(driver,10);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("markeField")));

        String url = driver.getCurrentUrl();
        Assert.assertEquals("http://localhost:8080/#!Autosuche", url);
        driver.quit();
    }

    @Test
    public void loginVertriebler(){
        login("as@fake.com", "passwort");

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("addButton")));

        String url = driver.getCurrentUrl();
        Assert.assertEquals("http://localhost:8080/#!MeineInseriertenAutos", url);
        driver.quit();
    }
}