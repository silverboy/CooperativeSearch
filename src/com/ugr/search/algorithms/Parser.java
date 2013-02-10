package com.ugr.search.algorithms;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Parser {

    private int[] performances;
    private int[][] costs;
    private int[] constraints;
    private int knapsacks;
    private int objects;
    private int optimalValue;

    public Parser(String filePath) {
        readInputFile(filePath);
    }

    private void readInputFile(String filePath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(dataInputStream));
            String line = buffer.readLine().trim();
            String[] numbers = line.split(" ");
            knapsacks = Integer.parseInt(numbers[0]);
            objects = Integer.parseInt(numbers[1]);
            int numberOfPerformances = 0;
            performances = new int[objects];
            while (numberOfPerformances != objects) {
                line = buffer.readLine().trim();
                numbers = line.split(" ");
                for (String number : numbers) {
                    performances[numberOfPerformances] = Integer.parseInt(number);
                    numberOfPerformances++;
                }
            }
            int numberOfConstraints = 0;
            constraints = new int[knapsacks];
            while (numberOfConstraints != knapsacks) {
                line = buffer.readLine().trim();
                numbers = line.split(" ");
                for (String number : numbers) {
                    constraints[numberOfConstraints] = Integer.parseInt(number);
                    numberOfConstraints++;
                }
            }
            int currentKnapsack = 0, currentObject = 0;
            costs = new int[knapsacks][objects];
            while (currentKnapsack != knapsacks) {
                while (currentObject != objects) {
                    line = buffer.readLine().trim();
                    numbers = line.split(" ");
                    for (String number : numbers) {
                        costs[currentKnapsack][currentObject] = Integer.parseInt(number);
                        currentObject++;
                    }
                }
                currentObject = 0;
                currentKnapsack++;
            }
            buffer.readLine();
            line = buffer.readLine().trim();
            optimalValue = Integer.parseInt(line);
            dataInputStream.close();
        } catch (Exception error) {
            System.err.println("Error: " + error.getMessage());
        }
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

    public int getOptimalValue() {
        return optimalValue;
    }
}
