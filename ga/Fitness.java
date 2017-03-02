package com.alegria.utg.ga;

import com.alegria.utg.model.Classroom;
import com.alegria.utg.model.Course;
import com.alegria.utg.model.Teacher;
import com.alegria.utg.utilities.Constants;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * @author Alberto Alegria
 */
public class Fitness {
    public static int getFitness(Individual individual) {

        int a = getSizeConstraints(individual.getGenes());
        int b = getClassroomConstraints(individual.getClassrooms());
        int c = getTeachersConstraints(individual.getGenes());
        int d = getGroupConstraints(individual.getGenes(), individual.getCourses());

        //System.out.println(a + ", " + b + ",  " + c + ", " + d);

        return a + b + c + d;
    }

    private static double getStandardDeviation(ArrayList<Gene> dowClasses) {
        if (dowClasses.size() > 0){
            double[] dowGapValues = new double[dowClasses.size() - 1];

            for (int i = 1; i < dowClasses.size(); i++) {
                dowGapValues[i - 1] = dowClasses.get(i).getPosition().getTime().getHour() - dowClasses.get(i - 1).getPosition().getTime().getHour();
            }

            double mean = 0.0;
            for (int i = 0; i < dowGapValues.length; i++) {
                mean += dowGapValues[i];
            }

            mean = mean / dowGapValues.length;

            double variance = 0.0;
            for (int i = 0; i < dowGapValues.length; i++) {
                dowGapValues[i] = Math.pow((dowGapValues[i] - mean), 2) / dowGapValues.length;
                variance += dowGapValues[i];
            }

            return Math.sqrt(variance);
        } else {
            return 0;
        }
    }

    private static int getGroupConstraints(ArrayList<Gene> genes, ArrayList<Course> courses) {
        int constraints = 0;

        ArrayList<Integer> groups = new ArrayList<>();
        courses.stream().filter(course -> !groups.contains(course.getGroup())).forEach(course -> groups.add(course.getGroup()));

        for (Integer group : groups) {
            ArrayList<Gene> geneGroup = genes.stream().filter(gene -> gene.getCourse().getGroup() == group).collect(Collectors.toCollection(ArrayList<Gene>::new));

            ArrayList<Gene> mondayClasses = geneGroup.stream().filter(gene -> gene.getPosition().getTime().getDay().equals(Constants.Day.MONDAY)).collect(Collectors.toCollection(ArrayList<Gene>::new));
            ArrayList<Gene> tuesdayClasses = geneGroup.stream().filter(gene -> gene.getPosition().getTime().getDay().equals(Constants.Day.TUESDAY)).collect(Collectors.toCollection(ArrayList<Gene>::new));
            ArrayList<Gene> wednesdayClasses = geneGroup.stream().filter(gene -> gene.getPosition().getTime().getDay().equals(Constants.Day.WEDNESDAY)).collect(Collectors.toCollection(ArrayList<Gene>::new));
            ArrayList<Gene> thursdayClasses = geneGroup.stream().filter(gene -> gene.getPosition().getTime().getDay().equals(Constants.Day.THURSDAY)).collect(Collectors.toCollection(ArrayList<Gene>::new));
            ArrayList<Gene> fridayClasses = geneGroup.stream().filter(gene -> gene.getPosition().getTime().getDay().equals(Constants.Day.FRIDAY)).collect(Collectors.toCollection(ArrayList<Gene>::new));

            mondayClasses.sort(Comparator.comparing(gene -> gene.getPosition().getTime().getHour()));
            tuesdayClasses.sort(Comparator.comparing(gene -> gene.getPosition().getTime().getHour()));
            wednesdayClasses.sort(Comparator.comparing(gene -> gene.getPosition().getTime().getHour()));
            thursdayClasses.sort(Comparator.comparing(gene -> gene.getPosition().getTime().getHour()));
            fridayClasses.sort(Comparator.comparing(gene -> gene.getPosition().getTime().getHour()));

            /*double mondaySD = getStandardDeviation(mondayClasses);
            double tuesdaySD = getStandardDeviation(tuesdayClasses);
            double wednesdaySD = getStandardDeviation(wednesdayClasses);
            double thursdaySD = getStandardDeviation(thursdayClasses);
            double fridaySD = getStandardDeviation(fridayClasses);

            if (mondaySD > 0.5) {
                constraints++;
            }
            if (tuesdaySD > 0.5) {
                constraints++;
            }
            if (wednesdaySD > 0.5) {
                constraints++;
            }
            if (thursdaySD > 0.5) {
                constraints++;
            }
            if (fridaySD > 0.5) {
                constraints++;
            }*/

            double totalStandardDeviation = getStandardDeviation(mondayClasses) + getStandardDeviation(tuesdayClasses) + getStandardDeviation(wednesdayClasses)
                    + getStandardDeviation(thursdayClasses) + getStandardDeviation(fridayClasses);

            if (totalStandardDeviation > 2.5) {
                constraints++;
            }
        }


        return constraints;
    }

