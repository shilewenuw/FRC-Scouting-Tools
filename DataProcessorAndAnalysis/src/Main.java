import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;


/**
 * Created by pickl on 10/16/2017.
 */
public class Main {

    static String path = "C:\\Users\\pickl\\OneDrive\\Desktop\\bob";
    static String DBname = "\\test.xlsx";
    static File folder = new File(path);//where scout data files are located

    static String[] allVars;//combines the two string arrays into one
    static ArrayList<String> teams;//expandable list that collects all team names from files
    static double[] maxMedians;
    static double[] maxCompositeAverages;
    static String array[][];//where all data is temporarily put before going in the spreadsheet
    static String arrayOfAverages[][];

    static final int COUNT_POSITION = 0;//position of the count of times team appears, used in later method
    static final int TOTAL_POSITION = 1;

    static Workbook workbook;
    static Sheet sheetRaw, sheetAverages, sheetNormalized, sheetMedian, sheetMedNormalized, sheetLateWeighted;
    static LinkedHashMap<String, double[]> compositeDict;
    static int numberOfComposites;

    public static void main(String[] args) {
        processFiles();
    }

    public static void processFiles() {
        compositeDict = Global.dict(); //returns a dictionary
        String[] composites = new String[compositeDict.size()];
        numberOfComposites = composites.length;
        int iter = 0;
        for (Object str : compositeDict.keySet()) { //get String names of the composite variables
            composites[iter] = (String) str;
            iter++;
        }

        allVars = Stream.concat(Arrays.stream(Global.basicVars), Arrays.stream(composites))
                .toArray(String[]::new); //combines the basic and composite vars arrays

        workbook = new XSSFWorkbook();//Overall spreadsheet---all sheets
        sheetRaw = workbook.createSheet("IT'S RAW");//spreadsheet sheet
        sheetAverages = workbook.createSheet("Mean man");
        //sheetNormalized = workbook.createSheet("Normalized");
        sheetLateWeighted = workbook.createSheet("Marco is a Late Weight");
        sheetMedian = workbook.createSheet("Median");

        teams = new ArrayList<>();// used to determine how many rows the 2x2 array will have, as each data file will have their own row
        array = new String[getTeamsCount()][allVars.length];//one array element for each file
        maxMedians = new double[Global.basicVars.length];//use to normalize the median value
        maxCompositeAverages = new double[numberOfComposites];

        Row rowSheet1 = sheetRaw.createRow((short) 0);//for first sheet
        Row rowSheet2 = sheetAverages.createRow((short) 0);//for second sheet
        //Row rowSheet3 = sheetNormalized.createRow((short) 0);
        Row rowSheet3 = sheetLateWeighted.createRow((short) 0);
        Row rowSheet4 = sheetMedian.createRow((short) 0);


        for (int k = 0; k < allVars.length; k++) {
            rowSheet1.createCell(k).setCellValue(allVars[k]);//establish headers
            rowSheet2.createCell(k).setCellValue(allVars[k]);
            rowSheet3.createCell(k).setCellValue(allVars[k]);
            rowSheet4.createCell(k).setCellValue(allVars[k]);
            //rowSheet5.createCell(k).setCellValue(allVars[k]);

        }
        putRawDataInArrayAndSpreadSheet();//puts all raw data on one sheet
        putAveragesInArrayAndSpreadsheet();//puts averaged data on one sheet
        //putNormalizedInSpreadsheet();
        putAveragesLateWeightedInSpreadSheet();
        putMedianInSpreadsheet();
        //putMedianNormalizedInSpreadsheet();

        try {
            FileOutputStream os = new FileOutputStream(path + DBname, false);
            workbook.write(os);
            os.close();
        } catch (Exception e) {
            System.err.println("write failed. close the spreadsheet.");
        }
    }

