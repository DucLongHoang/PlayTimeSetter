package application;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BrowserManipulator {

    private String saturday = "/html/body/div[5]/div/div/div/div/div/div/div[9]";
    private String sunday = "/html/body/div[5]/div/div/div/div/div/div/div[3]";
    static final Map<Integer, Double> timeAdjust = new HashMap<>();
    public static double getAdjust(int time){
        return timeAdjust.get(time);
    }


    public LinkedList<MarkEntry> getAllMarks() throws InterruptedException {
        LinkedList<MarkEntry> result = new LinkedList<>();

        System.setProperty("webdriver.gecko.driver", "S:/Libs/geckodriver-v0.29.1-win64/geckodriver.exe");
        System.setProperty("webdriver.firefox.bin", "C:/Program Files/Mozilla Firefox/firefox.exe");
        FirefoxOptions fOptions = new FirefoxOptions();
        fOptions.setHeadless(true);
        int sleepTime = 0;

        WebDriver driver = new FirefoxDriver(fOptions);
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.get("https://skola.plzen-edu.cz");

        // login
        driver.findElement(By.xpath("//input[@id='uwt_ctl00_JmenoUzivatele']")).sendKeys("Khoi Nguyen");
        driver.findElement(By.xpath("//input[@id='uwt_ctl00_HesloUzivatele']")).sendKeys("filip2007");
        driver.findElement(By.xpath("//button[@id='uwt_ctl00_SignIn']")).click();
/*
        // change academic year
        driver.findElement(By.xpath("//a[@class='period']")).click();
        Thread.sleep(sleepTime);
        driver.findElement(By.xpath("//select[@id='ddlSkolniRok']")).click();
        Thread.sleep(sleepTime);
        driver.findElement(By.xpath("//option[@value='B2459']")).click();
        Thread.sleep(sleepTime);
        driver.findElement(By.xpath("//button[@id='btnUlozit']")).click();
        Thread.sleep(sleepTime);
*/
        // clicking to the marks
        driver.findElement(By.xpath("//a[text()='Hodnocení']")).click();
        Thread.sleep(sleepTime);
        driver.findElement(By.xpath("//a[text()='Výpisy hodnocení']")).click();
        Thread.sleep(sleepTime);
        driver.findElement(By.xpath("//a[text()='Průběžné hodnocení']")).click();
        Thread.sleep(sleepTime);

        WebElement marksTable = driver.findElement(By.id("G_ctl00xmainxgridHodnoceni"));
        //int numMarks = driver.findElements(By.xpath("//table[@id='G_ctl00xmainxgridHodnoceni']/tbody/tr")).size();
        String s = driver.findElement((By.xpath("//table[@class='UWGPager']/tbody/tr/td[1]"))).getText();
        int numMarks = Integer.parseInt(s.substring(15));
        //int colNum = driver.findElements(By.xpath("//table[@id='G_ctl00xmainxgridHodnoceni']/thead/tr/th")).size();
        List<WebElement> rowValues = marksTable.findElements(By.tagName("tr"));

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String[] cellValues = new String[5];
        List<WebElement> colValues;
        MarkEntry temp;
        int loop = 0;

        for (int i = 1; i < numMarks; i++) {
            colValues = rowValues.get(i).findElements(By.tagName("td"));
            for (int j = 3; j < 8; j++) {
                cellValues[loop] = colValues.get(j).getText();
                loop++;
            }
            cellValues[3] = cellValues[3].replace(',', '.');
            temp = new MarkEntry(LocalDate.parse(cellValues[0], dtf), cellValues[1], cellValues[2],
                    Integer.parseInt(cellValues[4]), Double.parseDouble(cellValues[3]));
            // add mark to final result
            result.add(temp);
            loop = 0;
        }
        Thread.sleep(500);
        driver.close();

        return result;
    }

    public void setPlayTime(int sat, int sun) throws InterruptedException {
        System.setProperty("webdriver.gecko.driver", "S:/Libs/geckodriver-v0.29.1-win64/geckodriver.exe");
        System.setProperty("webdriver.firefox.bin", "C:/Program Files/Mozilla Firefox/firefox.exe");
        FirefoxOptions fOptions = new FirefoxOptions();
        fOptions.setHeadless(false);

        WebDriver driver = new FirefoxDriver(fOptions);
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.get("https://account.microsoft.com/family/settings/screen-time/3578722689714235306/devices");

        // login
        driver.findElement(By.xpath("//input[@id='i0116']")).sendKeys("long18.12.1999@email.cz");
        driver.findElement(By.xpath("//input[@id='idSIButton9']")).click();
        Thread.sleep(1500);
        driver.findElement(By.xpath("//input[@id='i0118']")).sendKeys("batman1999");
        driver.findElement(By.xpath("//input[@id='idSIButton9']")).click();
        driver.findElement(By.xpath("//input[@id='idBtn_Back']")).click();
        Thread.sleep(1500);
        // accept cookies
        driver.findElement(By.xpath("//button[@class='_1XuCi2WhiqeWRUVp3pnFG3 erL690_8JwUW-R4bJRcfl']")).click();
        Thread.sleep(1500);
        // close pop-up
        driver.findElement(By.xpath("//button[@id='obf-ToastCancel']")).click();
        Thread.sleep(1500);

        setSlider(driver, sat, sun);
    }

    private void setSlider(WebDriver driver, int sat, int sun) throws InterruptedException {
        setSliderDefault(driver, sat, sun);
    }

    private void setSliderDefault(WebDriver driver,int sat, int sun) throws InterruptedException {
        Keys movement;
        int satSteps = 0;
        int sunSteps = 0;
        boolean increase = true;
        satSteps = sat / 15;
        sunSteps = sun / 15;
        if(sat < 0 || sun < 0){increase = false;}


        // edit limits
        driver.findElement(By.xpath("/html/body/div[1]/div[3]/div/div[1]/div/div/div/div/div/div[2]/main/div[2]" +
                "/div/div[2]/div[2]/nav/div/div[2]/div/section/div/div/div/div[3]/div[2]/div/div/div/div/div" +
                "/div/div/div[1]/div/div[3]/div/div/div/div/div[2]/button")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("/html/body/div[4]/div/div/div/div/div/div/ul/li[1]/button")).click();
        // dropdown
        driver.findElement(By.xpath("//*[@id='Dropdown116-option']")).click();// select only saturday
        driver.findElement(By.xpath(this.saturday)).click();
        driver.findElement(By.xpath("//*[@id='Dropdown116-option']")).click();

        //driver.findElement(By.xpath("/html/body/div[4]/div/div/div/div[2]/div[2]/div[2]/div/div[3]/div/div[2]/button")).click();

        // slider
        WebElement sliderSat = driver.findElement(By.xpath("//*[@id='Slider117']"));
        Actions moveSat = new Actions(driver);
        moveSat.click(sliderSat).build().perform();
        Thread.sleep(1000);
        // set sat and sun to 2h
        for (int j = 0; j < 13; j++) {
            moveSat.sendKeys(Keys.ARROW_LEFT).build().perform();
            Thread.sleep(100);
        }
        // set sat time
        for (int j = 0; j < satSteps; j++) {
            movement = (increase) ? Keys.ARROW_RIGHT : Keys.ARROW_LEFT;
            moveSat.sendKeys(movement).build().perform();
            Thread.sleep(100);
        }
        Thread.sleep(1000);
        driver.findElement(By.xpath("//button[@class='ms-Button ms-Button--primary root-545']")).click();
        Thread.sleep(2000);


        // edit limits

        driver.findElement(By.xpath("/html/body/div[1]/div[3]/div/div[1]/div/div/div/div/div/div[2]/main/div[2]" +
                "/div/div[2]/div[2]/nav/div/div[2]/div/section/div/div/div/div[3]/div[2]/div/div/div/div/div" +
                "/div/div/div[1]/div/div[3]/div/div/div/div/div[2]/button")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("/html/body/div[4]/div/div/div/div/div/div/ul/li[1]/button")).click();
        // remove 7:00-22:00 limit
        driver.findElement(By.xpath("/html/body/div[4]/div/div/div/div[2]/div[2]/div[2]/div/div[3]/div/div[2]/button")).click();
        //select only sunday
        driver.findElement(By.xpath("//*[@id='Dropdown137-option']")).click();
        driver.findElement(By.xpath(this.sunday)).click();
        driver.findElement(By.xpath("//*[@id='Dropdown137-option']")).click();
        Thread.sleep(1000);

        driver.findElement(By.xpath("/html/body/div[4]/div/div/div/div[2]/div[2]/div[2]/div/div[3]/div/div[2]/button")).click();

        // slider
        WebElement sliderSun = driver.findElement(By.xpath("//*[@id='Slider138']"));
        Actions moveSun = new Actions(driver);
        moveSun.click(sliderSun).build().perform();
        Thread.sleep(1000);
        // sun to 2h
        for (int j = 0; j < 13; j++) {
            moveSun.sendKeys(Keys.ARROW_LEFT).build().perform();
            Thread.sleep(100);
        }
        // set sun time
        for (int j = 0; j < sunSteps; j++) {
            movement = (increase) ? Keys.ARROW_RIGHT : Keys.ARROW_LEFT;
            moveSun.sendKeys(movement).build().perform();
            Thread.sleep(100);
        }
        driver.findElement(By.xpath("//button[@class='ms-Button ms-Button--primary root-545']")).click();
        Thread.sleep(2000);
        driver.close();
    }
}