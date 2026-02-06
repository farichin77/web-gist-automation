package gist.tests;

import gist.core.BaseTest;
import gist.pages.GlobalPage;
import gist.pages.UpdateGistPage;
import gist.utils.CSVDataReader;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UpdateGistTest extends BaseTest {

    @Test(dataProvider = "updateGistData", dataProviderClass = CSVDataReader.class)
    public void updateGistTest(String originalDescription, String originalFilename, String originalContent,
                              String newDescription, String newFilename, String newContent) {
        
        login();
        GlobalPage globalPage = new GlobalPage();
        
        // Navigate to gists and find the gist to update
        globalPage.clickProfileIcon();
        globalPage.clickYourGistsMenu();

        UpdateGistPage updateGistPage = new UpdateGistPage();
        updateGistPage.clickFileLink();
        updateGistPage.clickEditButton();
        updateGistPage.inputGistDescription(newDescription);
        updateGistPage.inputGistFilename(newFilename);
        updateGistPage.inputGistEditor(newContent);
        updateGistPage.clickUpdateGistButton();

        // Verify the update was successful
        Assert.assertTrue(updateGistPage.nameAuthorDisplayed(),
                "Gist update failed - user avatar not displayed");
    }
    
    @Test(dataProvider = "updateGistNegativeData", dataProviderClass = CSVDataReader.class)
    public void updateGistNegativeTest(String originalDescription, String originalFilename, String originalContent,
                                       String newDescription, String newFilename, String newContent, String expectedError) {
        
        login();
        GlobalPage globalPage = new GlobalPage();
        
        // Navigate to gists and find the gist to update
        globalPage.clickProfileIcon();
        globalPage.clickYourGistsMenu();

        UpdateGistPage updateGistPage = new UpdateGistPage();
        updateGistPage.clickFileLink();
        updateGistPage.clickEditButton();
        
        // Update the gist fields with invalid data
        updateGistPage.inputGistDescription(newDescription);
        updateGistPage.inputGistFilename(newFilename);
        updateGistPage.inputGistEditor(newContent);
        
        // Try to save changes
        updateGistPage.clickUpdateGistButton();
        
        // Verify the expected error occurs
        Assert.assertTrue(
                updateGistPage.isContentEmptyAlertDisplayed(),
                "Expected error for " + expectedError + " but it was not displayed"
        );
    }
}
