package com.alegria.utg.ga;

import com.alegria.utg.data.Data;
import com.alegria.utg.model.Classroom;
import com.alegria.utg.model.Course;
import com.alegria.utg.model.Teacher;
import com.alegria.utg.model.Time;
import com.alegria.utg.utilities.Constants;
import com.alegria.utg.utilities.Methods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * @author Alberto Alegria
 */
public class Individual {
    private Gene[][] timetable;
    private ArrayList<Gene> genes;
    private double fitness;
    private double expectedFitness;
    private int rank;
    private int id;
    private int classroomCount;
    private ArrayList<Course> courses;
    private ArrayList<Teacher> teachers;
    private ArrayList<Classroom> classrooms;

    public Individual() {
        genes = new ArrayList<>();
        fitness = 0.0;
        expectedFitness = 0.0;
        rank = 0;
        classroomCount = 0;
        id = 0;

        courses = new ArrayList<>();
        teachers = new ArrayList<>();
        classrooms = new ArrayList<>();
    }

    public Individual(Individual copy) {
        genes = new ArrayList<>();
        this.fitness = copy.fitness;
        this.expectedFitness = copy.expectedFitness;
        this.rank = copy.rank;
        this.classroomCount = copy.classroomCount;
        this.timetable = new Gene[this.classroomCount][Constants.TIMES_COUNT];
        this.id = copy.id;

        courses = new ArrayList<>();
        teachers = new ArrayList<>();
        classrooms = new ArrayList<>();

        initTimetable();

    }
    public Individual(int id) {
        Data.get();
        this.classroomCount = Data.getClassroomsSize();
        timetable = new Gene[classroomCount][Constants.TIMES_COUNT];
        genes = new ArrayList<>();

        fitness = 0.0;
        expectedFitness = 0.0;
        rank = 0;

        this.id = id;

        courses = new ArrayList<>(Data.getCourses());

        teachers = new ArrayList<>(Data.getTeachers());
        classrooms = new ArrayList<>(Data.getClassrooms());

        initTimetable();
        initCourses();
        arrangeCourses();

        Data.close();
    }

    public void clear() {
        genes.clear();
        initTimetable();
    }

    private void initTimetable() {
        System.out.println("Initializing timetable for " + id);
        for (int i = 0; i < classroomCount; i++) {
            for (int j = 0; j < Constants.TIMES_COUNT; j++) {
                timetable[i][j] = new Gene();
            }
        }
    }

    private void initCourses() {
        int count = 0;
        System.out.println("Initializing courses for " + id);

        for (Course course : courses) {
            Gene gene = new Gene.Builder().setCourse(course).setTeacher(getRandomTeacher(course)).build();


            for (int i = 0; i < course.getHours(); i++) {
                genes.add(gene);
                gene.getTeacher().addCourse(course);
                count++;
            }
        }
    }

    private void arrangeCourses() {
        ArrayList<Gene> genes2 = new ArrayList<>(genes);
        genes.clear();

        genes.addAll(genes2.stream().map(Gene::new).collect(Collectors.toList()));

        courses.sort(Comparator.comparing(Course::getType));
        Collections.reverse(courses);

        for (Course course : courses) {
            ArrayList<Gene> courseGenes = genes.stream().filter(gene -> gene.getCourse().equals(course)).collect(Collectors.toCollection(ArrayList<Gene>::new));
            getCoursesPosition(courseGenes);
        }
    }

