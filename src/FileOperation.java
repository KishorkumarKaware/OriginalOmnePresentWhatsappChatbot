import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
//comparing to excel files and find out unmatched data into them and store into arraylist
public class FileOperation {
	//getting data from excel and store it into arraylist
	public ArrayList<String> FileRead(String filePath) throws IOException{
		ArrayList<String> readFileList=new ArrayList<String>();
		FileInputStream sendList = new FileInputStream(filePath);
		Workbook sendWorkbook= new HSSFWorkbook(sendList);
		HSSFSheet sendListSheet= (HSSFSheet)sendWorkbook.getSheet("Sheet1");
		for(int r1=1;r1<=sendListSheet.getLastRowNum();r1++){
			Row row=sendListSheet.getRow(r1);
			Cell name=row.getCell(0);
			String contactName=name.toString();
			readFileList.add(contactName);
		}
		sendList.close();
		return readFileList;
	}
	public ArrayList<String> FileReadWriteAndUpdate() throws IOException{
		ArrayList<String> replyList=new ArrayList<String>();
		File sendFile=new File("D:/Send_List.xls");
		//check file is present or not
		if(sendFile.exists()){
			//sendListExcel
			ArrayList<String> sendNameList=FileRead("D:/Send_List.xls");
			
			//contactsListExcel
			ArrayList<String> contactsNameList=FileRead("D:/ContactList.xls");
			
			int temp=0;
			//compare to arraylist
			for(String contactName:contactsNameList){
				temp=0;
				for(String sendName:sendNameList){
					if(contactName.equals(sendName))
					{
						temp=1;
					}
				}
				if(temp==0){
					replyList.add(contactName);
				}
			}
		}
		else{
			replyList=FileRead("D:/ContactList.xls");
		}
		//return arraylist
		return replyList;
	}
}
