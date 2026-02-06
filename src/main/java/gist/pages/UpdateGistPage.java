package gist.pages;

import gist.core.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class UpdateGistPage extends BasePage {
    public UpdateGistPage() {
        super();
    }
    @FindBy(xpath = "//strong[text()='testing gist']")
    private WebElement gistFileLink;

    @FindBy(xpath="//span[@class='Button-label'][normalize-space()='Edit']")
    private WebElement editButton;

    @FindBy(xpath = "//input[@placeholder='Gist description…']")
    private WebElement gistDescriptionInput;

    @FindBy(xpath = "//input[@placeholder='Filename including extension…']")
    private WebElement gistFilenameInput;

    @FindBy(className = "CodeMirror-code")
    private WebElement codeEditor;

    @FindBy(xpath= "//button[normalize-space()='Update secret gist']")
    private WebElement updateButton;

    @FindBy(css = "a[data-hovercard-type='user']")
    private WebElement createGistAuthor;

    @FindBy(xpath = "//div[contains(@class,'js-flash-alert')]")
    private WebElement contentEmptyAlert;

    @FindBy(xpath="//span[@class='Button-label'][normalize-space()='Delete']")
    private WebElement deleteButton;

    @FindBy(xpath = "//span[@class='p-name vcard-fullname d-block overflow-hidden']")
    private WebElement getNameAuthor;


    public void clickFileLink() {
        waitForVisibility(gistFileLink);
        click(gistFileLink);
    }
    public void clickEditButton() {
        click(editButton);
    }
    public void inputGistDescription(String description) {
        waitForVisibility(gistDescriptionInput);
        clearAndType(gistDescriptionInput, description);
    }
    public void inputGistFilename(String filename) {
        clearAndType(gistFilenameInput, filename);
    }
    public void inputGistEditor(String editor) {
        click(codeEditor); // Click to focus if needed
        clearAndType(codeEditor, editor);
    }
    public void clickUpdateGistButton(){
        click(updateButton);
    }
    public boolean nameAuthorDisplayed() {
        return (createGistAuthor).isDisplayed();
    }
    public boolean isContentEmptyAlertDisplayed() {
        try {
            return contentEmptyAlert.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    public void clickDeleteButton() {
        waitForVisibility(deleteButton);
        click(deleteButton);
    }
    public void deleteGistAndConfirm() {
        waitForAlertAndAccept();
    }

    public boolean getTextNameAuthor(){
        waitForVisibility(getNameAuthor);
        return(getNameAuthor.isDisplayed());
    }


}
