package gist.core;

import gist.pages.LoginPage;
import io.github.cdimascio.dotenv.Dotenv;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BaseTest {

    protected Dotenv dotenv = Dotenv.load();

    @BeforeMethod
    @Parameters("browser")
    public void setUp(@Optional("chrome") String browser) {
        System.out.println("Starting test with browser: " + browser);
        DriverManager.setDriver(browser);
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
    }

    protected void login() {
        LoginPage loginPage = new LoginPage();
        loginPage.open();
        loginPage.clickSignIn();
        loginPage.inputUsername(System.getenv("TEST_USERNAME"));
        loginPage.inputPassword(System.getenv("TEST_PASSWORD"));
        loginPage.clickSubmit();

        // Check for login errors
        if (loginPage.isErrorDisplayed()) {
            throw new RuntimeException("Login failed with error: " + loginPage.getErrorMessage());
        }

        // Check for 2FA
        if (DriverManager.getDriver().getCurrentUrl().contains("two-factor")) {
            throw new RuntimeException("Login failed: GitHub is requesting Two-Factor Authentication (2FA). Automation cannot bypass this without handling OTP.");
        }
        
    }

}
