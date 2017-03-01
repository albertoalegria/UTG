package com.alegria.utg.ga;

import com.alegria.utg.utilities.Constants;
import com.alegria.utg.utilities.Methods;

import java.util.ArrayList;

/**
 * @author Alberto Alegria
 */
public class Selection {
    private Population population;
    private int selectionType;
    private int poolSize;
    private int tournamentSize;
    private ArrayList<Individual> matingPool;

    public Selection(Population population, int selectionType) {
        this.population = population;
        this.selectionType = selectionType;

        matingPool = new ArrayList<>();
        poolSize = population.getSize()/ 10;
    }

    public Selection(Population population, int selectionType, int tournamentSize) {
        this.population = population;
        this.selectionType = selectionType;

        matingPool = new ArrayList<>();
        poolSize = population.getSize() / 5;
        this.tournamentSize = tournamentSize;
    }

    public ArrayList<Individual> getMatingPool() {

        switch (selectionType) {
            case Constants.SelectionMethods.ELITISM:
                System.out.println("Selection Method: Elitism");
                elitismSelection();
                break;

            case Constants.SelectionMethods.TOURNAMENT:
                System.out.println("Selection Method: Tournament");
                tournamentSelection();
                break;

            case Constants.SelectionMethods.BINARY_TOURNAMENT:
                System.out.println("Selection Method: Binary Tournament");
                binaryTournamentSelection();
                break;

            case Constants.SelectionMethods.LINEAR_RANK:
                System.out.println("Selection Method: Linear Rank");
                rankSelection();
                break;

            case Constants.SelectionMethods.ROULETTE_WHEEL:
                System.out.println("Selection Method: Roulette Wheel");
                rouletteWheelSelection();
                break;

            case Constants.SelectionMethods.STOCHASTIC_UNIVERSAL_SAMPLING:
                System.out.println("Selection Method: Stochastic Universal Sampling");
                stochasticUniversalSelection();
                break;
        }

        return matingPool;
    }

    private void elitismSelection() {
        matingPool.clear();
        population.sortByFitness();

        for (int i = 0; i < poolSize; i++) {
            System.out.println("Selected individual: " + population.getIndividual(i).getId() + ", F: " + population.getIndividual(i).getFitness());
            matingPool.add(population.getIndividual(i));
        }
    }

    private void tournamentSelection() {
        matingPool.clear();

        for (int i = 0; i < poolSize; i++) {
            ArrayList<Individual> competitors = new ArrayList<>();

            for (int j = 0; j < tournamentSize; j++) {
                competitors.add(population.getIndividual(Methods.getRandomNumber(0, population.getSize() - 1)));
            }

            System.out.println("Competitors: ");
            competitors.forEach(individual -> System.out.println("Individual " + individual.getId() + ": " + individual.getFitness()));

            Individual best = competitors.get(0);

            for (int j = 0; j < tournamentSize; j++) {
                if (competitors.get(j).getFitness() < best.getFitness()) {
                    best = competitors.get(j);
                }
            }

            System.out.println("Selected individual: " + best.getId() + ", F: " + best.getFitness());

            matingPool.add(best);
        }
    }

    private void binaryTournamentSelection() {
        matingPool.clear();
        for (int i = 0; i < poolSize; i++) {
            Individual individualA = population.getIndividual(Methods.getRandomNumber(0, population.getSize() - 1));
            Individual individualB = population.getIndividual(Methods.getRandomNumber(0, population.getSize() - 1));

            System.out.println("Competitors: ");
            System.out.println("Individual " + individualA.getId() + ": " + individualA.getFitness());
            System.out.println("Individual " + individualB.getId() + ": " + individualB.getFitness());


            if (individualA.getFitness() < individualB.getFitness()) {
                matingPool.add(individualA);
                System.out.println("Selected individual: " + individualA.getId() + ", F: " + individualA.getFitness());
            } else {
                matingPool.add(individualB);
                System.out.println("Selected individual: " + individualB.getId() + ", F: " + individualB.getFitness());
            }
        }
    }

    private void rankSelection() {
        matingPool.clear();
        population.sortByFitness();
        population.reverse();

        int size = population.getSize();

        for (int i = 1; i <= size; i++) {
            population.getIndividual(i - 1).setRank(i);
        }

        population.shuffle();

        int pool = 0;

        while (pool < poolSize) {
            for (Individual individual : population.getIndividuals()) {
                int rand = Methods.getRandomNumber(0, population.getSize());

                if (rand <= individual.getRank() && pool < poolSize) {
                    matingPool.add(individual);
                    System.out.println("Selected individual: " + individual.getId() + ", R: " + individual.getRank() + ", F: " + individual.getFitness());
                    pool++;
                }
            }
        }
    }

    private void rouletteWheelSelection() {
        matingPool.clear();
        population.sortByFitness();

        population.getIndividuals().stream().forEach(individual ->
                individual.setExpectedFitness((individual.getFitness() == 0) ? 2 : 1 / individual.getFitness()));

        population.reverse();

        double sumFitness = 0.0;
        for (Individual individual : population.getIndividuals()) {
            sumFitness += individual.getExpectedFitness();
            individual.setExpectedFitness(sumFitness);
        }

        int pool = 0;
        while (pool < poolSize) {
            double rand = Methods.getRandomDouble(0, sumFitness);

            for (int i = 0; i < population.getSize(); i++) {
                if (rand <= population.getIndividual(i).getExpectedFitness()) {
                    matingPool.add(population.getIndividual(i));
                    System.out.println("Selected individual: " + population.getIndividual(i).getId() + ", F: " + population.getIndividual(i).getFitness() +  ", EF: " + population.getIndividual(i).getExpectedFitness());
                    pool++;
                    break;
                }
            }
        }
    }

    private void stochasticUniversalSelection() {
        matingPool.clear();
        population.sortByFitness();

        population.getIndividuals().stream().forEach(individual ->
                individual.setExpectedFitness((individual.getFitness() == 0) ? 2 : 1 / individual.getFitness()));

        population.reverse();

        double sumFitness = 0.0;
        for (Individual individual : population.getIndividuals()) {
            sumFitness += individual.getExpectedFitness();
            individual.setExpectedFitness(sumFitness);
        }

        double distance = sumFitness / poolSize;
        double start = Methods.getRandomDouble(0, distance);

        for (double i = start; i < sumFitness; i += distance) {
            for (Individual individual : population.getIndividuals()) {
                if (i <= individual.getExpectedFitness()) {
                    System.out.println("Selected individual: " + individual.getId() + ", F: " + individual.getFitness() + ", EF: " + individual.getExpectedFitness());
                    matingPool.add(individual);
                    break;
                }
            }
        }
    }

    public void setTournamentSize(int tournamentSize) {
        this.tournamentSize = tournamentSize;
    }
}