    public static void putRawDataInArrayAndSpreadSheet() {
        int currentCount = 0;// used to index data for row position in array
        for (File file : folder.listFiles()) {//put all data in 2d array
            if (file.getName().matches("team\\d(.*)")) { //in order to filter out certain files, make sures file has number
                try {

                    BufferedReader fr = new BufferedReader(new FileReader(file));//go through files

                    String line = fr.readLine();//read the data stored in txt files
                    String[] temp = line.split(",");//turns CSVs into array

                    for (int y = 0; y < temp.length; y++) {
                        try {
                            array[currentCount][y] = temp[y];
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    int iter = allVars.length - compositeDict.size();
                    for (Map.Entry<String, double[]> entry : compositeDict.entrySet()) {
                        array[currentCount][iter] = Double.toString(
                                calculateCompositeScore(array[currentCount], Global.quantVars,
                                        entry.getValue()) / .62
                        );
                        iter++;
                    }


                    String tm = file.getName().split("-")[0];//team number
                    if (!teams.contains(tm)) {//if teams arraylist doesn't have a team, add it to it
                        teams.add(tm);
                    }
                    Row row = sheetRaw.createRow(currentCount + 1);
                    for (int j = 0; j < array[currentCount].length; j++) {//put all data into spreadsheet
                        row.createCell(j).setCellValue(array[currentCount][j]);
                    }

                    currentCount++;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void putAveragesInArrayAndSpreadsheet() {
        arrayOfAverages = new String[teams.size()][Global.basicVars.length - 1];//minus one because we don't want comment

        for (int a = 0; a < teams.size(); a++) {//put averages in sheetAverages, and put wendex values in sheetWendexLocal
            Row iterRow = sheetAverages.createRow((short) a + 1);//top row taken by variable names


            iterRow.createCell(0).setCellValue(teams.get(a));//*set team numbers*/
            arrayOfAverages[a][0] = teams.get(a);

            for (int q = 1; q < Global.basicVars.length - 1; q++) {//first cell covered, last cell is comments
                try { //set values of averages, and
                    if (("true").equals(array[2][q]) || ("false").equals(array[2][q])) {
                        double avgBool = calculateAverage("bool", getIndex(allVars[q]), teams.get(a));
                        iterRow.createCell(getIndex(allVars[q])).setCellValue(avgBool);

                        arrayOfAverages[a][getIndex(allVars[q])] = Double.toString(avgBool);

                    } else if (isNumeric(array[2][q])) {
                        double avgNum = calculateAverage("number", getIndex(allVars[q]), teams.get(a));
                        iterRow.createCell(getIndex(allVars[q])).setCellValue(avgNum);

                        arrayOfAverages[a][getIndex(allVars[q])] = Double.toString(avgNum);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            iterRow.createCell(getIndex("comments"))
                    .setCellValue(concatComments(teams.get(a), getIndex("comments")));
            boolean temp = false;
            for (int c = allVars.length - compositeDict.size(); c < allVars.length; c++) {
                double avgNum2;
                if (!temp) {
                    avgNum2 = calculateAverage("number", getIndex(allVars[c]), teams.get(a)) / 3.25;
                    temp = true;
                } else
                    avgNum2 = calculateAverage("number", getIndex(allVars[c]), teams.get(a));
                iterRow.createCell(getIndex(allVars[c])).setCellValue(avgNum2);
            }
        }

    }

    public static void putAveragesLateWeightedInSpreadSheet() {

        for (int a = 0; a < teams.size(); a++) {//put averages in sheetAverages, and put wendex values in sheetWendexLocal
            Row iterRow = sheetLateWeighted.createRow((short) a + 1);//top row taken by variable names


            iterRow.createCell(0).setCellValue(teams.get(a));//*set team numbers*/

            for (int q = 1; q < Global.basicVars.length - 1; q++) {//first cell covered, last cell is comments
                try { //set values of averages, and
                    if (("true").equals(array[2][q]) || ("false").equals(array[2][q])) {
                        double avgBool = calculateAverageWeightLaters("bool", getIndex(allVars[q]), teams.get(a));
                        iterRow.createCell(getIndex(allVars[q])).setCellValue(avgBool);


                    } else if (isNumeric(array[2][q])) {
                        double avgNum = calculateAverageWeightLaters("number", getIndex(allVars[q]), teams.get(a)) ;
                        iterRow.createCell(getIndex(allVars[q])).setCellValue(avgNum);
                        System.out.println("bob");


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            iterRow.createCell(getIndex("comments"))
                    .setCellValue(concatComments(teams.get(a), getIndex("comments")));

            boolean temp = false;
            for (int c = allVars.length - compositeDict.size(); c < allVars.length; c++) {
                double avgNum2;
                if (!temp) {
                    avgNum2 = calculateAverageWeightLaters("number", getIndex(allVars[c]), teams.get(a)) ;
                    temp = true;
                } else
                    avgNum2 = calculateAverageWeightLaters("number", getIndex(allVars[c]), teams.get(a));
                iterRow.createCell(getIndex(allVars[c])).setCellValue(avgNum2);
            }
        }

    }

    public static void putNormalizedInSpreadsheet() {
        for (int i = 0; i < arrayOfAverages.length; i++) {
            Row iterRow = sheetNormalized.createRow((short) i + 1);
            iterRow.createCell(0).setCellValue(arrayOfAverages[i][0]);
            for (int j = 1; j < arrayOfAverages[0].length; j++) {
                arrayOfAverages[i][j] = divideEqual(arrayOfAverages[i][j], getMaxFromArray(j));
                iterRow.createCell(j).setCellValue(arrayOfAverages[i][j]);
            }
            iterRow.createCell(getIndex("comments")).setCellValue("");
        }

    }

    public static String divideEqual(String num1, double num2) {
        if (num2 == 0.0)
            return "0";
        else
            return Double.toString(Double.parseDouble(num1) / num2);
    }

    public static double divideEqual(double num1, double num2) {
        if (num2 == 0.0)
            return 0;
        else
            return num1 / num2;
    }

    public static double getMaxFromArray(int column) {
        double max = 0;
        for (String[] arr : arrayOfAverages)
            if (Double.parseDouble(arr[column]) > max)
                max = Double.parseDouble(arr[column]);
        return max;
    }

    public static void putMedianInSpreadsheet() {
        for (int i = 0; i < teams.size(); i++) {
            String[] temp = new String[Global.basicVars.length];
            Row iterRow = sheetMedian.createRow((short) i + 1);

            iterRow.createCell(0).setCellValue(teams.get(i));
            for (int j = 1; j < Global.basicVars.length - 1; j++) {
                double median = getMedian(teams.get(i), j);
                iterRow.createCell(j).setCellValue(median);
                temp[j] = Double.toString(median);
            }
            iterRow.createCell(getIndex("comments")).setCellValue("");

            int c = allVars.length - compositeDict.size();
            for (Object arr : compositeDict.values()) {
                double median = calculateCompositeScore(temp, Global.quantVars, (double[]) arr);
                iterRow.createCell(getIndex(allVars[c])).setCellValue(median);
                c++;
            }
        }

    }

    /*public static void putMedianNormalizedInSpreadsheet(){
        for(int i=0;i<teams.size();i++){
            String[] temp = new String[Global.basicVars.length];
            Row iterRow = sheetMedNormalized.createRow((short) i+1);

            iterRow.createCell(0).setCellValue(teams.get(i));
            for(int j=1;j<Global.basicVars.length-1;j++){
                double median = getMedian(teams.get(i),j);
                iterRow.createCell(j).setCellValue(divideEqual(median,maxMedians[j]));
                temp[j] = Double.toString(divideEqual(median,maxMedians[j]));
            }
            iterRow.createCell(getIndex("comments")).setCellValue("");
            iterRow.createCell(allVars.length-composites.length).
                    setCellValue(calculateCompositeScore(temp, Global.quantVars,Global.firstPick));
        }

    }*/
    public static double getMedian(String teamnumber, int column) {
        ArrayList<Double> list = new ArrayList<>();
        for (String[] arr : array) {
            if (matches(arr, teamnumber)) {
                list.add(Double.parseDouble(arr[column]));
            }
        }
        Collections.sort(list);
        if (list.size() % 2 == 1) {
            double median = list.get(list.size() / 2);
            if (median > maxMedians[column])
                maxMedians[column] = median;
            return median;
        } else {
            int temp = list.size() / 2;
            double median = (list.get(temp - 1) + list.get(temp)) / 2;
            if (median > maxMedians[column])
                maxMedians[column] = median;
            return median;
        }
    }


    public static double calculateAverage(String type, int position, String teamnumber) {
        double temp[] = calculateSumAndCount(type, position, teamnumber);
        double count = temp[COUNT_POSITION];
        double total = temp[TOTAL_POSITION];

        return total / count;
    }//2 part so other method can be reused

    public static double[] calculateSumAndCount(String type, int position, String teamnumber) {
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
                        total += Double.parseDouble(array[i][position]);
                        count++;
                        /*if(count <=2){
                            count++;
                        }
                        else if(count<5) {
                            total += Double.parseDouble(array[i][position]);
                            count++;
                        }
                        else{
                            total += 2*Double.parseDouble(array[i][position]);
                            count += 2;
                        }*/
                    }
                } catch (Exception e) {//e.printStackTrace();
                }
            }
        }
        elements[COUNT_POSITION] = count;
        elements[TOTAL_POSITION] = total;
        return elements;
    }

    public static double calculateAverageWeightLaters(String type, int position, String teamnumber) {

        double count = 0;
        double total = 0;
        int iter = 0;
        boolean canLevel2 = false;
        if (type == "bool") {
            for (int i = array.length - 1; i >= 0; i--) {
                try {
                    if (matches(array[i], teamnumber)) {//
                        count++;
                        if (array[i][position].equals("true"))
                            total++;
                    }
                } catch (Exception e) {e.printStackTrace();
                }
            }
        } else if (type == "number") {
            for (int i = array.length - 1; i >= 0; i--) {
                try {
                    if (position == getIndex("HAB climb")) {
                        if (matches(array[i], teamnumber)) {
                            count++;
                            //System.out.print("bob");
                            if (array[i][position].equals("6"))
                                canLevel2 = true;

                            /*if (iter < 3) {
                                if (array[i][position].equals("6")) {
                                    total += 2;

                                }
                                count += 2;
                                iter++;
                            } else {
                                if (array[i][position].equals("6")) {
                                    total += 1;

                                }
                                count++;
                                iter++;
                            }*/
                        }
                    } else if (position == getIndex("driver ability")) {
                        if (matches(array[i], teamnumber)) {

                            if (iter < 3) {
                                if (!array[i][position].equals("0")) {
                                    total += 2 * Double.parseDouble(array[i][position]);
                                    count += 2;
                                }
                                iter++;


                            } else {
                                if (!array[i][position].equals("0")) {
                                    total += Double.parseDouble(array[i][position]);
                                    count++;
                                }
                                iter++;

                            }
                        }
                    } else {


                        if (matches(array[i], teamnumber)) {

                            if (iter < 3) {
                                total += 2 * Double.parseDouble(array[i][position]);
                                count += 2;
                            } else {
                                total += Double.parseDouble(array[i][position]);
                                count++;
                            }
                            iter++;
                        }
                    }
                } catch (Exception e) {e.printStackTrace();
                }
            }
        }
        if(canLevel2)
            total+=6*count;
        return total / count;

    }


    public static double calculateCompositeScore(String[] array, String[] vars, double[] values) {
        double total = 0;
        if (vars.length != values.length)
            System.err.println("vars and values don't match" + vars.length + ":" + values.length);
        for (int i = 0; i < vars.length; i++) {
            total += (Double.parseDouble(array[getIndex(vars[i])]) * values[i]);
        }
        return total;
    }


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
        for (int i = 0; i < allVars.length; i++)
            if (allVars[i] == item)
                index = i;
        return index;
    }//index of variable within the variables array

    public static boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    public static boolean matches(String[] team, String teamnumber) {//each array has a corresponding team
        return (team[0].split("-")[0].equals(teamnumber));
    }

    public static boolean isLessThan(String num1, double num2) {//unique comparison useful for the wendex calculations
        return (Double.parseDouble(num1) < num2);
    }

    public static int getTeamsCount() {
        int teamsCount = 0;
        for (File file0 : folder.listFiles())
            if (file0.getName().matches("team\\d(.*)"))//some files, like the .xlsx(the spreadsheet), is not wanted
                teamsCount++;
        return teamsCount;
    }
}
