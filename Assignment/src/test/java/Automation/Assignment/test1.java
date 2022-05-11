package Automation.Assignment;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;




public class test1 {
public  WebDriver driver;
public String baseUrl="https://www.ebay.com/";
public String driverPath="C:\\Users\\hp\\Downloads\\chromedriver.exe";




@DataProvider(name = "listItem-data")
public Object[] dataprovfunc(){
	return new Object[] {"Books"};
	
}

@DataProvider(name = "searchItem-data")
public Object[][] dataprovfuncSearch(){
	return new Object[][] {{"Harry Potter and the Philosopher's Stone By J.K. Rowling.","Books"}};
	
}


@BeforeTest
public void launchBrowser() throws InterruptedException {
	System.setProperty("webdriver.chrome.driver",driverPath);
	driver = new ChromeDriver();
	driver.get(baseUrl);
	driver.manage().window().maximize();
	//Get current Url
	String currentUrl = driver.getCurrentUrl();
	driver.manage().timeouts().implicitlyWait(8000, TimeUnit.SECONDS);
	Assert.assertEquals(currentUrl, baseUrl);
	if (driver.findElement(By.xpath("//button[@id='gdpr-banner-accept']")).isDisplayed()) {
		driver.findElement(By.xpath("//button[@id='gdpr-banner-accept']")).click();
	}
	
	
	
	
}

//TC002
//Verify Register and Sign in links are available on Homepage and Total links in footer section of ebay site
@Test
public void homePageLinkTextValidtion() {
	
	String signText=driver.findElement(By.xpath("//span[@id='gh-ug']//a[contains(text(),'Sign in')]")).getText();
	//System.out.println(signText);
	Assert.assertEquals(signText, "Sign in", "Sign in link exist'");
	Assert.assertEquals(driver.findElement(By.linkText("register")).getText(), "register");	
	WebElement footer = driver.findElement(By.id("glbfooter"));
	int TotalLinkFooter = footer.findElements(By.tagName("a")).size();
	Assert.assertEquals(TotalLinkFooter, 95);
	
}

//TC003
//Verify Total no of categories in category dropdown and check if "Books" category is present in the dropdown
@Test(dataProvider="listItem-data")
public void DropdownItemValidation(String itemExp) {
	boolean found= false;
	WebElement dropElement = driver.findElement(By.id("gh-cat"));
	Select dropdown = new Select(dropElement);
	List<WebElement> AllCategoryitems = dropdown.getOptions();
	Assert.assertEquals(AllCategoryitems.size(), 36);
	for( int i=0;i<AllCategoryitems.size();i++) {
		if (AllCategoryitems.get(i).getText().contains(itemExp)) {
			found =true;
			break;
		}
		
	}
	if (found) {
		Assert.assertTrue(true);
	}
	else {
		Assert.assertTrue(false);
	}	
		
		
		
}

//TC005 and TC006
//Verify search functionality and add to cart 
@Test(dataProvider = "searchItem-data")
public void searchProduct(String product, String category){
	
	driver.findElement(By.id("gh-ac")).sendKeys(product);
	Assert.assertTrue(true, "product data entered");
	WebElement dropElement = driver.findElement(By.id("gh-cat"));
	Select dropdown = new Select(dropElement);
	dropdown.selectByVisibleText(category);
	Assert.assertTrue(true, "category entered");
	driver.findElement(By.id("gh-btn")).click();
	Assert.assertTrue(true, "Search button clicked");
	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	String result=driver.findElement(By.xpath("//h1[@class='srp-controls__count-heading']/span[1]")).getText();
    //System.out.println("Result is : "+ result);
    Assert.assertTrue(true, "Search results populated");
    driver.findElement(By.xpath("//div[@id='srp-river-results']//li[1]//div[1]//div[2]//a[1]//h3[1]")).click();
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    Set <String> windows = driver.getWindowHandles();
    Iterator <String> it = windows.iterator();
    String parentWindowID = it.next();
    String childWindowID = it.next();
    driver.switchTo().window(childWindowID);
    driver.findElement(By.cssSelector("#isCartBtn_btn")).click();
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    String AddedItem = driver.findElement(By.cssSelector(".listsummary-content-itemdetails")).getText().split("\\n")[0];
    Assert.assertTrue(AddedItem.matches("Harry.*"));
    Assert.assertTrue(true, "Item added to cart");
    driver.switchTo().window(parentWindowID);
    }
    
   
@ AfterTest
public void ClearAll() {
	 
	 System.out.println("Browser about to Close");
	 System.out.println("...");
	 
	 //Close the Browser
	 driver.quit();
	 
	 System.out.println("Browser Closed");
}


    
}

 
	

