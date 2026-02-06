package gist.pages;

import gist.core.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class GlobalPage extends BasePage {
    public GlobalPage() {
        super();
    }
    @FindBy(xpath="//span[@class='Button-label']//img[@alt='@farichin77']")
    private WebElement profileIcon;

    @FindBy(xpath = "//*[name()='path' and contains(@d,'M7.75 2a.7')]")
    private WebElement createNewGistIcon;

    @FindBy(xpath = "//span[normalize-space()='Your gists']")
    private WebElement yourGistsMenu;
    @FindBy(xpath = "//span[normalize-space()='Create a gist']")
    private WebElement createGistBtn;


    public void clickProfileIcon(){
        click(profileIcon);
    }
    public void clickNewGistIcon(){
        click(createNewGistIcon);
    }
    public void clickYourGistsMenu(){
        waitForVisibility(yourGistsMenu);
        click(yourGistsMenu);
    }

}
