package com.ugr.search.algorithms;

import com.ugr.search.algorithms.BestSolution;
import com.ugr.search.algorithms.tabu.TabuSearch;

import java.util.*;

public class Manager extends Thread{

    private int instances;
    private Parser parser;
    private List<HashMap<String,Integer>> params;
    private List<Algorithm> tabuSearches;
    private CooperativeInfo cooperativeInfo;
    private int optimalValue;
    private int groupMonitorStep;
    private int totalEvaluationsLimit;

    private Vector<Double> evolution;


    public Manager(Parser parser, boolean cooperative, int instances, int totalEvaluationsLimit,
                   List<HashMap<String, Integer>> params,int CooperationStep ,int groupMonitorStep) {
        this.optimalValue = parser.getOptimalValue();
        this.instances = instances;
        this.parser = parser;
        this.params = params;
        this.tabuSearches = new ArrayList<Algorithm>();
        this.groupMonitorStep = groupMonitorStep;
        this.totalEvaluationsLimit=totalEvaluationsLimit;
        this.cooperativeInfo = new CooperativeInfo(instances, CooperationStep);
    }

    public Manager(Parser parser,List<HashMap<String,Integer>> algorithm,int totalEvaluationsLimit,
                   int groupMonitorStep){
        Algorithm myAlgorithm;
        int id=0;
        int evaluations=totalEvaluationsLimit/algorithm.size();
        int step=groupMonitorStep/algorithm.size();
        tabuSearches=new ArrayList<Algorithm>();

        for(HashMap hM:algorithm) {
            if (hM.get(Params.TYPE) == Params.TABUSEARCH) {
                myAlgorithm = new TabuSearch(id, parser.getPerformances(), parser.getCosts(),
                        parser.getConstraints(), hM, parser.getOptimalValue(), evaluations);
                tabuSearches.add(myAlgorithm);
                myAlgorithm.enableMonitoring(step);
                myAlgorithm.start();
            }
            id++;
        }
    }

    public void run() {
        int step=groupMonitorStep/instances;
        int evaluations=totalEvaluationsLimit/instances;
        for(int i = 0; i < instances; i++) {
            TabuSearch tabuSearch = new TabuSearch(i, parser.getPerformances(),
                    parser.getCosts(), parser.getConstraints(), params.get(i),
                    parser.getOptimalValue(), evaluations);
            tabuSearch.setCooperativeExecution(cooperativeInfo);
            tabuSearches.add(tabuSearch);
            tabuSearch.enableMonitoring(step);
            tabuSearch.start();
        }

        while(!cooperativeInfo.allInstancesFinished()) {
            if(cooperativeInfo.allInstancesStopped()) {
                cooperativeInfo.updateAlgorithmSolution();
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
        //IMPRIMIR BEST SOLUTION
        cooperativeInfo.getBestSolution();
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