    private void getCoursesPosition(ArrayList<Gene> courseGenes) {
        Gene first = courseGenes.get(0);
        first.setPosition(getFirstClassPosition(first));

        timetable[first.getPosition().getClassroomId()][first.getPosition().getTimeId()] = first;

        Gene second = courseGenes.get(1);
        second.setPosition(getSecondClassPosition(first, second));

        timetable[second.getPosition().getClassroomId()][second.getPosition().getTimeId()] = second;

        for (int i = 2; i < courseGenes.size(); i++) {

            ArrayList<Gene> prevGenes = courseGenes.stream().filter(gene -> gene.getPosition() != null).collect(Collectors.toCollection(ArrayList<Gene>::new));
            long monday =    prevGenes.stream().filter(gene -> gene.getPosition().getTime().getDay().equals(Constants.Day.MONDAY)).count();
            long tuesday =   prevGenes.stream().filter(gene -> gene.getPosition().getTime().getDay().equals(Constants.Day.TUESDAY)).count();
            long wednesday = prevGenes.stream().filter(gene -> gene.getPosition().getTime().getDay().equals(Constants.Day.WEDNESDAY)).count();
            long thursday =  prevGenes.stream().filter(gene -> gene.getPosition().getTime().getDay().equals(Constants.Day.THURSDAY)).count();
            long friday =    prevGenes.stream().filter(gene -> gene.getPosition().getTime().getDay().equals(Constants.Day.FRIDAY)).count();

            if (monday > 1) {
                courseGenes.get(i).getCourse().getAvailableTimes().removeAll(courseGenes.get(i).getCourse().getAvailableTimes().stream().filter(time -> time.getDay().equals(Constants.Day.MONDAY)).collect(Collectors.toCollection(ArrayList<Time>::new)));
            }

            if (tuesday > 1) {
                courseGenes.get(i).getCourse().getAvailableTimes().removeAll(courseGenes.get(i).getCourse().getAvailableTimes().stream().filter(time -> time.getDay().equals(Constants.Day.TUESDAY)).collect(Collectors.toCollection(ArrayList<Time>::new)));
            }

            if (wednesday > 1) {
                courseGenes.get(i).getCourse().getAvailableTimes().removeAll(courseGenes.get(i).getCourse().getAvailableTimes().stream().filter(time -> time.getDay().equals(Constants.Day.WEDNESDAY)).collect(Collectors.toCollection(ArrayList<Time>::new)));
            }

            if (thursday > 1) {
                courseGenes.get(i).getCourse().getAvailableTimes().removeAll(courseGenes.get(i).getCourse().getAvailableTimes().stream().filter(time -> time.getDay().equals(Constants.Day.THURSDAY)).collect(Collectors.toCollection(ArrayList<Time>::new)));
            }

            if (friday > 1) {
                courseGenes.get(i).getCourse().getAvailableTimes().removeAll(courseGenes.get(i).getCourse().getAvailableTimes().stream().filter(time -> time.getDay().equals(Constants.Day.FRIDAY)).collect(Collectors.toCollection(ArrayList<Time>::new)));
            }

            courseGenes.get(i).setPosition(getClassPosition(courseGenes.get(i), courseGenes.get(i - 1)));
            timetable[courseGenes.get(i).getPosition().getClassroomId()][courseGenes.get(i).getPosition().getTimeId()] = courseGenes.get(i);
        }


    }

