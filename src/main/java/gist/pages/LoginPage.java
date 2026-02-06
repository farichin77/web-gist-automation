package gist.pages;

import gist.core.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;

public class LoginPage extends BasePage {
    public LoginPage() {
        super();
    }

    @FindBy(xpath = "//a[contains(@href, '/auth/github')]")
    private List<WebElement> signInLinks;

    @FindBy(id = "login_field")
    private WebElement usernameInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(name = "commit")
    private WebElement signInButton;

    @FindBy(css = ".flash.flash-error")
    private WebElement errorMessage;

    public boolean isErrorDisplayed() {
        return isDisplayed(errorMessage);
    }
    
    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public void clickSignIn(){
        for (WebElement link : signInLinks) {
            try {
                if (link.isDisplayed()) {
                    link.click();
                    return;
                }
            } catch (Exception e) {
                // Ignore and try next
            }
        }
        throw new RuntimeException("No visible 'Sign in' link found.");
    }
    public void inputUsername(String username){
        type(usernameInput, username);
    }
    public void inputPassword(String password){
        type(passwordInput, password);
    }
    public void clickSubmit(){
        click(signInButton);
    }

    public boolean isSignInDisplayed() {
        for (WebElement link : signInLinks) {
             if (isDisplayed(link)) {
                 return true;
             }
        }
        return false;
    }
}
