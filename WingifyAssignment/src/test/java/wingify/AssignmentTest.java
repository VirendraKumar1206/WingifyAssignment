package wingify;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class AssignmentTest {
	WebDriver driver;
	ExtentReports report;
	ExtentTest test;

	@BeforeClass
	public void ExtentReport() {
		report = new ExtentReports(System.getProperty("user.dir") + "/extentreport.html");
		test = report.startTest("emailableReport");

	}

	@BeforeMethod
	public void setup() {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\HP\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
		driver = new ChromeDriver();
		test.log(LogStatus.PASS, "chromeBrowser opened successfully");
		driver.get("https://sakshingp.github.io/assignment/login.html");
		test.log(LogStatus.PASS, "successfully user is redirected to mentioned web address");
		driver.manage().window().maximize();
		test.log(LogStatus.PASS, "size of webpage successfully encreased when url is successfully loaded");
	}

	@Test(enabled = true)
	public void loginAndSortTransactions() {
		// Login
		driver.findElement(By.id("username")).sendKeys("Virendra1213");
		test.log(LogStatus.PASS, "username text box is editable and username successfully filled ");
		
		driver.findElement(By.id("password")).sendKeys("Vire@31213");
		test.log(LogStatus.PASS, "password text box is editable and password successfully filled ");
		
		driver.findElement(By.xpath("//input[@type='checkbox']")).click();
		test.log(LogStatus.PASS, "checkbox is clickable");
		
		driver.findElement(By.id("log-in")).click();
		test.log(LogStatus.PASS, "click button is clickable");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		test.log(LogStatus.PASS, "wait is working fine");
		
	
		
		WebElement amountHeader = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//th[@id='amount']")));
		test.log(LogStatus.PASS, "Waiting for the 'amountHeader' to be clickable.");

		
		amountHeader.click();
		test.log(LogStatus.PASS, "Clicked on 'amountHeader' to sort the amounts in ascending order.");

		
		List<Double> amounts = retrieveAmounts();
		Assert.assertTrue(isSortedAscending(amounts), "The amounts are not sorted in ascending order.");
		test.log(LogStatus.PASS, "Verified that the amounts are sorted in ascending order.");

		
		amountHeader.click();
		test.log(LogStatus.PASS, "Clicked on 'amountHeader' again to sort the amounts in descending order.");

		
		amounts = retrieveAmounts();
		Assert.assertTrue(isSortedDescending(amounts), "The amounts are not sorted in descending order.");
		test.log(LogStatus.PASS, "Verified that the amounts are sorted in descending order.");

		
	}

	private List<Double> retrieveAmounts() {
		 test.log(LogStatus.INFO, "we are retrieving amounts from the transactions table.");
		List<WebElement> rows = driver.findElements(By.xpath("//table[@id='transactionsTable']/tbody/tr"));
		List<Double> amounts = new ArrayList<>();
		for (WebElement row : rows) {
			String amountText = row.findElement(By.xpath("./td[5]")).getText();
			double amount = Double.parseDouble(amountText.replaceAll("[^0-9.-]", ""));
			
			//replaceAll("[^0-9.-]", "") uses a regular expression to remove all characters from amountText except digits (0-9), the decimal point (.), and the minus sign (-).
			//Double.parseDouble(): Converts the cleaned string (e.g., "1250.00" or "-320.00") into a double.
			
			amounts.add(amount);
		}
		 test.log(LogStatus.PASS, "Successfully retrieved amounts from the transactions table.");
		return amounts;
	}

	private boolean isSortedAscending(List<Double> amounts) {
		test.log(LogStatus.INFO, "we are verifying if amounts are sorted in ascending order.");
		for (int i = 0; i < amounts.size() - 1; i++) {
			if (amounts.get(i) > amounts.get(i + 1)) {
				  test.log(LogStatus.FAIL, "Amounts are not sorted in ascending order.");
				return false;
			}
		}
		test.log(LogStatus.PASS, "Amounts are sorted in ascending order.");
		return true;
	}

	private boolean isSortedDescending(List<Double> amounts) {
		 test.log(LogStatus.INFO, "we are verifying if amounts are sorted in descending order.");
		for (int i = 0; i < amounts.size() - 1; i++) {
			if (amounts.get(i) < amounts.get(i + 1)) {
				test.log(LogStatus.FAIL, "Amounts are not sorted in descending order.");
				return false;
			}
		}
		test.log(LogStatus.PASS, "Amounts are sorted in descending order.");
		return true;
	}

	@AfterMethod
	public void tearDown() throws InterruptedException {
		Thread.sleep(3000);
		driver.quit();
		test.log(LogStatus.PASS, "here we are closing the driver");
	}

	@AfterClass
	public void EndReporting() {
		report.endTest(test);
		test.log(LogStatus.PASS, "here we are stoping the reporting");
		report.flush();
		test.log(LogStatus.PASS, "here we are deleting exiting report");
	}
}
