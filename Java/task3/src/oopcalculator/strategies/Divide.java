package oopcalculator.strategies;

public class Divide implements IOperationStrategy {

    public String getOperator() {
        return "/";
    }

    public double execute(double a, double b) {

        if (b == 0) {
            throw new ArithmeticException("\nCannot divide by zero my man");
        }
        return a / b;

    }
}
