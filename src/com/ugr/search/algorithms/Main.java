package com.ugr.search.algorithms;

import com.ugr.search.algorithms.tabu.TabuSearch;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class Main {

    private static String filePath;
    private static HashMap<String, Integer> params = new HashMap<String, Integer>();
    private static List<HashMap<String, Integer>> paramsList = new ArrayList<HashMap<String, Integer>>();


    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("Wrong parameters.");
        } else {

            int evaluationLimit=1000;
            int monitorStep=20;
            int runs=30;

            /* Single execution
            params.put(Params.DEPTH, 1);
            params.put(Params.TABU_METHOD, 3);
            params.put(Params.LIST_SIZE,100);
            readParams(args);*/

            // Group execution

            //1
            HashMap<String,Integer> groupParams1=new HashMap<String, Integer>();
            groupParams1.put(Params.TYPE,Params.TABUSEARCH);
            groupParams1.put(Params.TABU_METHOD,2);
            groupParams1.put(Params.LIST_SIZE,100);
            groupParams1.put(Params.DEPTH,1);

            // 2
            HashMap<String,Integer> groupParams2=new HashMap<String, Integer>();
            groupParams2.put(Params.TYPE,Params.TABUSEARCH);
            groupParams2.put(Params.TABU_METHOD,2);
            groupParams2.put(Params.LIST_SIZE,100);
            groupParams2.put(Params.DEPTH,2);

            // 3
            HashMap<String,Integer> groupParams3=new HashMap<String, Integer>();
            groupParams3.put(Params.TYPE,Params.TABUSEARCH);
            groupParams3.put(Params.TABU_METHOD,3);
            groupParams3.put(Params.LIST_SIZE,100);
            groupParams3.put(Params.DEPTH,1);

            // 4
            HashMap<String,Integer> groupParams4=new HashMap<String, Integer>();
            groupParams4.put(Params.TYPE,Params.TABUSEARCH);
            groupParams4.put(Params.TABU_METHOD,3);
            groupParams4.put(Params.LIST_SIZE,100);
            groupParams4.put(Params.DEPTH,2);


            // Add times TS2 depth 1
            paramsList.add(groupParams1);
            paramsList.add(groupParams2);
            paramsList.add(groupParams3);
            paramsList.add(groupParams4);





            String[] dataFiles={"HP1.dat" ,"HP2.dat" ,"PB1.dat", "PB2.dat" ,"PB4.dat" ,"PB5.dat" ,"PB6.dat" ,"PB7.dat" };

            String path="./data/";

            //for(int i=3;i<dataFiles.length;i++){
                for(int i=1;i<2;i++){


                filePath=path + dataFiles[i];

                Parser parser = new Parser(filePath);

                // Output file
                String outputFile = "./results/";
                int start=filePath.lastIndexOf('/')+1;
                int end=filePath.indexOf(".dat");
                outputFile+=filePath.substring(start,end)+"_v1v2Coop.dat";

                // Group execution
                Vector<Manager> instances=createCooperativeAlgorithmManagers(runs,parser,paramsList,evaluationLimit,monitorStep);
                writeGroupCSV(outputFile,instances);
                System.out.println("Escrito archivo: "+i);


                    //Manager myManager=new Manager(parser,true,4,evaluationLimit,paramsList,100,monitorStep);
                    //myManager.start();
                    //myManager.calculateGroupEvolution();




                /*
                Single execution
                // 30 runs of algorithm
                Vector<Algorithm> instances=createAlgorithms(runs,parser,evaluationLimit,monitorStep);
                writeCSV(outputFile,instances);*/

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

    private static Vector<Algorithm> createAlgorithms(int n, Parser parser, int evaluationLimit, int monitorStep){

        Vector<Algorithm> instances=new Vector<Algorithm>();

        for(int i=0;i<n;i++){

            Algorithm myTabu = new TabuSearch(0,parser.getPerformances(),
                    parser.getCosts(), parser.getConstraints(), params,
                    parser.getOptimalValue(), evaluationLimit);

            myTabu.enableMonitoring(monitorStep);
            myTabu.start();
            instances.addElement(myTabu);
        }

        return instances;
    }

    private static Vector<Manager> createAlgorithmManagers(int numberOfManager, Parser parser, List<HashMap<String, Integer>> hashMapList,
                                                           int evaluationLimit,int groupMonitorStep){

        Vector<Manager> instances=new Vector<Manager>();

        for(int i=0;i<numberOfManager;i++){

            Manager myTabuGroup = new Manager(parser,hashMapList,evaluationLimit,groupMonitorStep);
            instances.addElement(myTabuGroup);
        }

        return instances;
    }

    private static Vector<Manager> createCooperativeAlgorithmManagers(int numberOfManager, Parser parser, List<HashMap<String, Integer>> hashMapList,
                                                           int evaluationLimit,int groupMonitorStep){

        Vector<Manager> instances=new Vector<Manager>();

        for(int i=0;i<numberOfManager;i++){

            Manager myTabuGroup = new Manager(parser,true,4,evaluationLimit,hashMapList,100,groupMonitorStep);
            myTabuGroup.start();
            instances.addElement(myTabuGroup);
            System.out.println(i);
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

    private static void writeGroupCSV(String outputFile, Vector<Manager> instances){

        try{
            // use FileWriter constructor that specifies open for appending
            FileWriter file=new FileWriter(outputFile,false);

            for(Manager myTabuGroup : instances){

                // Wait algorithmGroup threads finish
                myTabuGroup.groupJoin();
                myTabuGroup.calculateGroupEvolution();


                String result=myTabuGroup.getEvolution().toString();
                result=result.replaceAll("\\[","");
                result=result.replaceAll("\\]","");
                file.write(result);

                if(myTabuGroup != instances.lastElement()){
                    file.write("\n");
                }
            }
            file.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
