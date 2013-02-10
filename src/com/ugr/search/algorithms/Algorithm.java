package com.ugr.search.algorithms;

public abstract class Algorithm {

    private int[] performances;
    private int[][] costs;
    private int[] constraints;
    private int[] currentSolution;
    private int currentSolutionFitness;
    private BestSolution bestSolution;
    private int optimalValue;
    private int fitnessEvalCount;
    private int fitnessEvalLimit;

    public Algorithm(int[] performances, int[][] costs, int[] constraints, int optimalValue, int fitnessEvalLimit) {
        this.performances = performances;
        this.costs = costs;
        this.constraints = constraints;
        this.optimalValue = optimalValue;
        this.fitnessEvalLimit = fitnessEvalLimit;
        bestSolution = new BestSolution(null, 0, 0);
        currentSolutionFitness = 0;
        fitnessEvalCount = 0;
    }

    public abstract void init();

    public abstract void execute();

    public int calculateFitness(int[] solution) {
        fitnessEvalCount++;
        return Knapsack.getFitness(solution, performances);
    }

    public boolean fitnessEvalLimitReached() {
        return (fitnessEvalCount >= fitnessEvalLimit) ? true : false;
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
}
