package com.ugr.search.algorithms.tabu;

import com.ugr.search.algorithms.Knapsack;

import java.util.Vector;

public class TabuList {

    private Vector<int[]> solutions = new Vector<int[]>();
    private int maxSize;

    public TabuList(int maxSize) {
        this.maxSize = maxSize;
    }

    public TabuList() {
    }

    public void addSolution(int[] solution) {
        // Create copy of solution
        int[] solutionCopy = Knapsack.copyKnapsackItems(solution);

        // Add solution as the first item
        solutions.add(0, solutionCopy);

        // Check size of tabu list
        if (solutions.size() > maxSize) {
            solutions.remove(maxSize);
        }
    }

    public void removeSolution(int[] solution) {

    }

    public boolean inTabuList(int[] solution) {

        boolean inTL = false;

        int temp;

        for (int i = 0; i < solutions.size(); i++) {

            int[] sol = solutions.elementAt(i);
            temp = 0;

            // Perform operation XOR(tabulist , solution); if the result contains a 0 value solution is in tabu list
            for (int j = 0; j < solution.length; j++) {
                temp += sol[j] ^ solution[j];
            }

            if (temp == 0) {
                inTL = true;
                break;
            }
        }
        return inTL;
    }
}