    private static int getSizeConstraints(ArrayList<Gene> genes) {
        int constraint = 0;
        for (Gene gene : genes) {
            if (gene.getPosition().getClassroom().getSize() < gene.getCourse().getSize()) {
                constraint = 1;
            }
        }

        return constraint;
    }

    private static int getClassroomConstraints(ArrayList<Classroom> classrooms) {
        double count = 0.0;

        for (Classroom classroom : classrooms) {
            if (classroom.getType() == Constants.Type.CLASSROOM) {
                double occupation = (70.0 - classroom.getAvailableTimes().size()) / 70.0;
                count += occupation;
            }
        }

        double avg = count / classrooms.size();
        avg = avg * 100;

        int constraint = 0;

        for (Classroom classroom : classrooms) {
            if (classroom.getType() == Constants.Type.CLASSROOM) {
                double occupation = (70.0 - classroom.getAvailableTimes().size()) / 70.0;
                occupation = occupation * 100;
                if (Math.abs(occupation - avg) > 50) {
                    constraint = 1;
                }
            }
        }

        return constraint;
    }


    public static int getTeachersConstraints(ArrayList<Gene> genes) {
        int size = 0;

        ArrayList<Teacher> teachers = new ArrayList<>();
        genes.stream().filter(gene -> !teachers.contains(gene.getTeacher())).forEach(gene -> teachers.add(gene.getTeacher()));

        ArrayList<Teacher> teachersWithCourses = new ArrayList<>();
        genes.stream().filter(gene -> !teachersWithCourses.contains(gene.getTeacher())).forEach(gene -> teachersWithCourses.add(gene.getTeacher()));

        if (teachers.size() != teachersWithCourses.size()) {
            size = 1;
        }


        int available = 0;
        for (Teacher teacher : teachers) {
            if (teacher.getPreferredTimes().size() < teacher.getTimesSize()) {
                available++;
            }
            /*int teacherTime = 0;
            for (Time time : teacher.getUnavailableTimes()) {
                if (!teacher.getPreferredTimes().contains(time)) {
                    teacherTime++;
                }
            }

            if (teacherTime > 1) {
                available++;
            }*/
        }

        //System.out.println("Teacher: " + size + ", " + available);
        return size + available;
    }
}


