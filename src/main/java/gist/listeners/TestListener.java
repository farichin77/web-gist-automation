package gist.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import gist.core.DriverManager;
import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

public class TestListener implements ITestListener {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static volatile boolean cleanupDone = false;
    private static volatile boolean reportInitialized = false;
    private static final Object lock = new Object();

    @Override
    public void onStart(ITestContext context) {
        // Reset static variables at the start of each test context
        synchronized (lock) {
            // Always reset for fresh start
            if (extent != null) {
                extent.flush();
            }
            extent = null;
            reportInitialized = false;
            test.remove();
            
            // Initialize fresh report
            if (!reportInitialized) {
                // Clean up old reports only once per session
                if (!cleanupDone) {
                    cleanupOldReports();
                    cleanupDone = true;
                }
                
                String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
                String reportPath = System.getProperty("user.dir") + "/test-reports/ExtentReport_" + timestamp + ".html";
                
                ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
                sparkReporter.config().setDocumentTitle("Gist Automation Report - Multi-Browser");
                sparkReporter.config().setReportName("Test Execution Report - All Browsers");
                sparkReporter.config().setTheme(Theme.STANDARD);
                
                // Configure screenshot directory
                String screenshotDir = System.getProperty("user.dir") + "/test-reports/screenshots/";
                sparkReporter.config().setCss(".screenshot { max-width: 800px; }");
                
                extent = new ExtentReports();
                extent.attachReporter(sparkReporter);
                extent.setSystemInfo("OS", System.getProperty("os.name"));
                extent.setSystemInfo("Java Version", System.getProperty("java.version"));
                extent.setSystemInfo("User", System.getProperty("user.name"));
                extent.setSystemInfo("Test Context", context.getName());
                
                reportInitialized = true;
                System.out.println("ExtentReports initialized: " + reportPath);
            }
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        if (extent == null) {
            System.err.println("ExtentReports not initialized!");
            return;
        }
        
        String browser = result.getTestContext().getCurrentXmlTest().getParameter("browser");
        String testName = result.getMethod().getMethodName() + " [" + (browser != null ? browser.toUpperCase() : "Unknown") + "]";
        ExtentTest extentTest = extent.createTest(testName);
        test.set(extentTest);
        test.get().log(Status.INFO, "Test started: " + testName);
        System.out.println("Test started: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (test.get() == null) {
            System.err.println("Test not initialized for: " + result.getMethod().getMethodName());
            return;
        }
        
        String browser = result.getTestContext().getCurrentXmlTest().getParameter("browser");
        String testName = result.getMethod().getMethodName() + " [" + (browser != null ? browser.toUpperCase() : "Unknown") + "]";
        test.get().log(Status.PASS, "Test passed: " + testName);
        System.out.println("Test passed: " + testName);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        if (test.get() == null) {
            System.err.println("Test not initialized for: " + result.getMethod().getMethodName());
            return;
        }
        
        String browser = result.getTestContext().getCurrentXmlTest().getParameter("browser");
        String testName = result.getMethod().getMethodName() + " [" + (browser != null ? browser.toUpperCase() : "Unknown") + "]";
        
        // Log test failure
        test.get().log(Status.FAIL, "Test failed: " + testName);
        
        // Log failure reason with stack trace
        if (result.getThrowable() != null) {
            String failureReason = result.getThrowable().getMessage();
            test.get().log(Status.FAIL, "Failure Reason: " + failureReason);
            
            // Capture and embed screenshot directly after failure reason
            try {
                WebDriver driver = DriverManager.getDriver();
                if (driver != null) {
                    String base64Screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
                    if (base64Screenshot != null && !base64Screenshot.isEmpty()) {
                        // Embed screenshot directly as HTML
                        String embeddedImage = "<br/><div style='margin: 10px 0;'><img src='data:image/png;base64," + base64Screenshot + "' style='max-width: 100%; height: auto; border: 1px solid #ccc; border-radius: 4px;' alt='Failure Screenshot'/></div>";
                        test.get().log(Status.FAIL, embeddedImage);
                        System.out.println("Screenshot embedded directly after failure reason");
                    }
                }
            } catch (Exception e) {
                System.err.println("Failed to embed screenshot: " + e.getMessage());
            }
            
            test.get().log(Status.FAIL, "Stack Trace: <pre>" + getStackTrace(result.getThrowable()) + "</pre>");
        }
        
        System.out.println("Test failed: " + testName);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (test.get() == null) {
            System.err.println("Test not initialized for: " + result.getMethod().getMethodName());
            return;
        }
        
        String browser = result.getTestContext().getCurrentXmlTest().getParameter("browser");
        String testName = result.getMethod().getMethodName() + " [" + (browser != null ? browser.toUpperCase() : "Unknown") + "]";
        
        test.get().log(Status.SKIP, "Test skipped: " + testName);
        
        if (result.getThrowable() != null) {
            test.get().log(Status.SKIP, "Skip Reason: " + result.getThrowable().getMessage());
        }
        
        // Try to capture screenshot for skipped tests too
        try {
            WebDriver driver = DriverManager.getDriver();
            if (driver != null) {
                String base64Screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
                
                if (base64Screenshot != null && !base64Screenshot.isEmpty()) {
                    try {
                        test.get().addScreenCaptureFromBase64String(base64Screenshot, "Skip Screenshot");
                        test.get().log(Status.SKIP, "<img src='data:image/png;base64," + base64Screenshot + "' width='800' height='450' alt='Skip Screenshot'/>");
                        test.get().log(Status.INFO, "Screenshot captured on skip (base64)");
                        System.out.println("Screenshot added to report using base64 encoding (skipped test)");
                    } catch (Exception e) {
                        System.err.println("Base64 screenshot failed for skip, using file path: " + e.getMessage());
                        String screenshotPath = captureScreenshot(result.getMethod().getMethodName(), browser);
                        if (screenshotPath != null) {
                            addScreenshotFromFile(screenshotPath);
                            String relativePath = screenshotPath.replace(System.getProperty("user.dir"), "").replace("\\", "/");
                            if (relativePath.startsWith("/")) {
                                relativePath = relativePath.substring(1);
                            }
                            test.get().log(Status.SKIP, "<img src='" + relativePath + "' width='800' height='450' alt='Skip Screenshot'/>");
                        }
                    }
                }
            }
        } catch (Exception e) {
            test.get().log(Status.WARNING, "Failed to capture screenshot on skip: " + e.getMessage());
        }
        
        System.out.println("Test skipped: " + testName);
    }

    @Override
    public void onFinish(ITestContext context) {
        // Only flush if extent is initialized
        if (extent != null) {
            synchronized (lock) {
                if (extent != null) {
                    extent.flush();
                    System.out.println("ExtentReports flushed for: " + context.getName());
                    
                    // Add shutdown hook to ensure report is generated even if JVM exits
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        if (extent != null) {
                            extent.flush();
                            System.out.println("ExtentReports flushed on shutdown");
                        }
                    }));
                }
            }
        }
    }
    
    // Method to manually reset the reporter (useful for running individual classes)
    public static void resetReporter() {
        synchronized (lock) {
            if (extent != null) {
                extent.flush();
            }
            extent = null;
            reportInitialized = false;
            cleanupDone = false;
            test.remove();
            System.out.println("ExtentReports manually reset");
        }
    }
    
    // Method to force cleanup of old reports
    public static void forceCleanup() {
        synchronized (lock) {
            cleanupDone = false;
            cleanupOldReports();
            cleanupDone = true;
            System.out.println("Force cleanup completed");
        }
    }

    private String captureScreenshotAsBase64() {
        WebDriver driver = DriverManager.getDriver();
        if (driver == null) {
            System.err.println("Driver is null, cannot capture base64 screenshot");
            return null;
        }

        try {
            String base64Screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
            System.out.println("Base64 screenshot captured successfully");
            System.out.println("Base64 starts with: " + base64Screenshot.substring(0, Math.min(50, base64Screenshot.length())) + "...");
            return base64Screenshot;
        } catch (Exception e) {
            System.err.println("Failed to capture base64 screenshot: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    private void addScreenshotFromFile(String screenshotPath) {
        try {
            // Convert to relative path for Extent Report
            String relativePath = screenshotPath.replace(System.getProperty("user.dir"), "").replace("\\", "/");
            if (relativePath.startsWith("/")) {
                relativePath = relativePath.substring(1);
            }
            
            System.out.println("Adding screenshot from file: " + relativePath);
            test.get().addScreenCaptureFromPath(relativePath);
            test.get().log(Status.INFO, "Screenshot captured: " + relativePath);
            System.out.println("Screenshot added to report using file path");
        } catch (Exception e) {
            System.err.println("Failed to add screenshot from file: " + e.getMessage());
            test.get().log(Status.WARNING, "Failed to attach screenshot: " + e.getMessage());
        }
    }
    
    private String captureScreenshot(String testName, String browser) throws IOException {
        WebDriver driver = DriverManager.getDriver();
        if (driver == null) {
            System.err.println("Driver is null, cannot capture screenshot");
            return null;
        }

        try {
            // Create screenshots directory if it doesn't exist
            String screenshotDir = System.getProperty("user.dir") + "/test-reports/screenshots/";
            Path path = Paths.get(screenshotDir);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                System.out.println("Created screenshot directory: " + screenshotDir);
            }

            // Capture screenshot
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String browserInfo = browser != null ? browser.toLowerCase() : "unknown";
            String screenshotPath = screenshotDir + testName + "_" + browserInfo + "_" + timestamp + ".png";
            
            // Copy the screenshot file
            Files.copy(screenshot.toPath(), Paths.get(screenshotPath));
            System.out.println("Screenshot saved: " + screenshotPath);
            
            // Verify file exists
            if (Files.exists(Paths.get(screenshotPath))) {
                System.out.println("Screenshot file verified: " + screenshotPath);
                return screenshotPath;
            } else {
                System.err.println("Screenshot file not found after save: " + screenshotPath);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    private String getStackTrace(Throwable throwable) {
        if (throwable == null) return "No exception details available";
        
        java.io.StringWriter sw = new java.io.StringWriter();
        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString().replace(System.getProperty("line.separator"), "<br/>");
    }
    
    private static void cleanupOldReports() {
        try {
            String reportsDir = System.getProperty("user.dir") + "/test-reports";
            Path reportsPath = Paths.get(reportsDir);
            
            if (Files.exists(reportsPath)) {
                try (Stream<Path> paths = Files.walk(reportsPath, 1)) {
                    paths.filter(path -> !path.equals(reportsPath))
                         .filter(path -> {
                             String fileName = path.getFileName().toString();
                             // Only delete HTML reports, keep screenshots directory
                             return fileName.endsWith(".html");
                         })
                         .forEach(path -> {
                             try {
                                 Files.delete(path);
                                 System.out.println("Deleted old report: " + path.getFileName());
                             } catch (IOException e) {
                                 System.err.println("Failed to delete " + path.getFileName() + ": " + e.getMessage());
                             }
                         });
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to cleanup old reports: " + e.getMessage());
        }
    }
}
