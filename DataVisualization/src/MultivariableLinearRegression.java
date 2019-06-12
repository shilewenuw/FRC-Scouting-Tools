import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pickl on 3/19/2018.
 */
public class MultivariableLinearRegression {
    static ArrayList<String[][]> dataFeed;
    static File dataFeedFileFolder = new File("C:\\Users\\pickl\\Desktop\\DataFeed");//folder where spreadsheets are stored
    static DataFormatter df = null;
    static String[] allvars;
    static Map<String[][], String[][]> hashMapForWinLossBasedOnData;//contains win/loss ratios of teams of different data sets
    static ArrayList<String[]> datasForMultipleTeams;
    static String[] selectVars;
    static LinearRegression[] linReg;
    static String[][] data;

    public static void main(String[] args){
        ArrayList<String[]> arrayList = new ArrayList<>();
        GetArrayFromDB getArrayFromDB = new GetArrayFromDB(1);
        data = getArrayFromDB.array;
        arrayList.add(getDataOfTeam("7032"));

        String[] array = {"line cross", "auto switch", "auto scale", "auto vault",
                "tele-op switch self", "tele-op switch opponent", "tele-op scale", "tele-op vault", "climb"};
        MultivariableLinearRegression linearRegression = new MultivariableLinearRegression(arrayList, array);
        System.out.print(linearRegression.predict());
    }

    MultivariableLinearRegression(ArrayList<String[]> datasForMultipleTeams, String[] vars){
        selectVars = vars;
        this.datasForMultipleTeams = datasForMultipleTeams;
        allvars = (new GetArrayFromDB(1)).variables;//get all variables
        df = new DataFormatter();
        getDataFeed();
        linReg = new LinearRegression[vars.length]; //create a linear regression model for each variable
        for(int i=0; i<vars.length;i++){//add data to each linear regression model
            linReg[i] = new LinearRegression();
            addData(linReg[i], vars[i]);
        }


    }

    public static void getDataFeed(){//get averages and win/loss
        dataFeed = new ArrayList<>();
        hashMapForWinLossBasedOnData = new HashMap<>();
        ArrayList<Sheet[]> sheets = new ArrayList<>();
        for(File file:dataFeedFileFolder.listFiles()){//multiple spreadsheeta with data should be processes
            if (file.getName().contains("Database")) {
                sheets.add(getSheetsFromWorkbook(file));
            }
        }
        for (Sheet[] sheet: sheets) {
            String[][] tempData = getDataFromArray(sheet[0]);//index 0 is where averages sheet is, index 1 is winloss
            dataFeed.add(tempData);
            hashMapForWinLossBasedOnData.put(tempData,
                    processWinsLossesIntoRatio(
                            getDataFromArray(sheet[1])));
        }

    }

    public static String[][] getDataFromArray(Sheet sheet){//input sheet, get the 2d array of data back
        String[][] array;
        int numRows = sheet.getLastRowNum() + 1;//row number is one smaller than actual for a strange reason
        int numCols = sheet.getRow(0).getLastCellNum();
        array = new String[numRows-1][numCols];
        for (int i = 1; i < numRows; i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < numCols; j++) {
                Cell cell = row.getCell(j);
                String value = df.formatCellValue(cell);
                array[i - 1][j] = value;
                try{
                    //System.out.print(value + " ");
                    }catch (Exception e){System.err.println(i+":"+j+" ");}
            }
        }
        return array;
    }
    public static void addData(LinearRegression linReg, String var) {//add data of linear regression model on one variable
        for(String[][] Data: dataFeed){
            for(String[] arr:Data) {
                linReg.addPoint(Double.parseDouble(arr[getIndex(var)]), getWinLossOfTeamFromDataSet(Data, arr[0].split("m")[1]));
            }
            linReg.calculateLinearRegression();
        }
    }
    public static double getWinLossOfTeamFromDataSet(String[][] Data, String team){//input dataset to get corresponding win loss ratio
        String[][] temp = hashMapForWinLossBasedOnData.get(Data);
        double winlossratio = 0;
        for(String[] arr:temp){
            if(arr[0].equals(team))
                winlossratio = Double.parseDouble(arr[1]);
        }
        //+++++++++System.out.print(winlossratio);
        return winlossratio;
    }

    public static String[][] processWinsLossesIntoRatio(String[][] array){//takes wins losses, and finds the ratios
        String[][] winlossratio = new String[array.length][3];
        for(int i=0;i<winlossratio.length;i++){
            winlossratio[i][0] = array[i][0];
            winlossratio[i][1] = calculateRatio(array[i][1],array[i][2]);
        }
        return winlossratio;
    }
    public static String calculateRatio(String wins, String losses){

        return Double.toString(Double.parseDouble(wins)/ ( Double.parseDouble(wins)+Double.parseDouble(losses)));
    }
    public static Sheet[] getSheetsFromWorkbook(File file){//input a file, select the two sheets wanted (data and winloss)
        Sheet sheet[] = new Sheet[2];
        try {
            OPCPackage pkg = OPCPackage.open(file);
            XSSFWorkbook temp = new XSSFWorkbook(pkg);
            sheet[0] = temp.getSheet("Compresos");//Compresos is where the averages are
            sheet[1] = temp.getSheet("wins-losses");
        }catch (Exception e){e.printStackTrace();}
        return sheet;
    }
    public static int getIndex(String item) {//get index of variable in variables array
        int index = 0;
        for (int i = 0; i < allvars.length; i++)
            if (allvars[i].equals(item))
                index = i;
        return index;
    }

    public double predict(){
        double total = 0;
        for(String[] dataForOneTeam:datasForMultipleTeams){
            total += calculatePercentageForTeam(dataForOneTeam);
            //System.out.println(dataForOneTeam);
        }
        return total/datasForMultipleTeams.size();
    }
    public static double calculatePercentageForTeam(String[] dataForOneTeam){
        double total = 0;
        double totalSlopes = 0;
        for(int i=0;i<selectVars.length;i++) {
            totalSlopes += linReg[i].getSlope();
            linReg[i].predict(Double.parseDouble(dataForOneTeam[getIndex(selectVars[i])]));
        }
        for(LinearRegression linreg:linReg)
            total += linreg.getSlope() * linreg.getPrediction() / totalSlopes;
        return total;

    }
    public static String[] getDataOfTeam(String team){
        String[] temp = null;
        for(String[] arr: data){
            if(arr[0].split("m")[1].equals(team))
                temp = arr;
        }
        return temp;
    }
}
