package com.alegria.utg.ga;

import com.alegria.utg.utilities.Constants;
import com.alegria.utg.utilities.Methods;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Alberto Alegria
 */
public class GeneticAlgorithm {
    private Population population;
    private Selection selection;
    private ArrayList<Individual> matingPool;

    private int maxGenerations;
    private int selectionType;
    private double mutationRate;
    private int populationSize;

    public GeneticAlgorithm(int populationSize, int maxGenerations, int selectionType, double mutationRate) {
        population = new Population(populationSize);
        this.maxGenerations = maxGenerations;
        this.selectionType = selectionType;
        this.mutationRate = mutationRate;
        this.populationSize = populationSize;
        matingPool = new ArrayList<>();
    }

    public void run() {
        int i = 0;
        population.setFitness(-1);
        while (i < maxGenerations) {
            if (population.getBest().getFitness() == 0){
                break;
            }
            System.out.println("Generation: " + i);
            matingPool.clear();
            selection();
            crossover();
            population.setFitness(i);
            i++;
        }

        Individual best = population.getBest();
        System.out.println("Best: " + best.getId() + "\n" + best.toString());
        System.out.println("Fitness: " + best.getFitness());
        try {
            Methods.exportData(best.toString(), "/Users/alberto/Desktop/timetable.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Main.debugIndividual(population.getBest());
    }

    private void selection() {
        selection = new Selection(population, selectionType, 10);
        matingPool = selection.getMatingPool();
    }

    private void crossover() {
        ArrayList<Individual> newPopulation = new ArrayList<>();
        newPopulation.addAll(matingPool);

        while (newPopulation.size() < populationSize) {
            Individual i1 = matingPool.get(Methods.getRandomNumber(0, matingPool.size() - 1));

            Individual i2 = matingPool.get(Methods.getRandomNumber(0, matingPool.size() - 1));

            Crossover crossover = new Crossover(i1, i2);

            Individual n1 = crossover.getFirstDescendant();
            Individual n2 = crossover.getSecondDescendant();

            Mutation m1 = new Mutation(n1, mutationRate);
            Mutation m2 = new Mutation(n2, mutationRate);

            m1.mutate();
            m2.mutate();

            newPopulation.add(n1);
            newPopulation.add(n2);
        }

        if (newPopulation.size() > populationSize) {
            newPopulation.subList(populationSize, newPopulation.size()).clear();
        }
        population.replacePopulation(newPopulation);
    }

    private void mutation() {
        System.out.println("Mutating");
        for (Individual individual : population.getIndividuals()) {
            Mutation mutation = new Mutation(individual, mutationRate);
            mutation.mutate();
        }
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(500, 100, Constants.SelectionMethods.TOURNAMENT, 0.01);
        geneticAlgorithm.run();

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        System.out.println("Execution time: " + totalTime);
    }
}
