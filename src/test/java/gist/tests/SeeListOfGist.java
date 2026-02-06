package gist.tests;

import gist.core.BaseTest;
import gist.pages.AllListGistPage;
import gist.pages.GlobalPage;
import org.testng.Assert;
import org.testng.annotations.Test;


public class SeeListOfGist extends BaseTest {
    @Test
    public void SeelistSecretTest(){
        login();
        GlobalPage globalPage = new GlobalPage();

        globalPage.clickProfileIcon();
        globalPage.clickYourGistsMenu();

        AllListGistPage allListGistPage = new AllListGistPage();
        allListGistPage.clickAllTypeGist();
        allListGistPage.clickSecret();

        Assert.assertTrue(allListGistPage.getSecretText(),
                "secret not displayed");
    }
    @Test
    public void SeelistPublicTest(){
        login();
        GlobalPage globalPage = new GlobalPage();

        globalPage.clickProfileIcon();
        globalPage.clickYourGistsMenu();

        AllListGistPage allListGistPage = new AllListGistPage();
        allListGistPage.clickAllTypeGist();
        allListGistPage.clickPublic();

        Assert.assertTrue(allListGistPage.getTextNameAuthor(),
                "secret not displayed");
    }
}
