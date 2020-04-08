package com.tqs_assignment.airquality.selenium;// Generated by Selenium IDE

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class HourlyTest {
    JavascriptExecutor js;
    private WebDriver driver;
    private Map<String, Object> vars;

    @Before
    public void setUp() {
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;
        vars = new HashMap<>();
        driver.get("http://localhost:8080/hourly");
        driver.manage().window().setSize(new Dimension(1296, 741));
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void success() throws InterruptedException {
        driver.findElement(By.id("place")).click();
        driver.findElement(By.id("place")).sendKeys("Aveiro,Portugal");
        driver.findElement(By.id("hours")).click();
        driver.findElement(By.id("hours")).sendKeys("3");
        driver.findElement(By.id("searchButton")).click();
        Thread.sleep(6000);
        WebDriverWait wait = new WebDriverWait(driver, 30);
        WebElement selectPollutant = wait.until(ExpectedConditions.elementToBeClickable(By.id("pollutant")));
        selectPollutant.click();
        assertThat(driver.findElement(By.id("pollutant")).getText(), containsString("Pollutant:"));
       /* assertThat(driver.findElement(By.xpath("(//li[@id=\'pollutant\'])[2]")).getText(), containsString("Pollutant:"));
        assertThat(driver.findElement(By.xpath("(//li[@id=\'pollutant\'])[3]")).getText(), containsString("Pollutant:"));
        */
    }

    @Test
    public void malFormedInputHours() {
        driver.findElement(By.id("place")).click();
        driver.findElement(By.id("place")).sendKeys("Aveiro,Portugal");
        driver.findElement(By.id("hours")).click();
        driver.findElement(By.id("hours")).sendKeys("aveiro");
        driver.findElement(By.id("searchButton")).click();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        WebElement selectIncorrect = wait.until(ExpectedConditions.elementToBeClickable(By.id("incorrect")));
        selectIncorrect.click();
        assertThat(driver.findElement(By.id("incorrect")).getText(), is("Number of hours or City Name Invalid!"));
    }

    @Test
    public void incorrectHours() {
        driver.findElement(By.id("place")).click();
        driver.findElement(By.id("place")).sendKeys("Aveiro,Portugal");
        driver.findElement(By.id("hours")).click();
        {
            WebElement element = driver.findElement(By.id("hours"));
            Actions builder = new Actions(driver);
            builder.doubleClick(element).perform();
        }
        driver.findElement(By.id("hours")).sendKeys("-32");
        driver.findElement(By.id("searchButton")).click();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        WebElement selectIncorrect = wait.until(ExpectedConditions.elementToBeClickable(By.id("incorrect")));
        selectIncorrect.click();
        assertThat(driver.findElement(By.id("incorrect")).getText(), is("Number of hours or City Name Invalid!"));
    }

    @Test
    public void incorrectCity() {
        driver.findElement(By.id("place")).click();
        driver.findElement(By.id("place")).sendKeys("Non,ExisitngPlacees");
        driver.findElement(By.id("hours")).click();
        driver.findElement(By.id("hours")).sendKeys("3");
        driver.findElement(By.id("searchButton")).click();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        WebElement selectIncorrect = wait.until(ExpectedConditions.elementToBeClickable(By.id("incorrect")));
        selectIncorrect.click();
        assertThat(driver.findElement(By.id("incorrect")).getText(), is("Number of hours or City Name Invalid!"));
    }

    @Test
    public void didntPutHours() {
        driver.findElement(By.id("place")).click();
        driver.findElement(By.id("place")).sendKeys("Aveiro,Portugal");
        driver.findElement(By.id("searchButton")).click();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        WebElement selectMissing = wait.until(ExpectedConditions.elementToBeClickable(By.id("missing")));
        selectMissing.click();
        assertThat(driver.findElement(By.id("missing")).getText(), is("Didn't input City Name or Hours!"));
    }

    @Test
    public void didntPutCity() {
        driver.findElement(By.id("hours")).click();
        driver.findElement(By.id("hours")).sendKeys("3");
        driver.findElement(By.id("searchButton")).click();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        WebElement selectMissing = wait.until(ExpectedConditions.elementToBeClickable(By.id("missing")));
        selectMissing.click();
        assertThat(driver.findElement(By.id("missing")).getText(), is("Didn't input City Name or Hours!"));
    }

    @Test
    public void didntPutAnything() {
        driver.findElement(By.id("searchButton")).click();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        WebElement selectMissing = wait.until(ExpectedConditions.elementToBeClickable(By.id("missing")));
        selectMissing.click();
        assertThat(driver.findElement(By.id("missing")).getText(), is("Didn't input City Name or Hours!"));
    }
}