    public void mutate(Gene gene, Gene prev) {
        //TODO Fix random crash here
        ArrayList<Gene> courseGenes = genes.stream().filter(gene1 -> gene1.getCourse().equals(gene.getCourse())).collect(Collectors.toCollection(ArrayList<Gene>::new));

        //System.out.println("Mutating");
        //System.out.println("Old pos: " + gene.getPosition().toString());
        timetable[gene.getPosition().getClassroomId()][gene.getPosition().getTimeId()] = new Gene();
        //gene.getCourse().getAvailableTimes().add(gene.getPosition().getTime());
        Position pos = gene.getPosition();
        genes.stream().filter(gene1 -> gene1.getCourse().getGroup() == gene.getCourse().getGroup()).forEach(gene1 -> gene1.getCourse().getAvailableTimes().add(pos.getTime()));
        //gene.getTeacher().getAvailableTimes().add(gene.getPosition().getTime());
        gene.getTeacher().addTime(gene.getPosition().getTime());
        gene.getPosition().getClassroom().getAvailableTimes().add(gene.getPosition().getTime());


        long monday =    courseGenes.stream().filter(gene1 -> gene1.getPosition().getTime().getDay().equals(Constants.Day.MONDAY)).count();
        long tuesday =   courseGenes.stream().filter(gene1 -> gene1.getPosition().getTime().getDay().equals(Constants.Day.TUESDAY)).count();
        long wednesday = courseGenes.stream().filter(gene1 -> gene1.getPosition().getTime().getDay().equals(Constants.Day.WEDNESDAY)).count();
        long thursday =  courseGenes.stream().filter(gene1 -> gene1.getPosition().getTime().getDay().equals(Constants.Day.THURSDAY)).count();
        long friday =    courseGenes.stream().filter(gene1 -> gene1.getPosition().getTime().getDay().equals(Constants.Day.FRIDAY)).count();

        if (monday > 1) {
            courseGenes.forEach(gene1 -> gene1.getCourse().getAvailableTimes().removeAll(gene.getCourse().getAvailableTimes().stream().filter(time -> time.getDay().equals(Constants.Day.MONDAY)).collect(Collectors.toCollection(ArrayList<Time>::new))));
        }

        if (tuesday > 1) {
            courseGenes.forEach(gene1 -> gene1.getCourse().getAvailableTimes().removeAll(gene.getCourse().getAvailableTimes().stream().filter(time -> time.getDay().equals(Constants.Day.TUESDAY)).collect(Collectors.toCollection(ArrayList<Time>::new))));
        }

        if (wednesday > 1) {
            courseGenes.forEach(gene1 -> gene1.getCourse().getAvailableTimes().removeAll(gene.getCourse().getAvailableTimes().stream().filter(time -> time.getDay().equals(Constants.Day.WEDNESDAY)).collect(Collectors.toCollection(ArrayList<Time>::new))));
        }

        if (thursday > 1) {
            courseGenes.forEach(gene1 -> gene1.getCourse().getAvailableTimes().removeAll(gene.getCourse().getAvailableTimes().stream().filter(time -> time.getDay().equals(Constants.Day.THURSDAY)).collect(Collectors.toCollection(ArrayList<Time>::new))));
        }

        if (friday > 1) {
            courseGenes.forEach(gene1 -> gene1.getCourse().getAvailableTimes().removeAll(gene.getCourse().getAvailableTimes().stream().filter(time -> time.getDay().equals(Constants.Day.FRIDAY)).collect(Collectors.toCollection(ArrayList<Time>::new))));
        }

        gene.setPosition(getClassPosition(gene, prev));

        Position p = gene.getPosition();
        gene.getTeacher().removeTime(gene.getPosition().getTime());
        genes.stream().filter(gene1 -> gene1.getCourse().getGroup() == gene.getCourse().getGroup()).forEach(gene1 -> gene1.getCourse().getAvailableTimes().remove(p.getTime()));
        //System.out.println("New pos: " + gene.getPosition().toString());
        timetable[gene.getPosition().getClassroomId()][gene.getPosition().getTimeId()] = gene;
    }

