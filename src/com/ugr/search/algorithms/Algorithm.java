package com.ugr.search.algorithms;

public abstract class Algorithm extends Thread {

    private int id;
    private int[] performances;
    private int[][] costs;
    private int[] constraints;
    private int[] currentSolution;
    private int currentSolutionFitness;
    private BestSolution bestSolution;
    private int optimalValue;
    private int fitnessEvalCount;
    private int fitnessEvalLimit;

    // Monitoring variables
    private boolean monitor=false;
    private int evalStep;

    private boolean cooperative = false;
    private CooperativeInfo cooperativeInfo = null;

    public Algorithm(int id, int[] performances, int[][] costs, int[] constraints, int optimalValue, int fitnessEvalLimit) {
        this.id = id;
        this.performances = performances;
        this.costs = costs;
        this.constraints = constraints;
        this.optimalValue = optimalValue;
        this.fitnessEvalLimit = fitnessEvalLimit;
        bestSolution = new BestSolution(optimalValue);
        currentSolutionFitness = 0;
        fitnessEvalCount = 0;
    }

    public abstract void init();

    public int getInstanceNumber() {
        return id;
    }

    public void setCooperativeExecution(CooperativeInfo cooperativeInfo) {
        this.cooperativeInfo = cooperativeInfo;
        this.cooperative = true;
    }

    public int calculateFitness(int[] solution) {
        fitnessEvalCount++;
        int fitness= Knapsack.getFitness(solution, performances);
        if(monitor && fitnessEvalCount%evalStep==0 ){
            bestSolution.updateEvolution(fitness);
        }
        return fitness;
    }

    public void enableMonitoring(int step){
        bestSolution.updateEvolution(currentSolutionFitness);
        monitor=true;
        evalStep=step;
    }

    public boolean fitnessEvalLimitReached() {
        return fitnessEvalCount >= fitnessEvalLimit;
    }

    public double getEvaluationProgress(){
        return (double)fitnessEvalCount/fitnessEvalLimit;
    }

    public void setCurrentSolution(int[] currentSolution) {
        this.currentSolution = currentSolution;
    }

    public int[] getPerformances() {
        return performances;
    }

    public int[][] getCosts() {
        return costs;
    }

    public int[] getConstraints() {
        return constraints;
    }

    public int[] getCurrentSolution() {
        return currentSolution;
    }

    public int getCurrentSolutionFitness() {
        return currentSolutionFitness;
    }

    public BestSolution getBestSolution() {
        return bestSolution;
    }


    public void updateBestSolutionFromCurrent() {
        if (Knapsack.feasibleKnapsack(currentSolution, costs, constraints) && currentSolutionFitness > bestSolution.getFitness()) {
            bestSolution.setValue(Knapsack.copyKnapsackItems(currentSolution));
            bestSolution.setFitness(currentSolutionFitness);
            bestSolution.setNumEvaluationFitness(fitnessEvalCount);
        }
    }

    public void setCurrentSolutionFitness(int currentSolutionFitness) {
        this.currentSolutionFitness = currentSolutionFitness;
    }

    public int getOptimalValue() {
        return optimalValue;
    }

    public boolean isCooperative() {
        return cooperative;
    }

    public CooperativeInfo getCooperativeInfo() {
        return cooperativeInfo;
    }
}
