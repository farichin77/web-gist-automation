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

    private static boolean isCiEnvironment() {
        String ci = System.getenv("CI");
        String gha = System.getenv("GITHUB_ACTIONS");
        return (ci != null && ci.equalsIgnoreCase("true"))
                || (gha != null && gha.equalsIgnoreCase("true"));
    }

    public static void setDriver(String browser) {
        WebDriver webDriver;
        boolean isCi = isCiEnvironment();
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-infobars");
                if (isCi) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    chromeOptions.addArguments("--window-size=1920,1080");
                }
                webDriver = new ChromeDriver(chromeOptions);
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--disable-notifications");
                if (isCi) {
                    firefoxOptions.addArguments("-headless");
                }
                webDriver = new FirefoxDriver(firefoxOptions);
                break;
            case "edge":
                try {
                    WebDriverManager.edgedriver().setup();
                    EdgeOptions edgeOptions = new EdgeOptions();
                    edgeOptions.addArguments("--disable-notifications");
                    edgeOptions.addArguments("--disable-infobars");
                    if (isCi) {
                        edgeOptions.addArguments("--headless=new");
                        edgeOptions.addArguments("--no-sandbox");
                        edgeOptions.addArguments("--disable-dev-shm-usage");
                        edgeOptions.addArguments("--window-size=1920,1080");
                    }
                    webDriver = new EdgeDriver(edgeOptions);
                } catch (Exception e) {
                    System.err.println("Edge driver failed, falling back to Chrome: " + e.getMessage());
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions fallbackChromeOptions = new ChromeOptions();
                    fallbackChromeOptions.addArguments("--remote-allow-origins=*");
                    fallbackChromeOptions.addArguments("--disable-notifications");
                    fallbackChromeOptions.addArguments("--disable-infobars");
                    if (isCi) {
                        fallbackChromeOptions.addArguments("--headless=new");
                        fallbackChromeOptions.addArguments("--no-sandbox");
                        fallbackChromeOptions.addArguments("--disable-dev-shm-usage");
                        fallbackChromeOptions.addArguments("--window-size=1920,1080");
                    }
                    webDriver = new ChromeDriver(fallbackChromeOptions);
                }
                break;
            default:
                throw new IllegalArgumentException("Browser " + browser + " not supported.");
        }
        if (!isCi) {
            webDriver.manage().window().maximize();
        }
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

