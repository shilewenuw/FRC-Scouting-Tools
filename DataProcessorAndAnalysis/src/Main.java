import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;


/**
 * Created by pickl on 10/16/2017.
 */
public class Main {
    //static String path1 = "C:\\Users\\pickl\\OneDrive\\Stuff\\Houston";
    static String path =  "C:\\Users\\pickl\\OneDrive\\Stuff\\Oswego\\";
    static String DBname = "OswegoDatabase.xlsx";
    static File folder = new File(path);//where scout data files are located

    static String[] variables1 = {"team number",
            "line cross", "auto switch", "auto scale", "auto vault",
            "tele-op switch self", "tele-op switch opponent", "tele-op scale", "tele-op vault", "climb",
            "switch cycle time self", "switch cycle time opponent", "scale cycle time", "vault cycle time",
            "unfortunates", "strategy", "comments"};

    static String[] composites = { "1st pick", "2nd pick", "switch combo", "Autocracy"};//, "Focus comp", "Auto comp", "CubesT"};//variables to put at the top of a spreadsheet
    static String[] variables = Stream.concat(Arrays.stream(variables1), Arrays.stream(composites))
            .toArray(String[]::new);
    static ArrayList<String> teams;//expandable list that collects all team names from files

    static String array[][];//where all data is temporarily put before going in the spreadsheet
    static String arrayofaverages[][];

    static final int COUNT_POSITION = 0;//position of the count of times team appears, used in later method
    static final int TOTAL_POSITION = 1;

    static Workbook workbook;
    static Sheet sheetRaw, sheetAverages, sheetWendexLocal, sheetWendexGlobal;

    static final int numberOfComposites = composites.length;

    public static void main(String[] args) {
        processFiles();
    }

