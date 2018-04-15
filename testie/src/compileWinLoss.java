import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by pickl on 3/20/2018.
 */
public class compileWinLoss {
    static ArrayList<String[][]> data;
    static File path = new File("C:\\Users\\pickl\\Desktop\\DataFeed\\OswegoDatabase.xlsx");
    public static void main(String[] args){
        data = new ArrayList<>();
        try{
            //XSSFWorkbook workbook = new XSSFWorkbook();
            Workbook workbook = WorkbookFactory.create(path);
            Sheet sheet = workbook.createSheet("wins-losses");

            Scanner scan = new Scanner(new File("C:\\Users\\pickl\\Desktop\\misc\\Oswegowinloss.txt"));
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("team");
            row.createCell(1).setCellValue("wins");
            row.createCell(2).setCellValue("losses");
            int count = 1;

            while(scan.hasNext()){
                String[] temp = scan.nextLine().split("\t");
                row = sheet.createRow(count);
                row.createCell(0).setCellValue(temp[1]);
                row.createCell(1).setCellValue(temp[7].split("-")[0]);
                try{
                    row.createCell(2).setCellValue(temp[7].split("-")[1]);
                }catch (Exception e) {
                    System.out.println(temp[7]);
                }
                count++;
            }
            try{
                //FileOutputStream os = new FileOutputStream(new File("C:\\Users\\pickl\\Desktop\\DataFeed" + "\\winloss.xlsx"), true);
                FileOutputStream os = new FileOutputStream(path, true);

                workbook.write(os);
                os.close();
            } catch (Exception e){}
        }catch (Exception e){e.printStackTrace();}

    }
}
