import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by pickl on 2/1/2018.
 */
public class EveryDayBro extends ApplicationFrame {
    String[] variables;
    String[][] rawArray;
    ArrayList<String[]> teamData;
    int column;
    ChartPanel chartPanel;
    JPanel panel;
    int column1;
    int column2;
    String variable1;
    String variable2;
    public EveryDayBro(String team, String variable, JPanel superPanel){
        super("");
        GetArrayFromDB page1 = new GetArrayFromDB(0);
        variables = page1.variables;
        rawArray = page1.array;
        column = Arrays.asList(variables).indexOf(variable);
        teamData = new ArrayList<>();

        for(String[] array: rawArray){
            try{if (team.equals(array[0].split("m")[1].split("-")[0])){
                teamData.add(array);
            }}catch (Exception e){e.printStackTrace();}
        }
        sortArrayByTime();
        double avg = calculateAvg(teamData, column);

        JFreeChart lineChart = ChartFactory.createLineChart(
                team + ": " + variables[column],
                "Time","Score",
                createDataset(avg, column),
                PlotOrientation.VERTICAL,
                true,false,false);

        chartPanel = new ChartPanel(lineChart);
        chartPanel.setBorder(BorderFactory.createLineBorder(Color.red));
        (new ComponentMover()).registerComponent(chartPanel);

        chartPanel.setMouseZoomable(false);
        chartPanel.setPreferredSize( new Dimension( 560 , 367 ) );
        /*panel = new JPanel();
        panel.add(chartPanel);*/
        chartPanel.addChartMouseListener(new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent chartMouseEvent) {
                if(Global.delete) {
                    superPanel.remove(chartPanel);
                    superPanel.revalidate();
                    superPanel.repaint();
                }
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent chartMouseEvent) {

            }
        });
    }
    public EveryDayBro(String team, String variable1, String variable2, JPanel superPanel){
        super("");
        this.variable1 = variable1;
        this.variable2 = variable2;
        GetArrayFromDB page1 = new GetArrayFromDB(0);
        variables = page1.variables;
        rawArray = page1.array;
        column1 = Arrays.asList(variables).indexOf(variable1);
        column2 = Arrays.asList(variables).indexOf(variable2);
        teamData = new ArrayList<>();

        for(String[] array: rawArray){
            try{if (team.equals(array[0].split("m")[1].split("-")[0])){
                teamData.add(array);
            }}catch (Exception e){e.printStackTrace();}
        }
        sortArrayByTime();
        JFreeChart lineChart = ChartFactory.createLineChart(
                team + ": " + variables[column],
                "Time","Score",
                createDataset(column1, column2),
                PlotOrientation.VERTICAL,
                true,false,false);

        chartPanel = new ChartPanel(lineChart);
        chartPanel.setBorder(BorderFactory.createLineBorder(Color.red));
        (new ComponentMover()).registerComponent(chartPanel);

        chartPanel.setMouseZoomable(false);
        chartPanel.setPreferredSize( new Dimension( 560 , 367 ) );
        /*panel = new JPanel();
        panel.add(chartPanel);*/
        chartPanel.addChartMouseListener(new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent chartMouseEvent) {
                if(Global.delete) {
                    superPanel.remove(chartPanel);
                    superPanel.revalidate();
                    superPanel.repaint();
                }
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent chartMouseEvent) {

            }
        });
    }
    private DefaultCategoryDataset createDataset(double avg, int column){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for(String[] arr:teamData){
            dataset.addValue(parse(arr[column]),"",arr[0].split("-")[1] );
            dataset.addValue(avg,"Avg",arr[0].split("-")[1] );
        }
        return dataset;
    }
    private  DefaultCategoryDataset createDataset(int column1, int column2){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for(String[] arr:teamData){
            String mn = arr[0].split("-")[1];
            dataset.addValue(parse(arr[column1]),variable1, mn);
            dataset.addValue(parse(arr[column2]), variable2, mn);
        }
        return dataset;
    }

    public void sortArrayByTime(){

        for (int i = (teamData.size() - 1); i >= 0; i--)
        {
            for (int j = 1; j <= i; j++)
            {
                if (!timeIsLesser(j-1, j))
                {
                    String[] temp = teamData.get(j-1);
                    teamData.set(j-1, teamData.get(j));
                    teamData.set(j, temp);
                } } }
    }
    /*public boolean timeIsLesser(int x, int y){//
        return ( (getDay(x)<=getDay(y)) &&
                (getMatchNumber(x)<getMatchNumber(y)) );
    }*/
    public boolean timeIsLesser(int x, int y){
        return (getMatchNumber(x)<getMatchNumber(y));
    }
    public static double parse(String i) {
        return Double.parseDouble(i);
    }
    public static int parseI(String i){
        return Integer.parseInt(i);
    }
    public int getMatchNumber(int i){
        return parseI(teamData.get(i)[0].split("-")[1]);
    }
    public int getDay(int i){
        return parseI(teamData.get(i)[0].split("-")[2]);
    }
    public double calculateAvg(ArrayList<String[]> list, int column){
        double total = 0;
        for(String[] arr:list){
            total+=parse(arr[column]);
        }
        return total/list.size();
    }
}
