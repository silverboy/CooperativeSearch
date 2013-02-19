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
    private int evaluations;

    public CooperativeInfo(int instances, int evaluations) {
        this.instances = instances;
        changeCurrentSolution = new boolean[instances];
        algorithmSolutions = new BestSolution[instances];
        this.evaluations = evaluations;
        evaluationTime = evaluations / instances;
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
        for(int i = 0; i < changeCurrentSolution.length; i++) {
            changeCurrentSolution[i] = false;
        }
        BestSolution bestSolution = getBestSolution();
        int id = 0;
        for(Vector<Integer> currentFitnessEval : fitnessAlgorithmEvaluations) {
            int evolution = 0;
            if((currentFitnessEval.get(0) - currentFitnessEval.get(4) < 250) &&
                    bestSolution.getFitness() - algorithmSolutions[id].getFitness() > 250){
                algorithmSolutions[id] = new BestSolution(bestSolution.getOptimalFitness());
                algorithmSolutions[id].setValue(Knapsack.copyKnapsackItems(bestSolution.getValue()));
                algorithmSolutions[id].setFitness(bestSolution.getFitness());
                algorithmSolutions[id].setNumEvaluationFitness(bestSolution.getNumEvaluationFitness());
                changeCurrentSolution[id] = true;
            }
            id++;
        }
        continueExecution = true;
    }

    public BestSolution getBestSolution() {
        BestSolution bestSolution = new BestSolution(algorithmSolutions[0].getOptimalFitness());
        bestSolution.setValue(Knapsack.copyKnapsackItems(algorithmSolutions[0].getValue()));
        bestSolution.setFitness(algorithmSolutions[0].getFitness());
        bestSolution.setNumEvaluationFitness(algorithmSolutions[0].getNumEvaluationFitness());
        for(int i = 1; i < algorithmSolutions.length; i++) {
            BestSolution currentSolution = algorithmSolutions[i];
            if(bestSolution.getFitness() < currentSolution.getFitness()) {
                bestSolution.setValue(Knapsack.copyKnapsackItems(currentSolution.getValue()));
                bestSolution.setFitness(currentSolution.getFitness());
                bestSolution.setNumEvaluationFitness(currentSolution.getNumEvaluationFitness());
            }
        }
        return bestSolution;
    }

    public int getEvaluations() {
        return evaluations;
    }

    public boolean solutionChange(int id) {
        return changeCurrentSolution[id];
    }
}
