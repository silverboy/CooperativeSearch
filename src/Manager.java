import com.ugr.search.algorithms.BestSolution;
import com.ugr.search.algorithms.tabu.TabuSearch;

import java.util.HashMap;

public class Manager {

    private boolean cooperative;
    private int instances;
    private int numberOfEvaluations;
    private int tabuListSize;
    private int[] performances;
    private int[][] costs;
    private int[] constraints;
    private BestSolution bestSolution;
    private int optimalValue;


    public Manager(int[] performances, int[][] costs, int[] constraints,
                   HashMap<String, Integer> params, int optimalValue,
                   boolean  cooperative, int instances, int numberOfEvaluations,
                   int tabuListSize) {
        this.performances = performances;
        this.costs = costs;
        this.constraints = constraints;
        this.optimalValue = optimalValue;
        this.cooperative = cooperative;
        this.instances = instances;
        this.numberOfEvaluations = numberOfEvaluations;
        this.tabuListSize = tabuListSize;
    }

    public void start() {
        for(int i = 0; i < instances; i++) {
        }
    }


}