/*if (mondayClasses.size() > 0) {
                double[] mondayGapValues = new double[mondayClasses.size() - 1];

                for (int i = 1; i < mondayClasses.size(); i++) {
                    mondayGapValues[i - 1] = mondayClasses.retrieve(i).getPosition().getTime().getHour() - mondayClasses.retrieve(i - 1).getPosition().getTime().getHour();
                }

                double mean = 0.0;
                for (int i = 0; i < mondayGapValues.length; i++) {
                    mean += mondayGapValues[i];
                }

                mean = mean / mondayGapValues.length;

                double variance = 0.0;
                for (int i = 0; i < mondayGapValues.length; i++) {
                    mondayGapValues[i] = Math.pow((mondayGapValues[i] - mean), 2) / mondayGapValues.length;
                    variance += mondayGapValues[i];
                }
                double standardDeviation = Math.sqrt(variance);
            }

            if (tuesdayClasses.size() > 0) {
                double[] tuesdayGapValues = new double[mondayClasses.size() - 1];

                for (int i = 1; i < tuesdayClasses.size(); i++) {
                    tuesdayGapValues[i - 1] = tuesdayClasses.retrieve(i).getPosition().getTime().getHour() - tuesdayClasses.retrieve(i - 1).getPosition().getTime().getHour();
                }

                double mean = 0.0;
                for (int i = 0; i < tuesdayGapValues.length; i++) {
                    mean += tuesdayGapValues[i];
                }

                mean = mean / tuesdayGapValues.length;

                double variance = 0.0;
                for (int i = 0; i < tuesdayGapValues.length; i++) {
                    tuesdayGapValues[i] = Math.pow((tuesdayGapValues[i] - mean), 2) / tuesdayGapValues.length;
                    variance += tuesdayGapValues[i];
                }
                double standardDeviation = Math.sqrt(variance);
            }

            if (wednesdayClasses.size() > 0) {
                double[] wednesdayGapValues = new double[mondayClasses.size() - 1];

                for (int i = 1; i < wednesdayClasses.size(); i++) {
                    wednesdayGapValues[i - 1] = wednesdayClasses.retrieve(i).getPosition().getTime().getHour() - wednesdayClasses.retrieve(i - 1).getPosition().getTime().getHour();
                }

                double mean = 0.0;
                for (int i = 0; i < wednesdayGapValues.length; i++) {
                    mean += wednesdayGapValues[i];
                }

                mean = mean / wednesdayGapValues.length;

                double variance = 0.0;
                for (int i = 0; i < wednesdayGapValues.length; i++) {
                    wednesdayGapValues[i] = Math.pow((wednesdayGapValues[i] - mean), 2) / wednesdayGapValues.length;
                    variance += wednesdayGapValues[i];
                }
                double standardDeviation = Math.sqrt(variance);
            }

            if (thursdayClasses.size() > 0) {
                double[] thursdayGapValues = new double[mondayClasses.size() - 1];

                for (int i = 1; i < thursdayClasses.size(); i++) {
                    thursdayGapValues[i - 1] = thursdayClasses.retrieve(i).getPosition().getTime().getHour() - thursdayClasses.retrieve(i - 1).getPosition().getTime().getHour();
                }

                double mean = 0.0;
                for (int i = 0; i < thursdayGapValues.length; i++) {
                    mean += thursdayGapValues[i];
                }

                mean = mean / thursdayGapValues.length;

                double variance = 0.0;
                for (int i = 0; i < thursdayGapValues.length; i++) {
                    thursdayGapValues[i] = Math.pow((thursdayGapValues[i] - mean), 2) / thursdayGapValues.length;
                    variance += thursdayGapValues[i];
                }
                double standardDeviation = Math.sqrt(variance);
            }

            if (fridayClasses.size() > 0) {
                double[] fridayGapValues = new double[mondayClasses.size() - 1];

                for (int i = 1; i < fridayClasses.size(); i++) {
                    fridayGapValues[i - 1] = fridayClasses.retrieve(i).getPosition().getTime().getHour() - fridayClasses.retrieve(i - 1).getPosition().getTime().getHour();
                }

                double mean = 0.0;
                for (int i = 0; i < fridayGapValues.length; i++) {
                    mean += fridayGapValues[i];
                }

                mean = mean / fridayGapValues.length;

                double variance = 0.0;
                for (int i = 0; i < fridayGapValues.length; i++) {
                    fridayGapValues[i] = Math.pow((fridayGapValues[i] - mean), 2) / fridayGapValues.length;
                    variance += fridayGapValues[i];
                }
                double standardDeviation = Math.sqrt(variance);
            }*/