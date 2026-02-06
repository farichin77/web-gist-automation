package gist.core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverManager {
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static void setDriver(String browser) {
        WebDriver webDriver;
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-infobars");
                webDriver = new ChromeDriver(chromeOptions);
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--disable-notifications");
                webDriver = new FirefoxDriver(firefoxOptions);
                break;
            case "edge":
                try {
                    WebDriverManager.edgedriver().setup();
                    EdgeOptions edgeOptions = new EdgeOptions();
                    edgeOptions.addArguments("--disable-notifications");
                    edgeOptions.addArguments("--disable-infobars");
                    webDriver = new EdgeDriver(edgeOptions);
                } catch (Exception e) {
                    System.err.println("Edge driver failed, falling back to Chrome: " + e.getMessage());
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions fallbackChromeOptions = new ChromeOptions();
                    fallbackChromeOptions.addArguments("--remote-allow-origins=*");
                    fallbackChromeOptions.addArguments("--disable-notifications");
                    fallbackChromeOptions.addArguments("--disable-infobars");
                    webDriver = new ChromeDriver(fallbackChromeOptions);
                }
                break;
            default:
                throw new IllegalArgumentException("Browser " + browser + " not supported.");
        }
        webDriver.manage().window().maximize();
        driver.set(webDriver);
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}

