import org.jfree.chart.*;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.*;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.TextAnchor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by pickl on 1/30/2018.
 */
public class NickCromparePanel extends ApplicationFrame{
    public String[][] arrayLmao;
    public String[][] rawArray;
    public String[] variables;
    public int column;
    public ChartPanel chartPanel;
    public String[] teams;
    //JPanel panel;
    ///private double maxNum;

    public NickCromparePanel(String variable, JPanel superPanel){
        this(variable, (new GetArrayFromDB(2)).teams, superPanel);

    }
    public NickCromparePanel(String variable, String[] teams, JPanel superPanel){
        super("");
        GetArrayFromDB getArrayFromDB = new GetArrayFromDB(2);
        arrayLmao = getArrayFromDB.array;
        variables = getArrayFromDB.variables;
        rawArray = (new GetArrayFromDB(0)).array;
        this.teams = teams;
        System.out.println(teams.length);
        column = Arrays.asList(variables).indexOf(variable);//get position in array at which target variable is located
        /*for(String str:variables)
            System.out.print(str);
        System.out.print(column);*/
        bubbleSort(column);
        /*JFreeChart barChart = ChartFactory.createBarChart(
                variable,
                "Team",
                "Score",
                createDataset(),
                PlotOrientation.VERTICAL,
                false, false, false);*/
        CategoryPlot plot = new CategoryPlot();//contains the barchart and linechart in one
        plot.setDomainAxis(new CategoryAxis("Team"));//x-axis
        plot.setDataset(0, createDataset());//auto indexed at 0, later comment explains why

        BarRenderer renderer = new BarRenderer();
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);
        ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
                TextAnchor.BOTTOM_CENTER);
        renderer.setBasePositiveItemLabelPosition(position);

        plot.setRenderer(0, renderer);//for bar chart
        plot.setRangeAxis(new NumberAxis("Score"));//y-axis
        ///plot.setDataset(0, getVariableThreeNumSummaryOfTeamDataSet(column));//needs multiple indexes if multiple datasets
        //plot.mapDatasetToRangeAxis(1, 0);//maps the dataset at index one to the same range axis as the range for the bar chart
        //plot.setRenderer(1, new MinMaxCategoryRenderer());
        ///plot.setRenderer(0, new MinMaxCategoryRenderer());
        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        //plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(95));
        ///plot.setDataset(1, medianDataset(column));
        ///plot.setRenderer(1, new LineAndShapeRenderer());

        JFreeChart chart = new JFreeChart(plot);//chart holds the plot
        chart.setTitle(variable);
        chart.removeLegend();

        chartPanel = new ChartPanel( chart);//chartpanel holds chart, like a JPanel, but specially for charts
        chartPanel.addChartMouseListener(new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent chartMouseEvent) {
                if(Global.delete) {
                    superPanel.remove(chartPanel);
                    superPanel.validate();
                    superPanel.repaint();
                }
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent chartMouseEvent) {

            }
        });

        chartPanel.setPreferredSize(new Dimension( 1800 , 1000 ) );
        //chartPanel.setPreferredSize(new java.awt.Dimension( 560 , 367 ) );
        //setContentPane( chartPanel );
        chartPanel.setBorder(BorderFactory.createLineBorder(Color.blue));
        chartPanel.setMouseZoomable(false);//you can zoom in on graph to see closer, but it is not needed for our case
        (new ComponentMover()).registerComponent(chartPanel);//this object registers panels to make them moveable
        /*panel = new JPanel();
        panel.add(chartPanel);
        chartPanel.addChartMouseListener(new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent chartMouseEvent) {
                panel.remove(chartPanel);
                panel.validate();
                repaint();
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent chartMouseEvent) {

            }
        });*/


    }



    public void bubbleSort(int column)// this will sort the teams by best to worst
    {
        for (int i = (arrayLmao.length - 1); i >= 0; i--) {
            for (int j = 1; j <= i; j++) {
                try{if (parse(arrayLmao[j - 1][column]) < parse(arrayLmao[j][column])) {
                    String[] temp = arrayLmao[j - 1];
                    arrayLmao[j - 1] = arrayLmao[j];
                    arrayLmao[j] = temp;
                }}catch (Exception e){System.err.print(i+":"+j+" "+arrayLmao.length);}
            }
        }
    }
    public double calculateAverage(){
        double sum = 0;
        double count = 0;
        for (String[] array:arrayLmao){
            if(Arrays.asList(teams).contains(array[0])) {
                sum += parse(array[column]);
                count++;
            }
        }
        return sum/count;
    }
    private CategoryDataset createDataset(){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int count = 0;

        int numTeams =30;
        try{
            numTeams = (int) Double.parseDouble(OptionPanel.scaleInput.getText());
        }catch (Exception e){}
        for(String[] array:arrayLmao) {
            //if (Arrays.asList(teams).contains(array[0])) {
                try {
                    if(count==0)
                        System.out.println(parse(array[column]));
                    if(count<numTeams)
                        dataset.addValue((double)Math.round(parse(array[column])*100)/100, "", getTeamNumber(array));
                    count++;
                } catch (Exception e) {
                }
            //}
        }
        return dataset;
    }
    private CategoryDataset getVariableThreeNumSummaryOfTeamDataSet(int column){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int count = 0;
        for(String[] array:arrayLmao){
            if(count<30){
                double[] threeNumSummary = getVariableSummaryOfTeam(getTeamNumber(array), column);
                dataset.addValue(threeNumSummary[0], "Q1", getTeamNumber(array));
                dataset.addValue(threeNumSummary[1], "Med", getTeamNumber(array));
                dataset.addValue(threeNumSummary[2], "Q3", getTeamNumber(array));
                count++;
            }
        }
        return dataset;

    }
    private CategoryDataset medianDataset(int column){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int count = 0;
        for(String[] array:arrayLmao){
            if(count<30){
                double[] threeNumSummary = getVariableSummaryOfTeam(getTeamNumber(array), column);
                dataset.addValue(threeNumSummary[1], "Med", getTeamNumber(array));
                count++;
            }
        }
        return dataset;

    }


    private double[] getVariableSummaryOfTeam(String team, int column){
        ArrayList<Double> list = new ArrayList();
        for(String[] array: rawArray){
            if(getTeamNumber(array).equals(team)){
                list.add(parse(array[column]));
            }
        }
        Double[] numbers = list.toArray(new Double[list.size()]);
        Arrays.sort(numbers);
        double median = getMiddleNum(numbers);
        double firstQuartile = getMiddleNum(Arrays.copyOfRange(numbers, 0, (numbers.length) / 2));
        double thirdQuartile;
        // Q3
        if (numbers.length % 2 == 0) {
            thirdQuartile = getMiddleNum(Arrays.copyOfRange(numbers, (numbers.length) /
                    2, numbers.length));
        } else {
            thirdQuartile = getMiddleNum(Arrays.copyOfRange(numbers, ((numbers.length) / 2) +
                    1, numbers.length));
        }
        return new double[]{firstQuartile, median, thirdQuartile};
    }


    double getMiddleNum(Double[] num) {
        int middle = (num.length - 1) / 2;

        if (num.length % 2 == 0) {
            double num1 = num[middle];
            double num2 = num[middle + 1];
            return (num1 + num2) / 2;
        } else {
            return num[middle];
        }
    }

    public static double parse(String d) {
        return Double.parseDouble(d);
    }
    public String getTeamNumber(String[] array){
        return array[0].split("-")[0].split("m")[1];
    }
}