    private Position getClassPosition(Gene gene, Gene prev) {
        boolean teacherHasNoTimes = false;
        Position position;

        if (prev.getPosition().getTime().getHour() < 7) {
            if (timetable[prev.getPosition().getClassroomId()][prev.getPosition().getTimeId() + 1].isNull() && gene.getTeacher().getAvailableTimes().contains(new Time(prev.getPosition().getTime().getAbsHour() + 1)) && gene.getCourse().getAvailableTimes().contains(new Time(prev.getPosition().getTime().getAbsHour() + 1))) {
                position = new Position(prev.getPosition().getClassroom(), new Time(prev.getPosition().getTime().getAbsHour() + 1));

                Position p = position;

                genes.stream().filter(gene1 -> gene1.getCourse().getGroup() == gene.getCourse().getGroup()).forEach(gene1 -> gene1.getCourse().getAvailableTimes().remove(p.getTime()));

                gene.getTeacher().removeTime(position.getTime());
                position.getClassroom().getAvailableTimes().remove(position.getTime());

                return position;
            }
        }

        ArrayList<Classroom> classrooms = this.classrooms.stream().filter(classroom -> classroom.getType() == Constants.Type.CLASSROOM).collect(Collectors.toCollection(ArrayList<Classroom>::new));

        ArrayList<Time> times = gene.getCourse().getAvailableTimes();

        times = times.stream().filter(time -> gene.getTeacher().getPreferredTimes().contains(time)).collect(Collectors.toCollection(ArrayList<Time>::new));

        if (times.size() == 0) {
            teacherHasNoTimes = true;
            System.out.println("Teacher out of available times");
            times = gene.getCourse().getAvailableTimes();
            times = times.stream().filter(time -> gene.getTeacher().getAvailableTimes().contains(time)).collect(Collectors.toCollection(ArrayList<Time>::new));
            System.out.println("S: " + times.size() + ", " + gene.getTeacher().getPreferredTimes() + ", " + gene.getCourse().getAvailableTimes());
        }

        ArrayList<Classroom> classroomAux = new ArrayList<>();

        for (Classroom classroom : classrooms) {
            int count = 0;
            for (Time time : times) {
                if (classroom.getAvailableTimes().contains(time)) {
                    count++;
                }
            }
            if (count > 0) {
                classroomAux.add(classroom);
            }
        }

        //System.out.println(gene.getCourseId() + " times size: " + times.size() + " ");
        //System.out.println("Classroom size: " + classroomAux.size());

        Classroom classroom = classroomAux.get(Methods.getRandomNumber(0, classroomAux.size() - 1));

        Time time = times.get(Methods.getRandomNumber(0, times.size() - 1));

        position = new Position(classroom, time);

        int max = times.size();
        int i = 0;
        while (!timetable[position.getClassroomId()][position.getTimeId()].isNull()) {
            if (i >= max){
                classroom = classroomAux.get(Methods.getRandomNumber(0, classroomAux.size() - 1));
                i = 0;
                System.out.println("Classroom changed");
            }
            System.out.println("Searching class position");
            //System.out.println("Times: " + Methods.timesToString(times));
            //System.out.println("Time: " + time.getAbsHour());
            //System.out.println("Classroom " + classroom.getId() + ": " + classroomAux.size() + ", T: " + Methods.timesToString(classroom.getAvailableTimes()));
            //System.out.println("Position: " + position.toString());
            time = times.get(Methods.getRandomNumber(0, times.size() - 1));
            position = new Position(classroom, time);
            i++;
        }

        //gene.getCourse().getAvailableTimes().remove(position.getTime());
        //gene.getTeacher().getAvailableTimes().remove(position.getTime());
        gene.getTeacher().removeTime(position.getTime());
        classroom.getAvailableTimes().remove(time);

        //System.out.println("Size before: " + gene.getCourse().getAvailableTimes().size());

        Position p = position;
        genes.stream().filter(gene1 -> gene1.getCourse().getGroup() == gene.getCourse().getGroup()).forEach(gene1 -> gene1.getCourse().getAvailableTimes().remove(p.getTime()));

        return position;
    }

    private Position getSecondClassPosition(Gene first, Gene second) {
        Position position;
        if (timetable[first.getPosition().getClassroomId()][first.getPosition().getTimeId() + 1].isNull() && second.getTeacher().getPreferredTimes().contains(new Time(first.getPosition().getTime().getAbsHour() + 1)) && second.getCourse().getAvailableTimes().contains(new Time(first.getPosition().getTime().getAbsHour() + 1))) {
            position = new Position(first.getPosition().getClassroom(), new Time(first.getPosition().getTime().getAbsHour() + 1));
        } else {
            position = getClassPosition(second, first);
        }

        second.getTeacher().removeTime(position.getTime());
        position.getClassroom().getAvailableTimes().remove(position.getTime());

        genes.stream().filter(gene1 -> gene1.getCourse().getGroup() == second.getCourse().getGroup()).forEach(gene1 -> gene1.getCourse().getAvailableTimes().remove(position.getTime()));

        return position;
    }

