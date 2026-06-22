package oopcalculator.strategies;

public class Add implements IOperationStrategy {

    public double execute(double a, double b) {
        return a + b;
    }

    public String getOperator() {
        return "+";
    }

}
