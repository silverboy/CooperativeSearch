package com.ugr.search.algorithms;

import com.ugr.search.algorithms.tabu.TabuSearch;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Javi
 * Date: 21/01/13
 * Time: 22:09
 * To change this template use File | Settings | File Templates.
 */
public class Prueba {

    int[] valores = new int[3];
    Vector<int[]> vector = new Vector<int[]>();

    public void annadir() {
        int[] copy = Knapsack.copyKnapsackItems(valores);
        vector.add(copy);
    }

    public void setValor(int[] valores, int a) {
        valores[0] = a;
    }

    public static void main(String[] args) {


        int[] c = {8, 13, 6, 1, 7};
        int[][] A = {{2, 5, 3, 1, 7}};
        int[] b = {10};

        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put(Params.DEPTH, 1);
        map.put(Params.TABU_METHOD, 2);

        TabuSearch tabu = new TabuSearch(c, A, b, map, 0, 5, 100);

        Knapsack.printVector(tabu.getCurrentSolution());

        //tabu.execute();

        Knapsack.printVector(tabu.getCurrentSolution());

        int a = 1;

        switch (a) {
            case 1:
                a = 2;
                System.out.println("a es 1");
                break;

            case 2:
                System.out.println("a es 2");
                break;
        }

        double a1=8;
        double b1=0;
        double c1=a1/b1;


        System.out.println("8/0: "+c1);
        
        if(Double.MAX_VALUE > c1){
            System.out.println("MAX_VALUE > Infinity");
        }
        if(Double.MAX_VALUE == c1){
            System.out.println("MAX_VALUE = Infinity");
        }
        if(Double.MAX_VALUE < c1){
            System.out.println("MAX_VALUE < Infinity");
        }


    }


}
