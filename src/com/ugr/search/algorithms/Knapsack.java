package com.ugr.search.algorithms;

import java.util.ArrayList;
import java.util.List;

public class Knapsack {

    public static List<Integer> getItemsNotInKnapsack(int[] solution) {
        List<Integer> items = new ArrayList<Integer>();
        for (int currentItem = 0; currentItem < solution.length; currentItem++) {
            if (solution[currentItem] == 0) {
                items.add(currentItem);
            }
        }
        return items;
    }

    public static List<Integer> getItemsInKnapsack(int[] solution) {
        List<Integer> items = new ArrayList<Integer>();
        for (int currentItem = 0; currentItem < solution.length; currentItem++) {
            if (solution[currentItem] == 1) {
                items.add(currentItem);
            }
        }
        return items;
    }

    public static int[] copyKnapsackItems(int[] solution) {
        int[] newSolution = new int[solution.length];
        System.arraycopy(solution, 0, newSolution, 0, solution.length);
        return newSolution;
    }

    public static void putItemInKnapsack(int[] solution, int item) {
        solution[item] = 1;
    }

    public static void removeItemInKnapsack(int[] solution, int item) {
        solution[item] = 0;
    }

    public static int getFitness(int[] solution, int[] performance) {
        int fitness = 0;
        for (int currentItem = 0; currentItem < solution.length; currentItem++) {
            if (solution[currentItem] == 1) {
                fitness += performance[currentItem];
            }
        }
        return fitness;
    }

    /**
     * Determine if a solution is feasible, near feasible or infeasible
     *
     * @param solution    the solution to be test (x)
     * @param costs       costs matrix (A)
     * @param constraints constraints vector (c)
     * @return true if the solution is feasible or near feasible, depending on nearFeasibleType param; false otherwise
     */
    public static boolean feasibleKnapsack(int[] solution, int[][] costs, int[] constraints) {
        boolean isFeasible = true;

        int[] result = calculateCostVector(costs, solution);

        for (int i = 0; i < result.length; i++) {
            if (result[i] > constraints[i]) {
                isFeasible = false;
                break;
            }
        }

        return isFeasible;
    }


    /**
     * Determine if a solution is near feasible according TS2 approach
     *
     * @param solution    the solution to be test (x)
     * @param costs       costs matrix (A)
     * @param constraints constraints vector (c)
     * @param iteration   algorithm iteration
     * @return true if solution is near feasible, false otherwise
     */
    public static boolean nearFeasibleTS2(int[] solution, int[][] costs, int[] constraints, int iteration) {

        boolean nearFeasible = true;
        int result = 0;

        // Determine constraint to be satisfied
        int sConstraint = iteration % constraints.length;

        // Compute sConstraint
        for (int i = 0; i < solution.length; i++) {
            result += costs[sConstraint][i] * solution[i];
        }

        if (result > constraints[sConstraint]) {
            nearFeasible = false;
        }

        return nearFeasible;
    }

    /**
     * Determine if a solution is near feasible according TS3 approach
     *
     * @param solution         the solution to be test (x)
     * @param costs            costs matrix (A)
     * @param constraints      constraints vector (c)
     * @param previousSolution previous solution
     * @return true if solution is near feasible, false otherwise
     */
    public static boolean nearFeasibleTS3(int[] solution, int[][] costs, int[] constraints, int[] previousSolution) {

        boolean nearFeasible = true;
        int result[] = calculateCostVector(costs, previousSolution);
        int cResult = 0;

        // Determine least saturated constraint in previous solution
        int sConstraint = 0;
        int maxDifference = constraints[0] - result[0];
        for (int i = 1; i < result.length; i++) {
            if (constraints[i] - result[i] > maxDifference) {
                sConstraint = i;
                maxDifference = constraints[i] - result[i];
            }
        }

        // Compute sConstraint
        for (int i = 0; i < solution.length; i++) {
            cResult += costs[sConstraint][i] * solution[i];
        }

        if (cResult > constraints[sConstraint]) {
            nearFeasible = false;
        }

        return nearFeasible;
    }


    private static int[] calculateCostVector(int[][] costs, int[] solution) {
        int[] result = new int[costs.length];

        for (int i = 0; i < costs.length; i++) {
            result[i] = 0;
            for (int j = 0; j < costs[i].length; j++) {
                result[i] += costs[i][j] * solution[j];
            }
        }

        return result;
    }

    public static int[] calculateCostPerItemSum(int[][] costs) {

        int[] sum = new int[costs[0].length];

        for (int j = 0; j < sum.length; j++) {
            sum[j] = 0;
            for (int i = 0; i < costs.length; i++) {
                sum[j] += costs[i][j];
            }
        }
        return sum;
    }


    public static int[] calculateCostPerConstraintSum(int[][] costs) {

        int[] sum = new int[costs.length];

        for (int i = 0; i < sum.length; i++) {
            sum[i] = 0;
            for (int j = 0; j < costs[0].length; j++) {
                sum[i] += costs[i][j];
            }
        }
        return sum;
    }

    public static int calculateTotalConstraintsMargin(int[] solution, int[][] costs, int[] constraints) {

        int margin = 0;

        int[] costVector = calculateCostVector(costs, solution);

        for (int i = 0; i < constraints.length; i++) {
            margin += constraints[i] - costVector[i];
        }

        return margin;
    }


    public static int[] calculateExcessInConstraints(int[] solution, int[][] costs, int[] constraints) {

        int[] excess = new int[constraints.length];

        int[] costVector = calculateCostVector(costs, solution);

        for (int i = 0; i < constraints.length; i++) {
            excess[i] = costVector[i] - constraints[i];
        }

        return excess;
    }

    public static double[] calculateExcessConstraintRatio(int[] solution, int[][] costs, int[] constraints) {

        double[] ratios = new double[constraints.length];
        int[] excess = calculateExcessInConstraints(solution, costs, constraints);

        for (int i = 0; i < ratios.length; i++) {
            ratios[i] = (double) excess[i] / constraints[i];
        }

        return ratios;
    }

    public static void printVector(int[] vector) {

        for (int i = 0; i < vector.length; i++) {
            System.out.print("\t" + vector[i]);
        }
        System.out.print("\n");
    }


    public static void printMatrix(int[][] matrix) {

        for (int i = 0; i < matrix.length; i++) {

            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print("\t" + matrix[i][j]);
            }
            System.out.print("\n");
        }
    }


}
