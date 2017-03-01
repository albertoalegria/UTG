package com.alegria.utg.ga;

import com.alegria.utg.utilities.Constants;
import com.alegria.utg.utilities.Methods;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author Alberto Alegria
 */
public class Mutation {

    private Individual individual;
    private double mutationRate;

    public Mutation(Individual individual, double mutationRate) {
        this.individual = individual;
        this.mutationRate = mutationRate;
    }

    public void mutate() {
        ArrayList<Gene> mutableGenes = individual.getGenes().stream().filter(gene -> gene.getCourse().getType() == Constants.Type.CLASSROOM).collect(Collectors.toCollection(ArrayList<Gene>::new));

        for (int i = 1; i < mutableGenes.size(); i++) {
            double mutation  = Methods.getRandomDouble(0, 1);

            if (mutation < mutationRate) {
                System.out.println("Mutation! " + mutableGenes.get(i).getPosition().toString());
                individual.mutate(mutableGenes.get(i), mutableGenes.get(i - 1));
            }
        }

        /*for (Gene gene : mutableGenes) {
            double mutation = Methods.getRandomDouble(0, 1);
            //System.out.println("Mutating");
            if (mutation < mutationRate) {
                //System.out.println("Mutating " + individual.getId());
                //System.out.println("Mutating!");
                //System.out.println("Gene position before: " + gene.getPosition().toString());
                individual.changeGenePosition(gene);
                //System.out.println("Gene position after: " + gene.getPosition().toString());
            }
        }*/
    }

    public Individual getMutatedIndividual() {
        return individual;
    }
}
