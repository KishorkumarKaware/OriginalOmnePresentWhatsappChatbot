import java.awt.AWTException;
import java.awt.Robot;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class WhatsAppReply {

	public void newSendContactsList(ArrayList<String> sendArrayList) throws IOException{
		//send list file
		File sendFile=new File("D:/Send_List.xls");
		//check file
		//if file found rewrite in it
		if(sendFile.exists())
		{
			ArrayList<String> readFileList=new ArrayList<String>();
			FileInputStream sendList = new FileInputStream(sendFile);
			HSSFWorkbook sendWorkbook= new HSSFWorkbook(sendList);
			HSSFSheet sendListSheet= (HSSFSheet)sendWorkbook.getSheet("Sheet1");
			//read data from file store it into arraylist
			for(int r1=1;r1<=sendListSheet.getLastRowNum();r1++){
				Row row=sendListSheet.getRow(r1);
				Cell name=row.getCell(0);
				String contactName=name.toString();
				readFileList.add(contactName);
			}
			sendList.close();
			//delete the send file
			sendFile.delete();
			//new file send list
			FileOutputStream writeSendFile=new FileOutputStream(sendFile);
			HSSFWorkbook workbook=new HSSFWorkbook();
			HSSFSheet sendSheet=(HSSFSheet)workbook.createSheet("Sheet1");
			Row headRow=sendSheet.createRow(0);
			Cell headCell=headRow.createCell(0);
			headCell.setCellValue("Name");
			//copy new contacts list
			readFileList.addAll(sendArrayList);
			//copy  contacts name in it
			for(int i=0;i<readFileList.size();i++){
				Row data=sendSheet.createRow(i+1);
				Cell contactName=data.createCell(0);
				contactName.setCellValue(readFileList.get(i));
			}
			workbook.write(writeSendFile);
			writeSendFile.close();
			System.out.println("rewrite in to sendlist");
		}
		//new send list
		else{
		
			FileOutputStream writeSendFile=new FileOutputStream(sendFile);
			HSSFWorkbook workbook=new HSSFWorkbook();
			HSSFSheet sendSheet=(HSSFSheet)workbook.createSheet("Sheet1");
			Row headRow=sendSheet.createRow(0);
			Cell headCell=headRow.createCell(0);
			headCell.setCellValue("Name");
			//copy all send contacts name
			for(int i=0;i<sendArrayList.size();i++){
				Row data=sendSheet.createRow(i+1);
				Cell contactName=data.createCell(0);
				contactName.setCellValue(sendArrayList.get(i));
			}
			workbook.write(writeSendFile);
			writeSendFile.close();
			System.out.println("write in to sendlist");
		}
	}
	public static void main(String[] args) throws IOException, AWTException, InterruptedException 
	{
		
		File textFile=new File("D:/TextMessage.txt");
		//class object
		WhatsAppReply war=new WhatsAppReply();
		//class object
		FileOperation fo=new FileOperation();
		//list for reply contacts name
		ArrayList<String> replyList=fo.FileReadWriteAndUpdate();
		//list for store send contacts name
		ArrayList<String> sendArrayList=new ArrayList<String>();
		//string array
		String[] array=new String[replyList.size()];
		int num=0;
		// get all contacts name in arraylist to array
		for(String name:replyList){
			array[num]=name;
			num++;
		}
		//System.setProperty("webdriver.chrome.driver", "D:/WhatsAppProject/InputFile/chromedriver_win32/chromedriver.exe");
		//get driver
		WebDriver driver=new  FirefoxDriver();
		//robot class object
		Robot robot=new Robot();
		//load url
		driver.get("https://web.whatsapp.com/");
		//maximize the window
		driver.manage().window().maximize();
		//wait for login
		Thread.sleep(20000);
		//click on new chat
		driver.findElement(By.xpath("//*[@title='New chat' and @role='button']")).click();
		//wait
		Thread.sleep(500);
		//send message to all
		for(int i=0;i<array.length;i++){
			//wait
			Thread.sleep(500);
			//clear text box
			driver.findElement(By.xpath("//*[@id='input-chatlist-search']")).clear();
			//put contact name into text box
			driver.findElement(By.xpath("//*[@id='input-chatlist-search']")).sendKeys(array[i]);
			//enter
			robot.keyPress(java.awt.event.KeyEvent.VK_ENTER);
			//get chat name
			String name=null;
			if(driver.findElements(By.xpath("//*[@class='_2zCDG']")).size()!=0){
				name=driver.findElement(By.xpath("//*[@class='_2zCDG']")).getText();
			}
			String notFound=null;
			if(driver.findElements(By.xpath("//*[@class='_3WZoe']")).size()!=0){
				notFound=driver.findElement(By.xpath("//*[@class='_3WZoe']")).getText();
			}
			//check chat name and contact name
			if(array[i].equals(name)||!notFound.equals("No contacts found")){
				//check unread message is there or not
				if(driver.findElements(By.xpath("//*[@class='L89LI']")).size()==0)
				{
					//wait
					Thread.sleep(500);
				}
				else{
					if(textFile.exists()){
						//input stream object
						FileInputStream fin=new FileInputStream(textFile);
						//byte array
						byte[] data=new byte[(int) textFile.length()];
						//read whole data
						fin.read(data);
						//close file input stream
						fin.close();
						//message
						String msg=new String(data);
						//put the message
						driver.findElement(By.xpath("//*[@class='_2bXVy']")).sendKeys(msg);
						//click on send
						driver.findElement(By.xpath("//*[@class='_2lkdt']")).click();
					}
					else{
						//click on attach
						driver.findElement(By.xpath("//*[@role='button' and @title='Attach']")).click();
						//click on item
						driver.findElement(By.xpath(".//*[@id='main']/header/div[3]/div/div[2]/span/div/div/ul/li[1]/button")).click();
						//run the autoit code for desktop window 
						Runtime.getRuntime().exec("D:/WhatsAppProject/InputFile/File_Upload.exe");
						//wait for upload the file
						Thread.sleep(5000);
						//click on send
						driver.findElement(By.xpath("//*[@class='_3hV1n yavlE']")).click();
						//put that contact name in to arraylist 
						sendArrayList.add(name);
					}
				}
				
			}
			
		}
		//click on menu
		driver.findElement(By.xpath("//*[@role='button' and @title='Menu']")).click();
		//wait
		Thread.sleep(500);
		//click on logout
		driver.findElement(By.xpath("//*[@class='_3lSL5 _2dGjP' and @title='Log out']")).click();
		//wait
		Thread.sleep(500);
		//browser close
		driver.close();
		//update send contacts list file
		war.newSendContactsList(sendArrayList);
	}

}