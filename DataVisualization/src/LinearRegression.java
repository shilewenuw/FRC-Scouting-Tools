import java.util.ArrayList;

/**
 * Created by pickl on 3/19/2018.
 */
public class LinearRegression {
    private boolean isRegressionReady = false;
    private int n;
    private double slope, intercept;
    private ArrayList<Double> xValues;
    private ArrayList<Double> yValues;
    public LinearRegression(){
        xValues = new ArrayList<>();
        yValues = new ArrayList<>();
    }
    public void addPoint(double x, double y){
        xValues.add(x);
        yValues.add(y);
    }
    public void calculateLinearRegression(){
        n = xValues.size();
        double xSummation = 0;
        double ySummation = 0;
        double xySummation = 0;
        double xxSummation = 0;
        for(int i=0;i<n;i++){
            xSummation += xValues.get(i);
            ySummation += yValues.get(i);
            xySummation += xValues.get(i) * yValues.get(i);
            xxSummation += xValues.get(i) * xValues.get(i);
        }
        slope =( n * xySummation - xSummation * ySummation)/
                ( n * xxSummation - xSummation * xSummation);
        intercept = ( ySummation - slope * xSummation ) / n ;
        isRegressionReady = true;
    }
    public double getSlope(){
        return slope;
    }
    public double getIntercept(){
        return intercept;
    }
    public double predict(double input){
        return input*slope + intercept;
    }

}