    private Position getFirstClassPosition(Gene gene) {
        boolean teacherHasNoTimes = false;
        Classroom classroom;
        ArrayList<Classroom> classrooms = this.classrooms.stream().filter(c -> c.getType() == Constants.Type.CLASSROOM).collect(Collectors.toCollection(ArrayList<Classroom>::new));
        ArrayList<Classroom> classroomAux = new ArrayList<>();

        ArrayList<Time> times = gene.getCourse().getAvailableTimes();
        times = times.stream().filter(time -> gene.getTeacher().getPreferredTimes().contains(time)).collect(Collectors.toCollection(ArrayList<Time>::new));

        if (times.size() == 0) {
            times = gene.getCourse().getAvailableTimes();
            times = times.stream().filter(time -> gene.getTeacher().getAvailableTimes().contains(time)).collect(Collectors.toCollection(ArrayList<Time>::new));
            teacherHasNoTimes = true;
        }

        times = times.stream().filter(time -> time.getHour() < 14).collect(Collectors.toCollection(ArrayList<Time>::new));

        if (gene.getCourse().getType() == Constants.Type.LABORATORY) {

            if (gene.getCourse().getName().equals("REDES1S6A") || gene.getCourse().getName().equals("REDES1S6B")
                    || gene.getCourse().getName().equals("REDES2S7A") || gene.getCourse().getName().equals("REDES2S7B") ||
                    gene.getCourse().getName().equals("ADMONREDS8A") || gene.getCourse().getName().equals("ADMONREDS8B")) {
                classroom = this.classrooms.get(18);
            } else if (gene.getCourse().getName().equals("FISGRALS3A") || gene.getCourse().getName().equals("FISGRALS3B")) {
                classroom = this.classrooms.get(19);
            } else {
                classroom = this.classrooms.get(20);
            }
        } else {
            for (Classroom classroom1 : classrooms) {
                int count = 0;
                for (Time time : times) {
                    if (classroom1.getAvailableTimes().contains(time)) {
                        count++;
                    }
                    if (count > 0) {
                        classroomAux.add(classroom1);
                    }
                }
            }
            classroom = classroomAux.get(Methods.getRandomNumber(0, classroomAux.size() - 1));
        }



        Time time = times.get(Methods.getRandomNumber(0, times.size() - 1));

        Position position = new Position(classroom, time);

        //TODO Do boolean comparison inside while
        boolean posNull = !timetable[position.getClassroomId()][position.getTimeId()].isNull();
        boolean nextPos = !timetable[position.getClassroomId()][position.getTimeId() + 1].isNull();
        boolean nxtTime = !gene.getTeacher().getPreferredTimes().contains(new Time(time.getAbsHour() + 1));
        if (teacherHasNoTimes) {
            nxtTime = !gene.getTeacher().getAvailableTimes().contains(new Time(time.getAbsHour() + 1));
        }

        if (gene.getCourse().getType() == Constants.Type.CLASSROOM) {
            int count = 0;
            while (posNull || nextPos || nxtTime) {
                time = times.get(Methods.getRandomNumber(0, times.size() - 1));
                position = new Position(classroom, time);

                posNull = !timetable[position.getClassroomId()][position.getTimeId()].isNull();
                nextPos = !timetable[position.getClassroomId()][position.getTimeId() + 1].isNull();
                nxtTime = !gene.getTeacher().getPreferredTimes().contains(new Time(time.getAbsHour() + 1));

                if (teacherHasNoTimes) {
                    nxtTime = !gene.getTeacher().getAvailableTimes().contains(new Time(time.getAbsHour() + 1));
                }

                if (count == 100) {
                    break;
                }
                System.out.println("Searching classroom first class");
                count++;
            }
            int i = 0;
            while (posNull) {

                if (i == 100) {
                    classroom = classroomAux.get(Methods.getRandomNumber(0, classroomAux.size() - 1));
                    i = 0;
                    System.out.println("Classroom changed");
                }

                System.out.println("Searching class first class2");
                time = times.get(Methods.getRandomNumber(0, times.size() - 1));
                position = new Position(classroom, time);
                posNull = !timetable[position.getClassroomId()][position.getTimeId()].isNull();
                i++;
            }

        } else {
            while (posNull || nextPos || nxtTime) {
                System.out.println("Searching classroom first class3");
                time = times.get(Methods.getRandomNumber(0, times.size() - 1));
                position = new Position(classroom, time);

                posNull = !timetable[position.getClassroomId()][position.getTimeId()].isNull();
                nextPos = !timetable[position.getClassroomId()][position.getTimeId() + 1].isNull();
                nxtTime = !gene.getTeacher().getPreferredTimes().contains(new Time(time.getAbsHour() + 1));
                if (teacherHasNoTimes) {
                    nxtTime = !gene.getTeacher().getAvailableTimes().contains(new Time(time.getAbsHour() + 1));
                }
            }
        }

        gene.getTeacher().removeTime(position.getTime());
        classroom.getAvailableTimes().remove(time);

        Position p = position;

        genes.stream().filter(gene1 -> gene1.getCourse().getGroup() == gene.getCourse().getGroup()).forEach(gene1 -> gene1.getCourse().getAvailableTimes().remove(p.getTime()));

        return position;
    }

