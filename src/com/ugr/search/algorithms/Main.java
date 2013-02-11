package com.ugr.search.algorithms;

import com.ugr.search.algorithms.tabu.TabuSearch;

import java.util.HashMap;

public class Main {

    private static String filePath;
    private static HashMap<String, Integer> params = new HashMap<String, Integer>();

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Wrong parameters.");
        } else {
            System.out.print("Soy javi");
            params.put(Params.DEPTH, 1);
            params.put(Params.TABU_METHOD, 2);
            readParams(args);
            Parser parser = new Parser(filePath);
            TabuSearch tabuSearch = new TabuSearch(parser.getPerformances(),
                    parser.getCosts(), parser.getConstraints(), params,
                    parser.getOptimalValue(), 1000, 100);
            System.out.println("Performances:");
            Knapsack.printVector(tabuSearch.getPerformances());
            System.out.println("Constraints:");
            Knapsack.printVector(tabuSearch.getConstraints());
            System.out.println("Costs:");
            Knapsack.printMatrix(tabuSearch.getCosts());
            System.out.println("Solucion inicial");
            Knapsack.printVector(tabuSearch.getCurrentSolution());
            System.out.println("Fitness inicial: " + tabuSearch.getCurrentSolutionFitness());


            tabuSearch.execute();
            System.out.println("Solucion final:");
            Knapsack.printVector(tabuSearch.getBestSolution().getValue());
            System.out.println("Fitness final: "+tabuSearch.getBestSolution().getFitness());
        }
    }

    private static void readParams(String[] args) {
        try {
            for (String arg : args) {
                if (arg.startsWith("-f")) {
                    filePath = arg.replace("-f", "");
                } else if (arg.startsWith("-d")) {
                    params.put(Params.DEPTH, Integer.parseInt(arg.replace("-d", "")));
                } else if (arg.startsWith(("-t"))) {
                    params.put(Params.TABU_METHOD, Integer.parseInt(arg.replace("-t", "")));
                }
            }
            if (filePath == null) {
                System.out.println("Wrong parameters.");
                System.exit(1);
            }
        } catch (Exception ex) {
            System.out.println("Wrong parameters.");
            System.exit(1);
        }
    }
}
