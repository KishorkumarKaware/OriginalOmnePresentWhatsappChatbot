import java.awt.AWTException;
import java.awt.Robot;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

//get the contacts name from excel file and send massage to them
public class WhatsAppSendHi {

	
	public static void main(String[] args) throws InterruptedException, AWTException, IOException {
		//Contacts Name Excel file
		FileInputStream contactsList = new FileInputStream("D:/ContactList.xls");
		//Workbook Object
		Workbook contactsWorkbook= new HSSFWorkbook(contactsList);
		//excel sheet
		HSSFSheet contactsListSheet= (HSSFSheet)contactsWorkbook.getSheet("Sheet1");
		//contacts name arraylist
		ArrayList<String> contactsNameList= new ArrayList<String>();
	
		//getting excel data(contacts name) into array list
		for(int r1=1;r1<=contactsListSheet.getLastRowNum();r1++){
			Row row=contactsListSheet.getRow(r1);
			Cell name=row.getCell(0);
			String contactName=name.toString();
			contactsNameList.add(contactName);
		}
		//array of string
		String[] array=new String[contactsNameList.size()];
		int num=0;
		//store arraylist data into array
		for(String name:contactsNameList){
			array[num]=name;
			num++;
			System.out.println(array);
		}
		
		//System.setProperty("webdriver.chrome.driver", "D:/WhatsAppProject/InputFile/chromedriver_win32/chromedriver.exe");
		//get driver
		WebDriver driver=new FirefoxDriver();
		Robot robot=new Robot();
		//load url
		driver.get("https://web.whatsapp.com/");
		//maximize the browser window
		driver.manage().window().maximize();
		//wait
		Thread.sleep(10000);
		//click on new chat tab
		driver.findElement(By.xpath("//*[@title='New chat' and @role='button']")).click();
		
		//wait
		Thread.sleep(500);

		for(int i=0;i<array.length;i++){
			//wait
			//Thread.sleep(500);
			//clear the search textbox
			driver.findElement(By.xpath("//*[@id='input-chatlist-search']")).clear();
			//put contact name into it
			driver.findElement(By.xpath("//*[@id='input-chatlist-search']")).sendKeys(array[i]);
			//enter
			robot.keyPress(java.awt.event.KeyEvent.VK_ENTER);
			//get chat name 
			String name=driver.findElement(By.xpath("//*[@class='_2zCDG']")).getText();
			//checking contact name and chat name
			if(array[i].equals(name)){
				//put message
				driver.findElement(By.xpath("//*[@class='_2bXVy']")).sendKeys("Hi");
				//click on send
				driver.findElement(By.xpath("//*[@class='_2lkdt']")).click();
			}
			
		}
		//click on menu
		driver.findElement(By.xpath("//*[@role='button' and @title='Menu']")).click();
		//wait
		Thread.sleep(500);
		//click on logout
		driver.findElement(By.xpath("//*[@class='_3lSL5 _2dGjP' and @role='button' and @title='Log out']")).click();
		//wait
		Thread.sleep(500);
		//close browser
		driver.close();
	}

}
