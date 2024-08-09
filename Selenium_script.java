import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FunctionalFlowTest {
    public static void main(String[] args) {
        // Set up WebDriver for Chrome
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            // Start recording (Assuming an external script starts ffmpeg recording here)
            startRecording();

            // Open the web application
            driver.get("https://demo.dealsdray.com/");

            // Log in to the application
            WebElement usernameField = driver.findElement(By.id("username")); // Replace with the actual id
            WebElement passwordField = driver.findElement(By.id("password")); // Replace with the actual id
            WebElement loginButton = driver.findElement(By.id("login")); // Replace with the actual id

            usernameField.sendKeys("prexo.mis@dealsdray.com");
            passwordField.sendKeys("prexo.mis@dealsdray.com");
            loginButton.click();

            // Wait for login to complete and navigate to the file upload section
            Thread.sleep(5000); // Adjust wait time as necessary

            // Upload the XLS file
            WebElement uploadElement = driver.findElement(By.id("fileUpload")); // Replace with the actual id
            uploadElement.sendKeys("/path/to/demo-data.xlsx"); // Use the actual path to the file

            // Validate the upload and process
            WebElement uploadButton = driver.findElement(By.id("uploadButton")); // Replace with the actual id
            uploadButton.click();

            // Wait for the processing to complete
            Thread.sleep(5000); // Adjust wait time as necessary

            // Take a screenshot of the final output page
            takeScreenshot(driver, "final_output");

            // Perform any necessary validations on the page

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            // Stop recording (Assuming an external script stops ffmpeg recording here)
            stopRecording();

            // Close the browser
            driver.quit();
        }
    }

    private static void startRecording() {
        // Code to start screen recording (can be a script call or ffmpeg command)
        // Example: Runtime.getRuntime().exec("path/to/start-recording-script.sh");
    }

    private static void stopRecording() {
        // Code to stop screen recording (can be a script call or ffmpeg command)
        // Example: Runtime.getRuntime().exec("path/to/stop-recording-script.sh");
    }

    private static void takeScreenshot(WebDriver driver, String fileName) throws IOException {
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        // Create folder structure
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        Path folderPath = Path.of("screenshots");
        Files.createDirectories(folderPath);

        // Save screenshot
        File destFile = folderPath.resolve(fileName + "_" + timestamp + ".png").toFile();
        Files.copy(srcFile.toPath(), destFile.toPath());
    }
}
