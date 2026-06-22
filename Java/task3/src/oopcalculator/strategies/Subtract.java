package oopcalculator.strategies;

public class Subtract implements IOperationStrategy {
    
    public String getOperator() {
        return "-";
    }
    
    public double execute(double a, double b) {
        return a - b;
    }
    
}
