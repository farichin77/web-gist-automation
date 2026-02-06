package gist.tests;

import gist.core.BaseTest;
import gist.pages.GlobalPage;
import gist.pages.UpdateGistPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DeleteGistTest extends BaseTest {

    @Test
    public void deleteGistTest() {
        login();
        GlobalPage globalPage = new GlobalPage();

        globalPage.clickProfileIcon();
        globalPage.clickYourGistsMenu();

        UpdateGistPage updateGistPage = new UpdateGistPage();
        updateGistPage.clickFileLink();
        updateGistPage.clickDeleteButton();
        updateGistPage.deleteGistAndConfirm();

        Assert.assertTrue( updateGistPage.getTextNameAuthor(),
                "Author name not visible");
    }
}