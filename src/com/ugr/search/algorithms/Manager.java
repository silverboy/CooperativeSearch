package com.ugr.search.algorithms;

import com.ugr.search.algorithms.BestSolution;
import com.ugr.search.algorithms.tabu.TabuSearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private Parser parser;
    private List<HashMap<String,Integer>> params;
    private List<TabuSearch> tabuSearches;
    private CooperativeInfo cooperativeInfo;


    public Manager(int[] performances, int[][] costs, int[] constraints,
                   List<HashMap<String,Integer>> params, int optimalValue,
                   boolean  cooperative, int instances, int numberOfEvaluations,
                   int tabuListSize, Parser parser) {
        this.performances = performances;
        this.costs = costs;
        this.constraints = constraints;
        this.optimalValue = optimalValue;
        this.cooperative = cooperative;
        this.instances = instances;
        this.numberOfEvaluations = numberOfEvaluations;
        this.tabuListSize = tabuListSize;
        this.parser = parser;
        this.params = params;
        this.tabuSearches = new ArrayList<TabuSearch>();
        this.cooperativeInfo = new CooperativeInfo(instances);
    }

    public void start() {
        for(int i = 0; i < instances; i++) {
            TabuSearch tabuSearch = new TabuSearch(i, parser.getPerformances(),
                    parser.getCosts(), parser.getConstraints(), params.get(i),
                    parser.getOptimalValue(), 500);
            tabuSearch.setCooperativeExecution(cooperativeInfo);
            tabuSearches.add(tabuSearch);
            tabuSearch.start();
        }
    }


}
