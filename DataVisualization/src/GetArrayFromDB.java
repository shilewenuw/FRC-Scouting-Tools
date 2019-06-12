/**
 * Created by pickl on 1/15/2018.
 */
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;

/**
 * Created by pickl on 1/11/2018.
 */
public class GetArrayFromDB {

    static final File file = new File("C:\\Users\\pickl\\OneDrive\\Desktop\\bob\\test.xlsx");

    static String[][] array;
    static String[] teams;
    static DataFormatter df = null;
    static XSSFWorkbook wb = null;
    static String[] variables;
    static final int SHEET_RAW = 0;
    static final int SHEET_AVG = 1;
    //static final int SHEET_NORM = 2;
    static final int SHEET_AVG_LATEWEIGHTED = 2;
    static final int SHEET_MEDIAN = 3;
   // static final int SHEET_MEDIAN_NORMALIZED = 4;


    GetArrayFromDB(int sheet){
        try{
            if(OptionPanel.meanToMedianCB.isSelected()){
                if(sheet == SHEET_AVG)
                    sheet = SHEET_MEDIAN;
            }
            if(OptionPanel.normalizeCB.isSelected()){
                //sheet+=1;
                if(sheet == SHEET_AVG)
                    sheet = SHEET_AVG_LATEWEIGHTED;
            }
        }catch (Exception e){}
        try {
            df = new DataFormatter();
            OPCPackage pkg = OPCPackage.open(file);
            wb = new XSSFWorkbook(pkg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getVariablesAndTeams(sheet);
        readData(sheet);
    }

    public static void readData(int shet) {

        Sheet sheet = wb.getSheetAt(shet);
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

    public static void getVariablesAndTeams(int shet){
        Sheet sheet = wb.getSheetAt(shet);
        variables = new String[sheet.getRow(0).getLastCellNum()];
        teams = new String[sheet.getLastRowNum()];//do not subtract one, for some reason the method counts one less already
        for (int i=0;i<variables.length;i++)
            variables[i]=getCell(shet, 0, i);
        for (int j=1;j<teams.length+1;j++)
            teams[j-1]=getCell(shet, j, 0);

    }

    public static String getCell(int sheetNumber, int rowPosition, int columnPosition){
        return df.formatCellValue(wb.getSheetAt(sheetNumber).getRow(rowPosition).getCell(columnPosition));
    }
}

