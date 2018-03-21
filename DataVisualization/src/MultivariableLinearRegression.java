import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;

/**
 * Created by pickl on 3/19/2018.
 */
public class MultivariableLinearRegression {
    static String[][] teamWinsLosses;
    static String[][] teamWinLossRatio;
    static File winlossFile = new File("C:\\Users\\pickl\\Desktop\\DataFeed\\winloss");
    static File dataFeedFile = new File("C:\\Users\\pickl\\Desktop\\DataFeed\\WilsonvilleDatabase");
    static DataFormatter df = null;
    static XSSFWorkbook wbWinLoss = null;
    static XSSFWorkbook wbDataFeed = null;

    MultivariableLinearRegression(String[] teams, String[] variables){

        teamWinsLosses = new String[3][];
        //team
    }

    public static void createObjects(){
        try {
            df = new DataFormatter();
            OPCPackage pkg = OPCPackage.open(dataFeedFile);
            wbDataFeed = new XSSFWorkbook(pkg);
            pkg = OPCPackage.open(winlossFile);
            wbWinLoss = new XSSFWorkbook(pkg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void putDataInArray(Sheet sheet, String[][] array){
        int numRows = sheet.getLastRowNum() + 1;
        int numCols = sheet.getRow(0).getLastCellNum();
        array = new String[numRows-1][numCols];
        for (int i = 1; i < numRows; i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < numCols; j++) {
                Cell cell = row.getCell(j);
                String value = df.formatCellValue(cell);
                array[i - 1][j] = value;
                //try{System.out.print(value + " ");}catch (Exception e){System.err.print(i+":"+j+" ");}
            }
        }
    }

}
