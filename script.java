import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.safari.SafariDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ResolutionTesting {
    public static void main(String[] args) throws IOException {
        // List of URLs to test
        String[] urls = {
            "https://www.getcalley.com/",
            "https://www.getcalley.com/calley-lifetime-offer/",
            "https://www.getcalley.com/see-a-demo/",
            "https://www.getcalley.com/calley-teams-features/",
            "https://www.getcalley.com/calley-pro-features/"
        };

        // List of resolutions and devices
        String[][] devices = {
            {"Desktop", "1920", "1080"},
            {"Desktop", "1366", "768"},
            {"Desktop", "1536", "864"},
            {"Mobile", "360", "640"},
            {"Mobile", "414", "896"},
            {"Mobile", "375", "667"}
        };

        // List of browsers to test
        String[] browsers = {"Chrome", "Firefox", "Safari"};

        // Iterate over each browser, device, and URL to capture screenshots
        for (String browser : browsers) {
            WebDriver driver = getDriver(browser);

            for (String[] device : devices) {
                String deviceName = device[0];
                int width = Integer.parseInt(device[1]);
                int height = Integer.parseInt(device[2]);

                driver.manage().window().setSize(new Dimension(width, height));

                for (String url : urls) {
                    driver.get(url);
                    takeScreenshot(driver, browser, deviceName, width, height, url);
                }
            }
            driver.quit();
        }
    }

    // Method to initialize the WebDriver for the specified browser
    private static WebDriver getDriver(String browser) {
        switch (browser) {
            case "Chrome":
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver();
            case "Firefox":
                WebDriverManager.firefoxdriver().setup();
                return new FirefoxDriver();
            case "Safari":
                // SafariDriver does not require a setup line but requires Safari to have Remote Automation enabled
                return new SafariDriver();
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }

    // Method to take a screenshot and save it in the specified folder structure
    private static void takeScreenshot(WebDriver driver, String browser, String deviceName, int width, int height, String url) throws IOException {
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        // Create folder structure
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        Path folderPath = Path.of("screenshots", browser, deviceName, width + "x" + height);
        Files.createDirectories(folderPath);

        // Generate a file name from the URL
        String fileName = url.replaceAll("https?://www\\.|https?://", "").replaceAll("[^a-zA-Z0-9.-]", "_") + "_" + timestamp + ".png";

        // Save screenshot
        File destFile = folderPath.resolve(fileName).toFile();
        Files.copy(srcFile.toPath(), destFile.toPath());
    }
}
