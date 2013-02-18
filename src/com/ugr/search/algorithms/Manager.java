package com.ugr.search.algorithms;

import com.ugr.search.algorithms.BestSolution;
import com.ugr.search.algorithms.tabu.TabuSearch;

import java.util.*;

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
    private List<Algorithm> tabuSearches;
    private CooperativeInfo cooperativeInfo;

    private Vector<Double> evolution;


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
        this.tabuSearches = new ArrayList<Algorithm>();
        this.cooperativeInfo = new CooperativeInfo(instances);
    }

    public Manager(Parser parser,List<HashMap<String,Integer>> algorithm,int totalEvaluationsLimit){
        Algorithm myAlgorithm;
        int id=0;
        int eval=totalEvaluationsLimit/algorithm.size();
        for(HashMap hM:algorithm) {
            if (hM.get(Params.TYPE) == Params.TABUSEARCH) {

                myAlgorithm = new TabuSearch(id, parser.getPerformances(), parser.getCosts(),
                        parser.getConstraints(), hM, parser.getOptimalValue(), eval);
                tabuSearches.add(myAlgorithm);
                myAlgorithm.enableMonitoring(25);
                myAlgorithm.start();
            }
            id++;
        }
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



    public void calculateGroupEvolution(){

        Vector<Double> myV=tabuSearches.get(0).getBestSolution().getEvolution();
        Double[] evol=new Double[myV.size()];
        myV.copyInto(evol);

        for(int i=1;i<tabuSearches.size();i++){
            myV=tabuSearches.get(i).getBestSolution().getEvolution();

            int j=0;
            for(Double element:myV){
                evol[j]=Math.max(evol[j],element);
                j++;
            }
        }

        evolution= new Vector<Double>(Arrays.asList(evol));
    }

    public void groupJoin() throws InterruptedException {

        for(Algorithm element:tabuSearches){
            element.join();
        }
    }

    public Vector<Double> getEvolution(){
        return evolution;
    }




}
