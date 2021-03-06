import org.jfree.chart.*;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by pickl on 3/8/2018.
 */
public class AveragesPanel {
    ChartPanel chartPanel;
    ArrayList<String[]> teamsData;
    /*static String[] vars = {
            "Hab lvl 2", "auto hatch", "auto cargo",
            "auto h1", "auto h2", "auto h3", "auto c1", "auto c2", "auto c3",
            "tele hatch", "tele cargo",
            "tele h1", "tele h2", "tele h3", "tele c1", "tele c2", "tele c3",
            "HAB climb", "driver ability"
    };*/
    static String[] vars = {
             "total hatches", "total cargo", "ship hatches", "ship cargo",
            "HAB climb", "driver ability"
    };
    static String[] allvars;
    AveragesPanel(String[] teams, JPanel superPanel){
        this(teams, superPanel, -1);
    }
    AveragesPanel(String[] teams, JPanel superPanel, double yRange){
        teamsData = new ArrayList<>();

        GetArrayFromDB getArrayFromDB = new GetArrayFromDB(1);
        allvars = getArrayFromDB.variables;
        String[][] array = getArrayFromDB.array;
        for(String[] arr: array){
            try{if (containsTeams(teams, arr[0].split("-")[0].split("m")[1])){
                teamsData.add(arr);
            }}catch (Exception e){}
        }
        String teamtitle = "";
        for(String team:teams){
            teamtitle+=team + " ";
        }
        Font font = new Font("Dialog", Font.PLAIN, 20 );
        CategoryPlot plot = new CategoryPlot();//contains the barchart and linechart in one
        plot.setDomainAxis(new CategoryAxis());//x-axis
        plot.setDataset(createDataset());//auto indexed at 0, later comment explains why
        plot.setRenderer(new BarRenderer());//for bar chart
        plot.setRangeAxis(new NumberAxis());//y-axis
        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        plot.getDomainAxis().setTickLabelFont(font);
        plot.getRangeAxis().setTickLabelFont(font);
        if(yRange!=-1)
            plot.getRangeAxis().setRange(0, yRange);
        /*JFreeChart barChart = ChartFactory.createBarChart(
                teamtitle,
                "Averages","Score",
                createDataset(),
                PlotOrientation.VERTICAL,
                false,false,false);*/
        JFreeChart chart = new JFreeChart(plot);
        chart.removeLegend();
        chart.setTitle(teamtitle);
        chartPanel = new ChartPanel(chart);
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
        chartPanel.setBorder(BorderFactory.createLineBorder(Color.green));
        (new ComponentMover()).registerComponent(chartPanel);
        chartPanel.setMouseZoomable(false);
        chartPanel.setPreferredSize( new Dimension( 570 , 430 ) );
    }
    public DefaultCategoryDataset createDataset(){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for(String var:vars){
            dataset.addValue(calculateSum(teamsData, getIndex(var)),"",var);
            //System.out.print(getIndex(var));
        }
        return dataset;
    }
    public boolean containsTeams(String[] teams, String arrTeam){
        boolean contains = false;
        for(String team: teams){
            if(team.equals(arrTeam))
                contains = true;
        }
        return contains;

    }
    public static int getIndex(String item) {
        int index = 0;
        for (int i = 0; i < allvars.length; i++)
            if (allvars[i].equals(item))
                index = i;
        return index;
    }
    public double calculateSum(ArrayList<String[]> list, int index){
        double total = 0;
        for(String[] arr:list){
            //System.out.print(index);
            try{total+=Double.parseDouble(arr[index]);}catch (Exception e){System.out.print(arr[index]);}
        }
        return total;
    }

}
