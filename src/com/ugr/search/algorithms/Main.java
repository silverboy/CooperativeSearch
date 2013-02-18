package com.ugr.search.algorithms;

import com.ugr.search.algorithms.tabu.TabuSearch;

import java.io.*;
import java.util.HashMap;
import java.util.Vector;

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

            String[] dataFiles={"HP1.dat" ,"HP2.dat" ,"PB1.dat", "PB2.dat" ,"PB4.dat" ,"PB5.dat" ,"PB6.dat" ,"PB7.dat" };

            String path="./data/";

            for(int i=0;i<dataFiles.length;i++){

                filePath=path + dataFiles[i];

                Parser parser = new Parser(filePath);

                // 30 runs of algorithm
                Vector<Algorithm> instances=createAlgorithms(30,parser);

                // Write results in CSV file
                String outputFile = "./results/";
                int start=filePath.lastIndexOf('/')+1;
                int end=filePath.indexOf(".dat");

                outputFile+=filePath.substring(start,end)+"_v1only.dat";

                writeCSV(outputFile,instances);
            }
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

    private static Vector<Algorithm> createAlgorithms(int n, Parser parser){

        Vector<Algorithm> instances=new Vector<Algorithm>();

        for(int i=0;i<n;i++){

            Algorithm myTabu = new TabuSearch(0,parser.getPerformances(),
                    parser.getCosts(), parser.getConstraints(), params,
                    parser.getOptimalValue(), 1000);

            myTabu.enableMonitoring(25);
            myTabu.start();
            instances.addElement(myTabu);
        }

        return instances;
    }

    private static void writeCSV(String outputFile,Vector<Algorithm> instances){

        try{
            // use FileWriter constructor that specifies open for appending
            FileWriter file=new FileWriter(outputFile,false);

            for(Algorithm myTabu : instances){

                // Wait algorithm thread finish
                myTabu.join();

                // write out a few records

                String result=myTabu.getBestSolution().getEvolution().toString();
                result=result.replaceAll("\\[","");
                result=result.replaceAll("\\]","");
                file.write(result);

                if(myTabu != instances.lastElement()){
                    file.write("\n");
                }
            }
            file.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
