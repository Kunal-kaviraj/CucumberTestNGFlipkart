package stepDefinitions;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class StepDefinition
{
	public WebDriver driver;
	
	FileWriter writer;
	String header = "Device_Details,Price,Ratings";
	String delimiter = "," ;
	String newline = "\n" ;

	@Given("^user is already on Login Page$")
	public void user_already_on_login_page()
	{
		System.setProperty("webdriver.chrome.driver", "Driver/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.get("https://www.flipkart.com");
	}

	@When("^user enters iphone in the search box and presses enter$")
	public void Iphone_Search() throws Exception
	{
		driver.findElement(By.xpath("//button[@class='_2AkmmA _29YdH8']")).click();
		driver.findElement(By.xpath("//input[@class=\"LM6RPg\"]")).sendKeys("iphone");
		driver.findElement(By.xpath("//input[@class=\"LM6RPg\"]")).sendKeys(Keys.ENTER);
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[text()='Price -- Low to High']")).click();
		Thread.sleep(2000);
	}

	@Then("^user creates a file$")
	public void user_creates_a_file() throws IOException
	{
		writer = new FileWriter("./demo.csv");	
	    writer.append(header);	   
	}

	@And("^user stores details of all the iphones having max price 40000$")
	public void user_stores_the_iphone_details_in_a_file() throws IOException, InterruptedException
	{
		List<WebElement> F = driver.findElements(By.xpath("//a[@class=\"_2Xp0TH\"]"));
		for (int j = 0; j < F.size()-1; j++)
		{
			List<WebElement> L1 = driver.findElements(By.xpath("//div[@class=\"_3wU53n\"]"));
			List<WebElement> L2 = driver.findElements(By.xpath("//div[@class=\"_1vC4OE _2rQ-NK\"]"));
			List<WebElement> L3 = driver.findElements(By.xpath("//span[@class=\"_38sUEc\"]"));
			for (int i = 0; i < L1.size(); i++)
			{
				String amt = L2.get(i).getText();
				String price = amt.replaceAll("[^a-zA-Z0-9]", "");
				String model = L1.get(i).getText().trim();
				String review = L3.get(i).getText();
				String rating1[] = review.split("&");
				String rating = rating1[0].replaceAll("[^0-9]", "").trim();
				if (Integer.parseInt(price) <= 40000)
				{
					writer.append(newline);
					writer.append(model);
					writer.append(delimiter);
					writer.append(amt);
					writer.append(delimiter);
					writer.append(rating);
					OutputStreamWriter stdOut = new OutputStreamWriter(System.out,"UTF8");
					System.out.println(L1.get(i).getText() + "," + L2.get(i).getText() + "," + L3.get(i).getText());
				}
			}
			driver.findElement(By.xpath("//*[@class='_3fVaIS']/*[text()='Next']")).click();
			Thread.sleep(2000);
		}
	}


	@Then("^Close the browser$")
	public void close_the_browser() throws IOException
	{
		writer.flush();
		writer.close();
		driver.quit();
	}
	
	@After
	public void teardown() throws IOException 
	{
		try
		{
			writer.close();
			driver.quit();
			Runtime rs =  Runtime.getRuntime();
		    System.out.println("Free memory in JVM before Garbage Collection = "+rs.freeMemory());
		    rs.gc();
		    System.out.println("Free memory in JVM after Garbage Collection = "+rs.freeMemory());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

}
