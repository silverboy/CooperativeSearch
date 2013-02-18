package com.ugr.search.algorithms;

import com.ugr.search.algorithms.BestSolution;

public class CooperativeInfo {

    private boolean pause = false;
    private BestSolution bestSolution;
    private boolean[] changeCurrentSolution;
    private  int instances;
    private BestSolution[] algorithmSolutions;

    public CooperativeInfo(int instances) {
        this.instances = instances;
        changeCurrentSolution = new boolean[instances];
        algorithmSolutions = new BestSolution[instances];
    }

    public boolean  stop() {
        return pause;
    }


    public void stopExecutions() {
        pause = true;
    }

    public void continueExecutions() {
        pause = false;
    }

    public void updateCooperativeInfo(BestSolution bestSolution, boolean[] changeCurrentSolution) {
        this.bestSolution = bestSolution;
        this.changeCurrentSolution = changeCurrentSolution;
    }

}
