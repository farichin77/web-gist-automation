package gist.pages;

import gist.core.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CreateGistPage extends BasePage {
    public CreateGistPage() {
        super();
    }

    @FindBy(name = "gist[description]")
    private WebElement gistDescriptionInput;

    @FindBy(name = "gist[contents][][name]")
    private WebElement gistFilenameInput;

    @FindBy(className = "CodeMirror-code")
    private WebElement codeEditor;

    @FindBy(xpath="//summary[@aria-label='Select a type of Gist']")
    private WebElement gistTypeSelector;

    @FindBy(xpath="//span[@class='select-menu-item-heading'][normalize-space()='Create public gist']")
    private WebElement selectPublicGist;

    @FindBy(xpath = "//button[@type='submit' and normalize-space()='Create public gist']")
    private WebElement createPublicGistButton;

    @FindBy(xpath = "//button[@type='submit' and normalize-space()='Create secret gist']")
    private WebElement createSecretGistButton;


    @FindBy(css = "a[data-hovercard-type='user']")
    private WebElement createGistAuthor;

    @FindBy(xpath = "//div[contains(@class,'js-flash-alert')]")
    private WebElement contentEmptyAlert;


    public void inputGistDescription(String description) {
        type(gistDescriptionInput, description);
    }
    public void inputGistFilename(String filename) {
        type(gistFilenameInput, filename);
    }
    public void inputGistEditor(String editor) {
        click(codeEditor); // Click to focus if needed
        type(codeEditor, editor);
    }
    public void clickGistType(){
        click(gistTypeSelector);
    }
    public void clickPublicHGist(){
        click(selectPublicGist);
    }
    public void clickCreatePublicGist(){
        click(createPublicGistButton);
    }

    public void clickCreateSecretGist(){
        click(createSecretGistButton);
    }
    public boolean isUserAvatarDisplayed() {
        return (createGistAuthor).isDisplayed();
    }
    public boolean isContentEmptyAlertDisplayed() {
        try {
            return contentEmptyAlert.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

}
