package com.alegria.utg.ga;

import com.alegria.utg.model.Course;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author Alberto Alegria
 */
public class Crossover {
    private Individual firstSelected;
    private Individual secondSelected;

    private Individual firstDescendant;
    private Individual secondDescendant;

    public Crossover(Individual firstSelected, Individual secondSelected) {
        this.firstSelected = firstSelected;
        this.secondSelected = secondSelected;

        System.out.println("Selected individuals: " + firstSelected.getId() + ", " + secondSelected.getId());

        firstDescendant = new Individual();
        System.out.println();
        secondDescendant = new Individual();

        crossover();
    }

    private void crossover() {
        System.out.println("Performing crossover");

        //TODO hacerlo random
        int middle = firstSelected.getCoursesSize() / 2; //Methods.getRandomDouble(1, (firstSelected.getCoursesSize() / 2) - 2);
        ArrayList<Gene> firstGenes = new ArrayList<>();
        ArrayList<Gene> secondGenes = new ArrayList<>();

        int i = 0;
        for (Course course : firstSelected.getCourses()) {
            if (i < middle) {
                firstGenes.addAll(firstSelected.getGenes().stream().filter(gene -> gene.getCourse().equals(course)).collect(Collectors.toCollection(ArrayList<Gene>::new)));
                secondGenes.addAll(secondSelected.getGenes().stream().filter(gene -> gene.getCourse().equals(course)).collect(Collectors.toCollection(ArrayList<Gene>::new)));
            } else {
                firstGenes.addAll(secondSelected.getGenes().stream().filter(gene -> gene.getCourse().equals(course)).collect(Collectors.toCollection(ArrayList<Gene>::new)));
                secondGenes.addAll(firstSelected.getGenes().stream().filter(gene -> gene.getCourse().equals(course)).collect(Collectors.toCollection(ArrayList<Gene>::new)));
            }
            i++;
        }

        System.out.println("first size: " + firstGenes.size());
        System.out.println("second size: " + secondGenes.size());


        firstDescendant.setNewGenes(middle, firstGenes);
        secondDescendant.setNewGenes(middle, secondGenes);

        //System.out.println("First descendant genes size: " + firstGenes.size());
        //System.out.println("Second descendant genes size: " + secondGenes.size());

        //firstDescendant.setId(10);
        //secondDescendant.setId(20);


        //firstDescendant.fixGenes();
        //secondDescendant.fixGenes();
    }

    public Individual getFirstDescendant() {
        return firstDescendant;
    }

    public Individual getSecondDescendant() {
        return secondDescendant;
    }
}
