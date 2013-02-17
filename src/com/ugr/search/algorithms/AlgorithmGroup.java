/**
 * Created with IntelliJ IDEA.
 * User: Javi
 * Date: 17/02/13
 * Time: 2:54
 * To change this template use File | Settings | File Templates.
 */

package com.ugr.search.algorithms;

import com.ugr.search.algorithms.tabu.TabuSearch;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class AlgorithmGroup {
    private Vector<Algorithm> instances;
    private Vector<Double> evolution;

    // constants for algorithm type
    public static final Integer TABUSEARCH=0;


    public AlgorithmGroup(Parser parser,List<HashMap<String,Integer>> algorithm,int evalLimit){
        Algorithm myAlgorithm;
        int id=0;
        for(HashMap hM:algorithm) {
            if (hM.get("type") == TABUSEARCH) {

                myAlgorithm = new TabuSearch(id, parser.getPerformances(), parser.getCosts(),
                        parser.getConstraints(), hM, parser.getOptimalValue(), evalLimit);
                instances.addElement(myAlgorithm);
            }
            id++;
        }
    }

}
