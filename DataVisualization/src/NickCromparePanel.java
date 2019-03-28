import org.jfree.chart.*;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.TextAnchor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Arrays;


/**
 * Created by pickl on 1/30/2018.
 */
public class NickCromparePanel extends ApplicationFrame implements TokenInterface{
    public String[][] arrayLmao;
    public String[] variables;
    public int column;
    public ChartPanel chartPanel;
    public String[] teams;
    JPanel panel;
    private double maxNum;

    public NickCromparePanel(String variable, JPanel superPanel){
        this(variable, (new GetArrayFromDB(1)).teams, superPanel);

    }
    public NickCromparePanel(String variable, String[] teams, JPanel superPanel){
        super("");
        GetArrayFromDB getArrayFromDB = new GetArrayFromDB(1);
        arrayLmao = getArrayFromDB.array;
        variables = getArrayFromDB.variables;
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
        plot.setDataset(createDataset());//auto indexed at 0, later comment explains why

        BarRenderer renderer = new BarRenderer();
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);
        ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
                TextAnchor.BOTTOM_CENTER);
        renderer.setBasePositiveItemLabelPosition(position);

        plot.setRenderer(renderer);//for bar chart
        plot.setRangeAxis(new NumberAxis("Score"));//y-axis
        plot.setDataset(1, averageLineDataset());//needs multiple indexes if multiple datasets
        plot.mapDatasetToRangeAxis(1, 0);//maps the dataset at index one to the same range axis as the range for the bar chart
        plot.setRenderer(1, new LineAndShapeRenderer());
        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        //plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(95));


        JFreeChart chart = new JFreeChart(plot);//chart holds the plot
        chart.setTitle(variable);

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
        for(String[] array:arrayLmao) {
            if (Arrays.asList(teams).contains(array[0])) {
                try {
                    if(count==0)
                        System.out.println(parse(array[column]));
                    if(count<30)
                        dataset.addValue((double)Math.round(parse(array[column])*10)/10, "", getTeamNumber(array));
                    count++;
                } catch (Exception e) {
                }
            }
        }
        return dataset;
    }
    private CategoryDataset averageLineDataset(){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        double avg = calculateAverage();

        for(String[] array:arrayLmao){
            dataset.addValue(avg, "AVGALL", getTeamNumber(array));
            /*if(Arrays.asList(teams).contains(array[0]))
                dataset.addValue(avg, "AVGSELECT", getTeamNumber(array));*/
        }
        return dataset;
    }

    public static double parse(String d) {
        return Double.parseDouble(d);
    }
    public String getTeamNumber(String[] array){
        return array[0].split("-")[0].split("m")[1];
    }
}
