package com.ugr.search.algorithms;

import java.util.Vector;

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
    private Vector<Double> evolution;
    private int optimalFitness;


    BestSolution(int optimalFitness) {
        value = null;
        fitness = 0;
        numEvaluationFitness = 0;
        this.optimalFitness=optimalFitness;
        evolution=new Vector<Double>();
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

    public void updateEvolution(int currentFitness){
        int best=Math.max(fitness,currentFitness);
        evolution.addElement((double)best/optimalFitness);
    }

    public Vector<Double> getEvolution(){
        return evolution;
    }

}
