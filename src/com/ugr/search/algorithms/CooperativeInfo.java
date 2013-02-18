package com.ugr.search.algorithms;

import com.ugr.search.algorithms.BestSolution;

import java.util.Vector;

public class CooperativeInfo {

    private boolean[] changeCurrentSolution;
    private  int instances;
    private BestSolution[] algorithmSolutions;
    private int evaluationTime;
    private int instancesStopped;
    private int instancesFinished;
    private boolean continueExecution;
    private Vector<Integer>[] fitnessAlgorithmEvaluations;

    public CooperativeInfo(int instances, int evaluations) {
        this.instances = instances;
        changeCurrentSolution = new boolean[instances];
        algorithmSolutions = new BestSolution[instances];
        evaluationTime = evaluations / 4;
        instancesStopped = 0;
        instancesFinished = 0;
        continueExecution = true;
        fitnessAlgorithmEvaluations = new Vector[instances];
    }

    public int getEvaluationTime() {
        return evaluationTime;
    }


    public void changeInformation(int id, BestSolution bestSolution, Vector<Integer> fitnessEvaluations) {
        continueExecution = false;
        algorithmSolutions[id] = bestSolution;
        fitnessAlgorithmEvaluations[id] = fitnessEvaluations;
        instancesStopped++;
    }

    public boolean allInstancesStopped() {
        return instancesStopped == instances;
    }

    public boolean allInstancesFinished() {
        return instancesFinished == instances;
    }

    public void finishExecution(int id, BestSolution bestSolution) {
        instancesFinished++;
        algorithmSolutions[id] = bestSolution;
    }

    public boolean continueExecution() {
        return continueExecution;
    }

    public BestSolution getAlgorithmSolution(int id) {
        return algorithmSolutions[id];
    }

    public void updateAlgorithmSolution() {
        BestSolution bestSolution = getBestSolution();
        int id = 0;
        for(Vector<Integer> currentFitnessEval : fitnessAlgorithmEvaluations) {
            int evolution = 0;
            if((currentFitnessEval.get(0) - currentFitnessEval.get(4) < 250) &&
                    bestSolution.getFitness() - algorithmSolutions[id].getFitness() > 250){
                algorithmSolutions[id] = bestSolution;
            }
            id++;
        }
        continueExecution = true;
    }

    public BestSolution getBestSolution() {
        BestSolution bestSolution = algorithmSolutions[0];
        for(BestSolution currentSolution : algorithmSolutions) {
            if(bestSolution.getFitness() < currentSolution.getFitness()) {
                bestSolution = currentSolution;
            }
        }
        return bestSolution;
    }
}
