package oopcalculator.factory;

import java.util.HashMap;
import java.util.Map;

// all the strategies
import oopcalculator.strategies.*;

public class OperationFactory {

    private static final Map<Integer, IOperationStrategy> operationMap = new HashMap<>();
    
    static {
        operationMap.put(1, new Add());
        operationMap.put(2, new Subtract());
        operationMap.put(3, new Multiply());
        operationMap.put(4, new Divide());
    }

    public IOperationStrategy getStrategy(int key) {
        return operationMap.get(key);
    }

    public boolean isValidStrategy(int key) {
        return operationMap.containsKey(key);
    }
  
    
}
