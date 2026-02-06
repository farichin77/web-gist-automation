package gist.pages;

import gist.core.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AllListGistPage extends BasePage {
    public AllListGistPage() {
        super();
    }
    @FindBy(xpath= "//button[.//span[normalize-space()='All']]")
    private WebElement allListGistButton;

    @FindBy(xpath= "//span[normalize-space()='Public']")
    private WebElement publicListGistButton;

    @FindBy(xpath ="//span[normalize-space()='Secret']")
    private WebElement secretListGistButton;

    @FindBy(xpath="//span[normalize-space()='Secret' and contains(@class,'Label')]")
    private WebElement getSecretText;

    @FindBy(xpath = "//span[@class='p-name vcard-fullname d-block overflow-hidden']")
    private WebElement getNameAuthor;


    public void clickAllTypeGist(){
        click(allListGistButton);
    }

    public void clickPublic(){
        click(publicListGistButton);
    }
    public void clickSecret(){
        click(secretListGistButton);
    }

    public boolean getSecretText(){
        return(getSecretText).isDisplayed();
    }
    public boolean getTextNameAuthor(){
        waitForVisibility(getNameAuthor);
        return(getNameAuthor.isDisplayed());
    }
}
