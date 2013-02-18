/**
 * Created with IntelliJ IDEA.
 * User: Javi
 * Date: 17/02/13
 * Time: 2:54
 * To change this template use File | Settings | File Templates.
 */

package com.ugr.search.algorithms;

import com.ugr.search.algorithms.tabu.TabuSearch;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class AlgorithmGroup {
    private Vector<Algorithm> instances;
    private Vector<Double> evolution;

    // constants for algorithm type
    public static final Integer TABUSEARCH=0;


    public AlgorithmGroup(Parser parser,List<HashMap<String,Integer>> algorithm,int totalEvaluationsLimit){
        Algorithm myAlgorithm;
        int id=0;
        int eval=totalEvaluationsLimit/algorithm.size();
        for(HashMap hM:algorithm) {
            if (hM.get("type") == TABUSEARCH) {

                myAlgorithm = new TabuSearch(id, parser.getPerformances(), parser.getCosts(),
                        parser.getConstraints(), hM, parser.getOptimalValue(), eval);
                instances.addElement(myAlgorithm);
                myAlgorithm.enableMonitoring(25);
                myAlgorithm.start();
            }
            id++;
        }
    }

    public void calculateGroupEvolution(){

        Vector<Double> myV=instances.elementAt(0).getBestSolution().getEvolution();
        Double[] evol=new Double[myV.size()];
        myV.copyInto(evol);

        for(int i=1;i<instances.size();i++){
            myV=instances.elementAt(i).getBestSolution().getEvolution();

            int j=0;
            for(Double element:myV){
                evol[j]=Math.max(evol[j],element);
                j++;
            }
        }

        evolution= new Vector<Double>(Arrays.asList(evol));
    }

    public void groupJoin() throws InterruptedException {

        for(Algorithm element:instances){
            element.join();
        }
    }

    public Vector<Double> getEvolution(){
        return evolution;
    }

}