    private Position getValidPosition(Gene gene) {
        ArrayList<Time> times = gene.getCourse().getAvailableTimes();
        times = times.stream().filter(time -> gene.getTeacher().getAvailableTimes().contains(time)).collect(Collectors.toCollection(ArrayList<Time>::new));

        Time time = times.get(Methods.getRandomNumber(0, times.size() - 1));

        ArrayList<Classroom> classrooms = this.classrooms.stream().filter(classroom -> classroom.getAvailableTimes().contains(time)).collect(Collectors.toCollection(ArrayList<Classroom>::new));

        if (gene.getCourse().getType() == 1) {
            classrooms = classrooms.stream().filter(classroom -> classroom.getType() == Constants.Type.CLASSROOM).collect(Collectors.toCollection(ArrayList<Classroom>::new));
        }

        Classroom classroom = classrooms.get(Methods.getRandomNumber(0, classrooms.size() - 1));

        Position position = new Position(classroom, time);


        while (!timetable[position.getClassroomId()][position.getTimeId()].isNull()) {
            Time time1 = times.get(Methods.getRandomNumber(0, times.size() - 1));

            ArrayList<Classroom> classrooms1 = this.classrooms.stream().filter(classroom1 -> classroom1.getAvailableTimes().contains(time1)).collect(Collectors.toCollection(ArrayList<Classroom>::new));

            Classroom classroom1 = classrooms1.get(Methods.getRandomNumber(0, classrooms1.size() - 1));

            position = new Position(classroom1, time1);
        }

        position.getClassroom().getAvailableTimes().remove(position.getTime());

        return position;
    }


    private Teacher getRandomTeacher(Course course) {
        ArrayList<Teacher> teachers = this.teachers.stream().filter(teacher -> teacher.getPossibleCourses().contains(course)).collect(Collectors.toCollection(ArrayList<Teacher>::new));

        return teachers.get(Methods.getRandomNumber(0, teachers.size() - 1));
    }

    //Getters and Setters

    public Gene[][] getTimetable() {
        return timetable;
    }

    public void setTimetable(Gene[][] timetable) {
        this.timetable = timetable;
    }

    public ArrayList<Gene> getGenes() {
        return genes;
    }

    private Gene getGene(Course course, ArrayList<Gene> genes1) {
        Gene gene = null;
        for (Gene gene1 : genes1) {
            if (gene1.getCourse().equals(course)) {
                gene = gene1;
            }
        }
        return gene;
    }

    public void setNewGenes(int middle, ArrayList<Gene> newGenes) {
        //System.out.println("New Genes size: " + newGenes.size());
        courses.clear();
        teachers.clear();
        classrooms.clear();
        genes.clear();

        Data.get();

        courses = new ArrayList<>(Data.getCourses());
        teachers = new ArrayList<>(Data.getTeachers());
        classrooms = new ArrayList<>(Data.getClassrooms());

        classroomCount = classrooms.size();
        timetable = new Gene[classroomCount][Constants.TIMES_COUNT];

        Data.close();

        initTimetable();

        ArrayList<Gene> aux = new ArrayList<>();
        newGenes.stream().filter(gene -> !aux.contains(gene)).forEach(aux::add);

        for (Gene gene1 : aux) {
            Gene gene = new Gene.Builder().setCourse(getCourse(gene1.getCourseId())).setTeacher(getTeacher(gene1.getTeacherId())).build();

            for (int i = 0; i < gene1.getCourse().getHours(); i++) {
                genes.add(gene);
            }
        }

        ArrayList<Gene> genes2 = new ArrayList<>(genes);

        genes.clear();
        genes.addAll(genes2.stream().map(Gene::new).collect(Collectors.toList()));

        //System.out.println("New genes size 2 " + newGenes.size());

        courses.sort(Comparator.comparing(Course::getType));
        Collections.reverse(courses);

        int i = 0;
        for (Course course : courses) {
            ArrayList<Gene> geneCourses = genes.stream().filter(gene -> gene.getCourse().equals(course)).collect(Collectors.toCollection(ArrayList<Gene>::new));

            if (i < middle) {
                ArrayList<Gene> auxGenes = newGenes.stream().filter(g -> g.getCourse().getId() == course.getId()).collect(Collectors.toCollection(ArrayList<Gene>::new));

          //      System.out.println(i + " New genes size 3: " + newGenes.size());
                setOldPosition(geneCourses, auxGenes);
            } else {
            //    System.out.println("Setting new positions");
                getCoursesPosition(geneCourses);
            }
            i++;
        }

        /*int i = 0;
        for (Gene gene : genes) {
            Gene geneAux = newGenes.get(i);
            if (timetable[geneAux.getPosition().getClassroomId()][geneAux.getPosition().getTimeId()].isNull()) {
                gene.setPosition(new Position(geneAux.getPosition()));
            } else {

            }
        }*/
    }