    public static void processFiles() {

        workbook = new XSSFWorkbook();//Overall spreadsheet---all sheets
        sheetRaw = workbook.createSheet("Todos");//spreadsheet sheet
        sheetAverages = workbook.createSheet("Compresos");//another one
        sheetWendexLocal = workbook.createSheet("Wendex Local");//another one
        sheetWendexGlobal = workbook.createSheet("Wendex Global");//another one
        teams = new ArrayList<>();// used to determine how many rows the 2x2 array will have, as each data file will have their own row

        array = new String[getTeamsCount()][variables.length];//one array element for each file
        Row row1 = sheetRaw.createRow((short) 0);//for first sheet
        Row row2 = sheetAverages.createRow((short) 0);//for second sheet
        Row row3 = sheetWendexLocal.createRow((short) 0);
        Row row4 = sheetWendexGlobal.createRow((short) 0);
        for (int k = 0; k < variables.length; k++) {
            row1.createCell(k).setCellValue(variables[k]);//establish headers
            row2.createCell(k).setCellValue(variables[k]);
            row3.createCell(k).setCellValue(variables[k]);
            row3.createCell(k).setCellValue(variables[k]);
        }
        putDataInArrayAndSpreadSheet();
        //putIndicesInSpreadSheet();

        arrayofaverages = new String[teams.size()][variables.length];

        for (int a = 0; a < teams.size(); a++) {//put averages in sheetAverages, and put wendex values in sheetWendexLocal
            Row rowRaw = sheetAverages.createRow((short) a + 1);//top row taken by variable names
            Row rowWendexLocal = sheetWendexLocal.createRow((short) a + 1);
            Row rowWendexGlobal = sheetWendexGlobal.createRow((short) a + 1);

            rowRaw.createCell(0).setCellValue(teams.get(a));//set team numbers
            rowWendexLocal.createCell(0).setCellValue(teams.get(a));
            rowWendexGlobal.createCell(0).setCellValue(teams.get(a));

            for (int q = 1; q < variables1.length - (7); q++) {//first cell covered
                try { //set values of averages, and
                    if (("true").equals(array[2][q]) || ("false").equals(array[2][q])) {
                        double avgBool = calculateAverage("bool", getIndex(variables[q]), teams.get(a));
                        rowRaw.createCell(getIndex(variables[q])).setCellValue(avgBool);
                        rowWendexLocal.createCell(getIndex(variables[q])).setCellValue(
                                calculateWendex("bool", getIndex(variables[q]), teams.get(a), avgBool));
                        arrayofaverages[a][getIndex(variables[q])] = Double.toString(avgBool);

                    }
                    else if (isNumeric(array[2][q])) {
                        double avgNum = calculateAverage("number", getIndex(variables[q]), teams.get(a));
                        rowRaw.createCell(getIndex(variables[q])).setCellValue(avgNum);
                        rowWendexLocal.createCell(getIndex(variables[q])).setCellValue(
                                calculateWendex("number", getIndex(variables[q]), teams.get(a), avgNum));
                        arrayofaverages[a][getIndex(variables[q])] = Double.toString(avgNum);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //

            for (int l = variables1.length - (7); l < variables1.length - (3); l++){//calculate cycle times according to cubes scored
                rowRaw.createCell(l).setCellValue(calculateAvgCycleTimes(l, teams.get(a)));
            }
            for (int b = variables1.length - (3); b < variables.length; b++){//this puts all the non number info into one cell
                rowRaw.createCell(b)
                        .setCellValue(concatComments(teams.get(a), b));
            }
            for (int c=variables.length-numberOfComposites;c<variables.length;c++){
                double avgNum2 = calculateAverage("number", getIndex(variables[c]), teams.get(a));
                rowRaw.createCell(getIndex(variables[c])).setCellValue(avgNum2);

                rowWendexLocal.createCell(getIndex(variables[c])).setCellValue(
                        calculateWendex("number", getIndex(variables[c]), teams.get(a), avgNum2));
            }
        }




        try {
            FileOutputStream os = new FileOutputStream(path + DBname, false);
            workbook.write(os);
            os.close();
        } catch (Exception e) {
            System.err.println("write failed. close the spreadsheet.");
        }
    }
    public static void putDataInArrayAndSpreadSheet(){
        int currentCount = 0;// used to index data for row position in array
        for (File file: folder.listFiles()) {//put all data in 2x2 array
            if (file.getName().matches("team\\d(.*)")) { //in order to filter out certain files
                try {

                    BufferedReader fr = new BufferedReader(new FileReader(file));//go through files

                    String line = fr.readLine();//read the data stored in txt files
                    String[] temp = line.split(",");//turns CSVs into array
                    //System.out.print("="+temp.length+"=");

                    for(int y=0;y<temp.length;y++){
                        try{array[currentCount][y] = temp[y];}catch (Exception e){}
                    }
                    //double[] adjustmentValues = {};
                    String[] arr5 = {"auto switch", "auto scale", "auto vault",
                            "tele-op switch self", "tele-op switch opponent", "tele-op scale", "tele-op vault"};
                    double[] vals5 = {1,1,1,1,1,1,1};
                    double tt = calculateCompositeScore(array[currentCount], arr5, vals5);

                    String[] arr1 = {"auto switch", "auto scale", "tele-op switch self", "tele-op switch opponent",
                            "tele-op scale", "tele-op vault"};
                    double[] vals1 = {13.3,16.7,1.96,1.96,6.57,.983};
                    array[currentCount][variables.length-numberOfComposites] = Double.toString(
                            calculateCompositeScore(array[currentCount], arr1, vals1) + tt*.441);

                    String[] arr2 = {"tele-op switch self", "tele-op switch opponent", "tele-op scale", "tele-op vault"};
                    //double[] vals2 = {1, 1, .5, 1.5};
                    double[] vals2 = {0,19.3,3.34,3.34,1.89,3.39};
                    array[currentCount][variables.length-numberOfComposites+1] = Double.toString(
                            calculateCompositeScore(array[currentCount], arr1, vals2) + tt*1.26);

                    String[] arr3 = {"tele-op switch self", "tele-op switch opponent"};
                    double[] vals3 = {1, 1};
                    array[currentCount][variables.length-numberOfComposites+2] = Double.toString(
                            calculateCompositeScore(array[currentCount], arr3, vals3));

                    String[] arr4 = {"auto switch", "auto scale"};
                    double[] vals4 = {1,1};
                    array[currentCount][variables.length-numberOfComposites+3] = Double.toString(
                            calculateCompositeScore(array[currentCount], arr4, vals4));

                    /*String[] arr5 = {"auto switch", "auto scale", "auto vault",
                            "tele-op switch self", "tele-op switch opponent", "tele-op scale", "tele-op vault"};
                    double[] vals5 = {1,1,1,1,1,1,1};
                    array[currentCount][variables.length-numberOfComposites+4] = Double.toString(
                            calculateCompositeScore(array[currentCount], arr5, vals5));*/




                    //System.out.print("find:" + array[currentCount][temp.length]);

                    String tm = file.getName().split("-")[0];//team number
                    if (!teams.contains(tm)) {//if teams arraylist doesn't have a team, add it to it
                        teams.add(tm);
                    }
                    Row row1 = sheetRaw.createRow(currentCount+1);
                    for (int j = 0; j < array[currentCount].length; j++) {//put all data into spreadsheet
                        try{if(variables[j].contains("cycle"))
                            row1.createCell(j).setCellValue(calculateCycleTime(array[currentCount][j]));
                        else
                            row1.createCell(j).setCellValue(array[currentCount][j]);}catch (Exception e){}
                    }

                    currentCount++;

                } catch (Exception e) {//e.printStackTrace();
                }
            }
        }
    }
    public static void putIndicesInSpreadSheet(){

    }
    /*public static double hyperAverage (String type, int position){
        double denom = 0;
        if (type == "bool"){
            for (int i=0;i<array.length;i++){
                if (matches(array[i], teamnumber))//find the index of array[][] at which the target team number matches it
                    if (array[i][position].equals("false"))
                        denom += Math.pow(avg, 2);
            }
        }
        else if (type == "number"){
            for (int i=0;i<array.length;i++){
                if (matches(array[i], teamnumber))//find the index of array[][] at which the target team number matches it
                    try {if (isLessThan(array[i][position], avg))
                        denom += Math.pow(avg-Double.parseDouble(array[i][position]), 2);}catch (Exception e){e.printStackTrace();}
            }
        }
    }*/
    public static double calculateWendex (String type, int position, String teamnumber, double avg){
        double instances = calculateSumAndCount(type, position, teamnumber)[COUNT_POSITION];
        double denom = 0;
        if (type == "bool"){
            for (int i=0;i<array.length;i++){
                if (matches(array[i], teamnumber))//find the index of array[][] at which the target team number matches it
                    if (array[i][position].equals("false"))
                        denom += Math.pow(avg, 2);
            }
        }
        else if (type == "number"){
            for (int i=0;i<array.length;i++){
                if (matches(array[i], teamnumber))//find the index of array[][] at which the target team number matches it
                    try {if (isLessThan(array[i][position], avg))
                        denom += Math.pow(avg-Double.parseDouble(array[i][position]), 2);}catch (Exception e){}//e.printStackTrace();}
            }
        }
        if(avg==0)
            return 0;
        else
            return Math.sqrt(denom/instances/avg);
    }//index
    public static double calculateAverage(String type, int position, String teamnumber) {
        double temp[] = calculateSumAndCount(type, position, teamnumber);
        double count = temp[COUNT_POSITION];
        double total = temp[TOTAL_POSITION];

        return total / count;
    }//2 part so other method can be reused
    public static double[] calculateSumAndCount(String type, int position, String teamnumber){
        double[] elements = new double[2];
        double count = 0;
        double total = 0;
        if (type == "bool") {
            for (int i = 0; i < array.length; i++) {
                try {
                    if (matches(array[i], teamnumber)) {//
                        count++;
                        if (array[i][position].equals("true"))
                            total++;
                    }
                } catch (Exception e) {//e.printStackTrace();
                }
            }
        } else if (type == "number") {
            for (int i = 0; i < array.length; i++) {
                try {
                    if (matches(array[i], teamnumber)) {
                        count++;
                        total += Double.parseDouble(array[i][position]);
                    }
                } catch (Exception e) {//e.printStackTrace();
                }
            }
        }
        elements[COUNT_POSITION] = count;
        elements[TOTAL_POSITION] = total;
        return elements;
    }//sum/count = average, but count is used somewhere else, the average calculations are in previous method
    public static double calculateAvgCycleTimes(int position, String teamnumber){
        double totaltime = 0;
        double totalcubes = 0;
        double temp = 0;
        for (int i=0;i<array.length;i++){
            if (matches(array[i], teamnumber)){
                try {
                    totaltime += Double.parseDouble(array[i][position].split("_")[0]);
                    totalcubes += Double.parseDouble(array[i][position].split("_")[1]);
                }catch (Exception e){//System.out.print(array[i][position]);}
                    //System.err.print(position + " ");
                    }
            }
        }
        if(totalcubes==0)
            temp = 0;
        else
            temp = totaltime/totalcubes;
        return temp;
    }
    public static double getMaxFromArray(String[][] arrayofaverages, int column){
        double max = 0;
        for(String[] arr:arrayofaverages)
            if (Double.parseDouble(arr[column]) > max)
                max = Double.parseDouble(arr[column]);
        return max;
    }
    public static double calculateCycleTime(String string){
        double totaltime = Double.parseDouble(string.split("_")[0]);
        double totalcubes = Double.parseDouble(string.split("_")[1]);
        if(totalcubes == 0){
            return 0;
        }
        else
            return totaltime/totalcubes;
    }
    public static double calculateCompositeScore(String[] array, String[] vars, double[] values){
        double total = 0;
        if (vars.length!=values.length)
            System.err.println("vars and values don't match" + vars.length + ":" + values.length);
        for (int i=0;i<vars.length;i++){
            if(Boolean.parseBoolean(array[1]))
                total+=1.0;
            total+=(Double.parseDouble(array[getIndex(vars[i])]) * values[i]);
        }
        return total;
    }
    public static double calculateCompositeScore2(String[] array, String[] vars, double[] values){
        double total = 0;
        if (vars.length!=values.length)
            System.err.print("vars and values don't match");
        for (int i=0;i<vars.length;i++){
            if(Boolean.parseBoolean(array[1]))
                total+=1.0;
            total+=(Double.parseDouble(array[getIndex(vars[i])]) * values[i]);
        }
        if (array[getIndex("climb")].equals("3"))
            total += 9;
        return total;
    }


    /*public static double calculateCompositeScoreAdjusted(String[] array, String[] vars, double[] values, double[] adjustment){

    }*/


    public static String concatComments(String teamnumber, int column) {
        String concat = "";
        for (String[] arr : array) {
            try {
                if (arr[0].split("-")[0].equals(teamnumber) && !arr[column].equals(""))
                    concat += arr[column] + "$";
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        return concat;
    }//puts all strings into one string
    public static int getIndex(String item) {
        int index = 0;
        for (int i = 0; i < variables.length; i++)
            if (variables[i] == item)
                index = i;
        return index;
    }//index of variable within the variables array
    public static boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }
    public static boolean matches(String[] team, String teamnumber){//each array has a corresponding team
        return (team[0].split("-")[0].equals(teamnumber));
    }
    public static boolean isLessThan(String num1, double num2){//unique comparison useful for the wendex calculations
        return (Double.parseDouble(num1) < num2);
    }
    public static int getTeamsCount(){
        int teamsCount = 0;
        for (File file0: folder.listFiles())
            if (file0.getName().matches("team\\d(.*)"))//some files, like the .xlsx(the spreadsheet), is not wanted
                teamsCount++;
        return teamsCount;
    }
}
