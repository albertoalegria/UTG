package com.alegria.utg.ga;

import com.alegria.utg.utilities.Methods;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * @author Alberto Alegria
 */
public class Population {
    private ArrayList<Individual> individuals;
    private int size;

    public Population(int size) {
        individuals = new ArrayList<>();
        this.size = size;
        initPopulation();
    }

    public void printPopFitness() {
        for (Individual individual : individuals) {
            System.out.println("Individual " + individual.getId() + ": " + individual.getFitness());
        }
    }

    public void setFitness(int i) {
        String fitness = "";
        double fit = 0;
        for (Individual individual : individuals) {
            //System.out.println("Setting fit: " + individual.getId());
            individual.setFitness(Fitness.getFitness(individual));
            fitness += individual.getId() + ": " + individual.getFitness() + "\n";
            fit += individual.getFitness();
        }

        double avgFit = fit / individuals.size();
        fitness += "Avg: " + avgFit;

        try {
            Methods.exportData(fitness, "/Users/alberto/Desktop/utg_data/fitness" + i + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initPopulation() {
        for (int i = 0; i < size; i++) {
            individuals.add(new Individual(i + 1));
        }
    }

    public void replacePopulation(ArrayList<Individual> newPopulation) {
        //System.out.println("Replacing");
        individuals.clear();
        individuals = newPopulation;

        int i = 0;
        for (Individual individual : individuals) {
            individual.setId(i);
            i++;
        }
    }

    public Individual getBest() {
        Individual best = individuals.get(0);

        for (Individual individual : individuals) {
            if (individual.getFitness() < best.getFitness()) {
                best = individual;
            }
        }
        return best;
    }

    public ArrayList<Individual> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(ArrayList<Individual> individuals) {
        this.individuals = individuals;
    }

    public void sortByFitness() {
        individuals.sort(Comparator.comparing(Individual::getFitness));
    }

    public void sortByExpectedFitness() {
        individuals.sort(Comparator.comparing(Individual::getExpectedFitness));
    }

    public void reverse() {
        Collections.reverse(individuals);
    }

    public void shuffle() {
        Collections.shuffle(individuals, new Random(System.nanoTime()));
    }

    public Individual getIndividual(int index) {
        return individuals.get(index);
    }

    public void clear() {
        individuals.clear();
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        String string = "";
        for (Individual individual : individuals) {
            string += "\nIndividual " + individual.getId() + ":\n" + individual.toString() + "\n";
        }
        return string + "# # # # # \n";
    }
}
