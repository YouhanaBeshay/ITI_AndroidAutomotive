package oopcalculator.strategies;

public interface IOperationStrategy {
    double execute(double a, double b);
    String getOperator();

}
