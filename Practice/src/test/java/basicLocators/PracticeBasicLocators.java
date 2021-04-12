package basicLocators;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class PracticeBasicLocators {

    WebDriver driver;

    @Test
    public void testClassLocator(){



    }



    //lets go to google, click on gmail and verify url contain word mail

    @Test
    public void testLinkLocator() throws InterruptedException{
        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();

        driver.manage().window().maximize();

        driver.get("https://www.google.com/");
        //make sure you pass full text of the link
        WebElement gmailLink = driver.findElement(By.linkText("Gmail"));

        gmailLink.click();

        Thread.sleep(3000);
        Assert.assertTrue(driver.getCurrentUrl().contains("mail"));



    }

    @Test
    public void testPartialLink() throws InterruptedException{
        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();
        driver.get("https://www.google.com/");
        //using partial link text we will click on How Search works

        WebElement link = driver.findElement(By.partialLinkText("How Search"));
        link.click();
        Thread.sleep(2000);
        //Discover How Google Search Works

        String expected = "Discover How Google Search Works";

        Assert.assertTrue("Title verification failed. Expected: "+expected+". Actual: "
                +driver.getTitle() ,driver.getTitle().contains(expected));


    }

    //this is the method to clean up after your test
    @After
    public void tearDown(){
        driver.close();
    }
}
