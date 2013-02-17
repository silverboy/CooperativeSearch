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

            params.put(Params.DEPTH, 1);
            params.put(Params.TABU_METHOD, 2);
            params.put(Params.LIST_SIZE,100);
            readParams(args);
            Parser parser = new Parser(filePath);
            TabuSearch tabuSearch = new TabuSearch(0,parser.getPerformances(),
                    parser.getCosts(), parser.getConstraints(), params,
                    parser.getOptimalValue(), 500);

            tabuSearch.enableMonitoring(25);

            tabuSearch.start();

            try {
                tabuSearch.join();
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            System.out.println("Fitness final: " + tabuSearch.getBestSolution().getFitness());
            System.out.println("Fitness Optimo: "+tabuSearch.getOptimalValue());
            System.out.println("Evolucion: "+tabuSearch.getBestSolution().getEvolution());

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
