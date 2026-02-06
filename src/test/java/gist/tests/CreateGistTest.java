package gist.tests;

import com.opencsv.exceptions.CsvValidationException;
import gist.core.BaseTest;
import gist.pages.CreateGistPage;
import gist.pages.GlobalPage;
import gist.utils.CSVDataReader;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CreateGistTest extends BaseTest {

    @Test(dataProvider = "gistData", dataProviderClass = CSVDataReader.class)
    public void CreateSecretGistTest(String description, String filename, String content) {

        login();
        GlobalPage globalPage = new GlobalPage();
        globalPage.clickNewGistIcon();

        CreateGistPage createGistPage = new CreateGistPage();

        createGistPage.inputGistDescription(description);
        createGistPage.inputGistFilename(filename);
        createGistPage.inputGistEditor(content);
        createGistPage.clickCreateSecretGist();

        // Add verification if needed, e.g., checking the URL change or gist title
        Assert.assertTrue(
                createGistPage.isUserAvatarDisplayed(),
                "User avatar not displayed"
        );
    }
    @Test(dataProvider = "gistData", dataProviderClass = CSVDataReader.class)
    public void CreatePublicGistTest(String description, String filename, String content) {

        login();
        GlobalPage globalPage = new GlobalPage();
        globalPage.clickNewGistIcon();

        CreateGistPage createGistPage = new CreateGistPage();
        createGistPage.inputGistDescription(description);
        createGistPage.inputGistFilename(filename);
        createGistPage.inputGistEditor(content);
        createGistPage.clickGistType();
        createGistPage.clickPublicHGist();
        createGistPage.clickCreatePublicGist();

        // Add verification if needed, e.g., checking the URL change or gist title
        Assert.assertTrue(
                createGistPage.isUserAvatarDisplayed(),
                "User avatar not displayed"
        );
    }


    @Test(dataProvider = "negativeGistData", dataProviderClass = CSVDataReader.class)
    public void testCreateGistWithEmptyContent(String description, String filename, String content) {
        CreateGistPage createGistPage = new CreateGistPage();

        login();
        GlobalPage globalPage = new GlobalPage();
        globalPage.clickNewGistIcon();

        createGistPage.inputGistDescription(description);
        createGistPage.inputGistFilename(filename);
        createGistPage.inputGistEditor(content);
        createGistPage.clickCreateSecretGist();

        Assert.assertTrue(
                createGistPage.isContentEmptyAlertDisplayed(),
                "Expected empty content alert to be displayed for Input: " + description + ", but it was not."
        );
    }

}