    private void setOldPosition(ArrayList<Gene> courseGenes, ArrayList<Gene> prevGenes) {
        int i = 0;
        //System.out.println("CourseGenes: " + courseGenes.size() + ",  " + prevGenes.size());
        //System.out.println("Setting old position");
        for (Gene gene : courseGenes) {
            gene.setPosition(new Position(prevGenes.get(i).getPosition()));
            timetable[gene.getPosition().getClassroomId()][gene.getPosition().getTimeId()] = gene;
            //gene.getTeacher().getAvailableTimes().remove(gene.getPosition().getTime());
            gene.getTeacher().removeTime(gene.getPosition().getTime());
            gene.getPosition().getClassroom().getAvailableTimes().remove(gene.getPosition().getTime());
            Position p = gene.getPosition();
            genes.stream().filter(gene1 -> gene1.getCourse().getGroup() == gene.getCourse().getGroup()).forEach(gene1 -> gene1.getCourse().getAvailableTimes().remove(p.getTime()));
            //gene.getCourse().getAvailableTimes().remove(gene.getPosition().getTime());
          //  System.out.println("Course " + gene.getCourseId() + " doesn't change position " + gene.getPosition().toString());
            i++;
        }
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getExpectedFitness() {
        return expectedFitness;
    }

    public void setExpectedFitness(double expectedFitness) {
        this.expectedFitness = expectedFitness;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getClassroomCount() {
        return classroomCount;
    }

    public void setClassroomCount(int classroomCount) {
        this.classroomCount = classroomCount;
    }


    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    public ArrayList<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(ArrayList<Teacher> teachers) {
        this.teachers = teachers;
    }

    public ArrayList<Classroom> getClassrooms() {
        return classrooms;
    }

    public void setClassrooms(ArrayList<Classroom> classrooms) {
        this.classrooms = classrooms;
    }

    private boolean invalidPosition(Position position) {
        return !timetable[position.getClassroomId()][position.getTimeId()].isNull();
    }

    public String[][] getStringTimetable() {
        String[][] timetableString = new String[classroomCount][Constants.TIMES_COUNT];
        for (int i = 0; i < classroomCount; i++) {
            for (int j = 0; j < Constants.TIMES_COUNT; j++) {
                timetableString[i][j] = timetable[i][j].toString();
            }
        }
        return timetableString;
    }

    @Override
    public String toString() {
        String s = "";
        int geneCount = 0;
        for (int i = 0; i < classroomCount; i++) {
            for (int j = 0; j < Constants.TIMES_COUNT; j++) {
                s += timetable[i][j].toSimpleString() + " ";

                if (!timetable[i][j].isNull()) {
                    geneCount++;
                }
            }
            s += "\n";
        }
        return s + "Genes: " + geneCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCoursesSize() {
        return courses.size();
    }

    private Course getCourse(int id) {
        Course course = null;

        for (Course course1 : courses) {
            if (course1.getId() == id) {
                course = course1;
            }
        }
        return new Course(course);
    }

    private Teacher getTeacher(int id) {
        Teacher teacher = null;
        for (Teacher teacher1 : teachers) {
            if (teacher1.getId() == id) {
                teacher = teacher1;
            }
        }
        return teacher;
    }

}