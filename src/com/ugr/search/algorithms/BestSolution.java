package com.ugr.search.algorithms;

/**
 * Created by IntelliJ IDEA.
 * User: Javi
 * Date: 26/01/13
 * Time: 20:34
 * To change this template use File | Settings | File Templates.
 */
public class BestSolution {

    private int[] value;
    private int fitness;
    private int numEvaluationFitness;

    BestSolution(int[] value, int fitness, int numEvaluationFitness) {
        this.value = value;
        this.fitness = fitness;
        this.numEvaluationFitness = numEvaluationFitness;
    }

    public int[] getValue() {
        return value;
    }

    public int getFitness() {
        return fitness;
    }

    public int getNumEvaluationFitness() {
        return numEvaluationFitness;
    }

    public void setValue(int[] value) {
        this.value = value;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public void setNumEvaluationFitness(int numEvaluationFitness) {
        this.numEvaluationFitness = numEvaluationFitness;
    }

}
