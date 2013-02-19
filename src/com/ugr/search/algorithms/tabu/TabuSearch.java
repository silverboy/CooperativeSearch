package com.ugr.search.algorithms.tabu;

import com.ugr.search.algorithms.Algorithm;
import com.ugr.search.algorithms.Knapsack;
import com.ugr.search.algorithms.Params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TabuSearch extends Algorithm {

    enum Direction {CONSTRUCTIVE, DESTRUCTIVE}

    private TabuList tabuList;
    private Direction direction;
    private int depth;
    private int nearFeasibleType;


    /**
     * Replace c/u*A by costs c
     */
    private static final byte U_SIMPLE = 0;
    /**
     * Senju and Toyoda u multiplier
     */
    private static final byte U_ST = 1;
    /**
     * Fréville and Plateau / Dammeyer and Voss u multiplier
     */
    private static final byte U_FP = 2;
    /**
     * Structural multiplier approach
     */
    private static final byte U_SM = 3;


    public TabuSearch(int id, int[] performances, int[][] costs, int[] constraints,
                      HashMap<String, Integer> params, int optimalValue, int fitnessEvalLimit) {
        super(id, performances, costs, constraints, optimalValue, fitnessEvalLimit);
        tabuList = new TabuList(params.get(Params.LIST_SIZE));
        direction = Direction.CONSTRUCTIVE;
        readParams(params);
        init();
    }

    private void readParams(HashMap<String, Integer> params) {
        depth = params.get(Params.DEPTH);
        nearFeasibleType = params.get(Params.TABU_METHOD);
    }

    @Override
    public void init() {
        int numItems = getPerformances().length;

        Random generator = new Random();

        int[] solution = new int[numItems];

        for (int i = 0; i < numItems; i++) {
            solution[i] = generator.nextInt(2);
        }

        setCurrentSolution(solution);
        if (Knapsack.feasibleKnapsack(solution, getCosts(), getConstraints())) {
            setCurrentSolutionFitness(calculateFitness(solution));
            updateBestSolutionFromCurrent();
            tabuList.addSolution(solution);
        }

    }

    @Override
    public int calculateFitness(int[] solution) {
        return super.calculateFitness(solution);
    }


    @Override
    public void run() {

        int iteration = 0;
        boolean solutionChanged;
        while (!fitnessEvalLimitReached()) {
            switch (direction) {
                case CONSTRUCTIVE:
                    direction = Direction.DESTRUCTIVE;
                    solutionChanged = tabuSearchAdd(getCurrentSolution(), U_SIMPLE);
                    if (solutionChanged) {
                        setCurrentSolutionFitness(calculateFitness(getCurrentSolution()));
                        updateBestSolutionFromCurrent();
                        tabuList.addSolution(getCurrentSolution());
                    }
                    constructiveComplement();
                    tabuInfeasibleAdd(getCurrentSolution(), iteration);
                    break;
                case DESTRUCTIVE:
                    direction = Direction.CONSTRUCTIVE;
                    tabuSearchProject(getCurrentSolution(), U_ST);
                    setCurrentSolutionFitness(calculateFitness(getCurrentSolution()));
                    updateBestSolutionFromCurrent();
                    tabuList.addSolution(getCurrentSolution());
                    destructiveComplement();
                    tabuSearchDrop(getCurrentSolution());
                    break;
            }
            addFitnessEvolution(getBestSolution().getFitness());
            if(isCooperative() && !fitnessEvalLimitReached()
                    && getEvaluationCount() >= (getCooperativeEval() * getCooperativeInfo().getEvaluationTime())) {

                incrementCooperativeEval();
                getCooperativeInfo().changeInformation(getInstanceNumber(), getBestSolution(), getFitnessEvolution());
                while(!getCooperativeInfo().continueExecution(getInstanceNumber())) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
                if(getCooperativeInfo().solutionChange(getInstanceNumber())) {
                    setCurrentSolution(Knapsack.copyKnapsackItems(getCooperativeInfo().getAlgorithmSolution(getInstanceNumber()).getValue()));
                    setCurrentSolutionFitness(getCooperativeInfo().getAlgorithmSolution(getInstanceNumber()).getFitness());
                    updateBestSolutionFromCurrent();
                    tabuList.clearList();
                }
            }
            iteration++;
        }
        if(isCooperative()){
            getCooperativeInfo().finishExecution(getInstanceNumber(), getBestSolution());
        }
    }

    private boolean tabuSearchAdd(int[] solution, byte uMultiplier) {
        boolean solutionChanged = false;
        boolean feasible = true;
        while (feasible) {
            int item = searchBestItem(solution, uMultiplier);
            if (item != -1) {
                Knapsack.putItemInKnapsack(solution, item);
                solutionChanged = true;
            } else {
                feasible = false;
            }
        }
        return solutionChanged;
    }

    private void tabuSearchProject(int[] solution, byte uMultiplier) {
        boolean feasible = false;

        while (!feasible) {
            int item = searchWorstItem(solution, uMultiplier);
            if (item == -1) {
                item = firstAspirationCondition(solution, uMultiplier);
            }
            if (item != -1) {
                Knapsack.removeItemInKnapsack(solution, item);
            }
            if (Knapsack.feasibleKnapsack(solution, getCosts(), getConstraints())) {
                feasible = true;
            }
        }
    }

    private void constructiveComplement() {

        int[] currentSolution = Knapsack.copyKnapsackItems(getCurrentSolution());
        if (!Knapsack.feasibleKnapsack(currentSolution, getCosts(), getConstraints())) {
            return;
        }
        List<Integer> items = Knapsack.getItemsInKnapsack(getCurrentSolution());
        for (Integer currentItem : items) {
            if (fitnessEvalLimitReached()) {
                break;
            }
            int[] newSolution = Knapsack.copyKnapsackItems(currentSolution);
            Knapsack.removeItemInKnapsack(newSolution, currentItem);
            tabuSearchAdd(newSolution, U_SM);

            int newSolutionFitness = calculateFitness(newSolution);
            if (newSolutionFitness > getCurrentSolutionFitness()) {
                setCurrentSolution(newSolution);
                setCurrentSolutionFitness(newSolutionFitness);
                updateBestSolutionFromCurrent();
            }
        }
        if(!tabuList.inTabuList(getCurrentSolution())){
            tabuList.addSolution(getCurrentSolution());
        }
    }

    private void destructiveComplement() {
        int[] currentSolution = Knapsack.copyKnapsackItems(getCurrentSolution());
        List<Integer> items = Knapsack.getItemsNotInKnapsack(getCurrentSolution());
        for (Integer currentItem : items) {
            if (fitnessEvalLimitReached()) {
                break;
            }
            int[] newSolution = Knapsack.copyKnapsackItems(currentSolution);
            Knapsack.putItemInKnapsack(newSolution, currentItem);
            tabuSearchProject(newSolution, U_SM);
            int newSolutionFitness = calculateFitness(newSolution);
            if (newSolutionFitness > getCurrentSolutionFitness()) {
                setCurrentSolution(newSolution);
                setCurrentSolutionFitness(newSolutionFitness);
                updateBestSolutionFromCurrent();
            }
        }
        if(!tabuList.inTabuList(getCurrentSolution())){
            tabuList.addSolution(getCurrentSolution());
        }
    }

    private void tabuInfeasibleAdd(int[] solution, int iteration) {
        List<Integer> s = new ArrayList<Integer>();
        boolean nearFeasible = true;
        while (nearFeasible && !fitnessEvalLimitReached()) {
            int item = searchBestItem(solution, s);
            if (item != -1) {
                int[] newSolution = Knapsack.copyKnapsackItems(solution);
                Knapsack.putItemInKnapsack(newSolution, item);
                if (nearFeasible(newSolution, solution, iteration)) {
                    setCurrentSolution(newSolution);
                    solution = getCurrentSolution();
                } else {
                    s.add(item);
                }
            } else {
                nearFeasible = false;
            }
        }
    }

    private boolean nearFeasible(int[] candidateSolution, int[] currentSolution, int iteration) {
        boolean feasible = true;

        switch (nearFeasibleType) {
            case 2:
                feasible = Knapsack.nearFeasibleTS2(candidateSolution, getCosts(), getConstraints(), iteration);
                break;
            case 3:
                feasible = Knapsack.nearFeasibleTS3(candidateSolution, getCosts(), getConstraints(), currentSolution);
                break;
            default:
                System.err.println("Near feasible type unknown");
                break;
        }
        return feasible;
    }

    private void tabuSearchDrop(int[] solution) {
        int count = 1;
        while (count <= depth && !Knapsack.getItemsInKnapsack(solution).isEmpty()) {
            int item = searchWorstItem(solution, U_FP);
            if (item == -1) {
                item = secondAspirationCondition(solution);
            }
            if (item != -1) {
                Knapsack.removeItemInKnapsack(solution, item);
            }
            count++;
        }
        setCurrentSolutionFitness(calculateFitness(solution));
        tabuList.addSolution(solution);
    }

    private int searchWorstItem(int[] solution, byte uMultiplier) {
        // Item index
        int item = -1;
        double minRatio = Double.MAX_VALUE;

        // Local solution copy
        int[] solutionCopy = Knapsack.copyKnapsackItems(solution);

        // Ratios vector
        double[] ratios = calculatePerformanceCostRatios(solution, uMultiplier);
        List<Integer> items = Knapsack.getItemsInKnapsack(solution);
        for (Integer currentItem : items) {
            if (ratios[currentItem] < minRatio) {

                // Item currentItem is candidate if new solution does not appear on TL
                solutionCopy[currentItem] = 0;
                if (!tabuList.inTabuList(solutionCopy)) {
                    item = currentItem;
                    minRatio = ratios[currentItem];
                }

                // Restore solutionCopy
                solutionCopy[currentItem] = 1;
            }
        }
        return item;
    }

    private int firstAspirationCondition(int[] solution, byte uMultiplier) {
        // Item index
        int item = -1;
        double minRatio = Double.MAX_VALUE;

        // Ratios vector
        double[] ratios = calculatePerformanceCostRatios(solution, uMultiplier);
        List<Integer> items = Knapsack.getItemsInKnapsack(solution);
        for (Integer currentItem : items) {
            if (ratios[currentItem] < minRatio) {
                item = currentItem;
                minRatio = ratios[currentItem];
            }
        }
        return item;
    }

    private int searchBestItem(int[] solution, List<Integer> s) {
        List<Integer> items = Knapsack.getItemsNotInKnapsack(solution);
        int[] performances = getPerformances();
        int[] solutionCopy = Knapsack.copyKnapsackItems(solution);

        int indexItem = -1;
        for (Integer it : items) {
            if (!s.contains(it) && (indexItem == -1 || performances[it] > performances[indexItem])) {
                solutionCopy[it] = 1;
                if (!tabuList.inTabuList(solutionCopy)) {
                    indexItem = it;
                }
                solutionCopy[it] = 0;
            }
        }
        return indexItem;
    }

    private int secondAspirationCondition(int[] solution) {

        int totalMargin = Knapsack.calculateTotalConstraintsMargin(solution, getCosts(), getConstraints());
        int[] costPerItemSum = Knapsack.calculateCostPerItemSum(getCosts());
        int[] performances = getPerformances();

        // Item index
        int item = 0;
        double minRatio = Double.MAX_VALUE;
        double currentRatio;

        // Items in Knapsack
        List<Integer> items = Knapsack.getItemsInKnapsack(solution);

        for (Integer currentItem : items) {

            currentRatio = (getCurrentSolutionFitness() - performances[currentItem]) / (totalMargin - costPerItemSum[currentItem]);

            if (currentRatio < minRatio) {
                item = currentItem;
                minRatio = currentRatio;
            }
        }

        return item;

    }

    /**
     * Try add one item to the knapsack if the solution remains feasible. Choose the item with better performance
     *
     * @param solution    currento solution vector
     * @param uMultiplier u multiplier approach used. Constants U_ define possible values
     * @return item index with better performance or -1 if adding any item become solution infeasible or new solution
     *         is in TL
     */

    private int searchBestItem(int[] solution, byte uMultiplier) {
        // Item index
        int item = -1;
        double maxRatio = 0;

        // Local solution copy
        int[] solutionCopy = Knapsack.copyKnapsackItems(solution);

        // Ratios vector
        double[] ratios = calculatePerformanceCostRatios(solution, uMultiplier);
        List<Integer> items = Knapsack.getItemsNotInKnapsack(solution);
        for (Integer currentItem : items) {
            if (ratios[currentItem] > maxRatio) {

                // Item currentItem is candidate if new solution is feasible and does not appear on TL
                solutionCopy[currentItem] = 1;
                if (Knapsack.feasibleKnapsack(solutionCopy, getCosts(), getConstraints()) && !tabuList.inTabuList(solutionCopy)) {
                    item = currentItem;
                    maxRatio = ratios[currentItem];
                }

                // Restore solutionCopy
                solutionCopy[currentItem] = 0;
            }
        }
        return item;
    }

    private double[] calculatePerformanceCostRatios(int[] solution, byte uMultiplier) {

        double[] ratios = new double[solution.length];
        double[] u;
        int[] performances = getPerformances();

        if (uMultiplier == U_SIMPLE) {
            for (int j = 0; j < performances.length; j++) {
                ratios[j] = performances[j];
            }
        } else {
            u = calculateUVector(solution, uMultiplier);

            boolean allZero=true;

            for(int i=0;i<u.length;i++){
                if(u[i]!=0){
                    allZero=false;
                    break;
                }
            }

            if(!allZero){
                int[][] costs = getCosts();

                for (int j = 0; j < ratios.length; j++) {
                    ratios[j] = 0;
                    for (int i = 0; i < u.length; i++) {
                        ratios[j] += u[i] * costs[i][j];
                    }
                    ratios[j] = (double) performances[j] / ratios[j];
                    if(ratios[j]>Double.MAX_VALUE){
                        //Is Infinity
                        ratios[j]=Double.MAX_VALUE - 1;
                    }
                }
            }
            else{
                for (int j = 0; j < performances.length; j++) {
                    ratios[j] = performances[j];
                }
            }
        }

        return ratios;
    }

    private double[] calculateUVector(int[] solution, byte uMultiplier) {

        double[] uVector = new double[getConstraints().length];

        switch (uMultiplier) {

            case U_ST:
                int[] excess = Knapsack.calculateExcessInConstraints(solution, getCosts(), getConstraints());
                for (int i = 0; i < uVector.length; i++) {
                    if (excess[i] > 0) {
                        uVector[i] = excess[i];
                    } else {
                        uVector[i] = 0;
                    }
                }
                break;
            case U_FP:

                double[] eCRatios = Knapsack.calculateExcessConstraintRatio(solution, getCosts(), getConstraints());

                int minCRatioIndex = 0;
                uVector[0] = 0;

                for (int i = 1; i < uVector.length; i++) {
                    uVector[i] = 0;
                    if (eCRatios[i] < eCRatios[minCRatioIndex]) {
                        minCRatioIndex = i;
                    }
                }

                uVector[minCRatioIndex] = 1;
                break;

            case U_SM:

                int[] constraints = getConstraints();
                int[] sumA = Knapsack.calculateCostPerConstraintSum(getCosts());

                for (int i = 1; i < uVector.length; i++) {
                    if (sumA[i] >= constraints[i]) {
                        uVector[i] = (double) (sumA[i] - constraints[i]) / sumA[i];
                    } else {
                        uVector[i] = 0;
                    }
                }

                break;
        }
        return uVector;
    }
}
