package com.alegria.utg.model;

import com.alegria.utg.utilities.Methods;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author Alberto Alegria
 */
public class Teacher {

    private int id;
    private String name;
    private int totalHours;
    private int leftHours;
    private int timesSize;
    private Time firstTime;
    private Time lastTime;
    private ArrayList<Time> preferredTimes;
    private ArrayList<Course> givenCourses;
    private ArrayList<Course> possibleCourses;
    private ArrayList<Time> availableTimes;
    private ArrayList<Time> unavailableTimes;

    //TODO create real available times

    public Teacher(Teacher copy) {
        this.id = copy.id;
        this.name = copy.name;
        this.totalHours = copy.totalHours;
        this.leftHours = copy.leftHours;
        this.firstTime = copy.firstTime;
        this.lastTime = copy.lastTime;
        this.preferredTimes = copy.preferredTimes;
        this.givenCourses = new ArrayList<>(copy.givenCourses);
        this.possibleCourses = new ArrayList<>(copy.possibleCourses);
        this.availableTimes = new ArrayList<>(copy.availableTimes);
        this.unavailableTimes = new ArrayList<>(copy.unavailableTimes);
        timesSize = preferredTimes.size();
    }

    public static class Builder {
        private int id;
        private String name;
        private int totalHours;
        private Time firstTime;
        private Time lastTime;
        private ArrayList<Course> possibleCourses;
        private ArrayList<Time> availableTimes;

        public Builder() {}

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setTotalHours(int totalHours) {
            this.totalHours = totalHours;
            return this;
        }

        public Builder setFirstTime(Time firstTime) {
            this.firstTime = firstTime;
            return this;
        }

        public Builder setLastTime(Time lastTime) {
            this.lastTime = lastTime;
            return this;
        }

        public Builder setPossibleCourses(ArrayList<Course> possibleCourses) {
            this.possibleCourses = possibleCourses;
            return this;
        }

        public Builder setAvailableTimes(ArrayList<Time> availableTimes) {
            this.availableTimes = availableTimes;
            return this;
        }

        public Teacher build() {
            return new Teacher(this);
        }
    }

    private Teacher(Builder builder) {
        id = builder.id;
        name = builder.name;
        totalHours = builder.totalHours;
        leftHours = totalHours;
        possibleCourses = builder.possibleCourses;
        availableTimes = builder.availableTimes;

        firstTime = builder.firstTime;
        lastTime = builder.lastTime;
        preferredTimes = new ArrayList<>();
        createPreferredTimes();

        unavailableTimes = new ArrayList<>();
        givenCourses = new ArrayList<>();
        timesSize = preferredTimes.size();
    }

    private void createPreferredTimes() {
        ArrayList<Time> times = new ArrayList<>();
        for (int i = 0; i < 70; i++) {
            times.add(new Time(i + 1));
        }

        preferredTimes = times.stream().filter(time -> time.getHour() >= firstTime.getHour() && time.getHour() <= lastTime.getHour()).collect(Collectors.toCollection(ArrayList<Time>::new));
    }

    public void addCourse(Course course) {
        givenCourses.add(course);
        leftHours -= course.getHours();
    }

    public void removeTime(Time time) {
        preferredTimes.remove(time);
        availableTimes.remove(time);
        timesSize--;
        unavailableTimes.add(time);
    }

    public void addTime(Time time) {
        preferredTimes.add(time);
        availableTimes.add(time);
        timesSize++;
        unavailableTimes.remove(time);
    }

    @Override
    public boolean equals(Object anotherTeacher) {
        if (anotherTeacher instanceof Teacher) {
            Teacher teacher = (Teacher) anotherTeacher;
            return id == teacher.getId() && name.equals(teacher.getName());
        } else {
            return false;
        }
    }

    public String[] toArray() {
        return new String[] {
                name,
                String.valueOf(totalHours),
                Methods.coursesToString(possibleCourses),
                Methods.timesToString(availableTimes)};
    }

    /*
    *
    * private static String[] teacherToArray(Teacher teacher) {
            return new String[] {
                    teacher.getName(),
                    String.valueOf(teacher.getTotalHours()),
                    Methods.coursesToString(teacher.getPossibleCourses()),
                    Methods.timesToString(teacher.getAvailableTimes())};
        }
    * */

    /*public String[] toArray(boolean id) {
        String[] fields = new String[];

        if (id) {

        }

        return fields;
    }*/

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(int totalHours) {
        this.totalHours = totalHours;
    }

    public int getLeftHours() {
        return leftHours;
    }

    public void setLeftHours(int leftHours) {
        this.leftHours = leftHours;
    }

    public ArrayList<Course> getGivenCourses() {
        return givenCourses;
    }

    public void setGivenCourses(ArrayList<Course> givenCourses) {
        this.givenCourses = givenCourses;
    }

    public ArrayList<Course> getPossibleCourses() {
        return possibleCourses;
    }

    public void setPossibleCourses(ArrayList<Course> possibleCourses) {
        this.possibleCourses = possibleCourses;
    }

    public ArrayList<Time> getAvailableTimes() {
        return availableTimes;
    }

    public void setAvailableTimes(ArrayList<Time> availableTimes) {
        this.availableTimes = availableTimes;
    }

    public Time getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Time firstTime) {
        this.firstTime = firstTime;
    }

    public Time getLastTime() {
        return lastTime;
    }

    public void setLastTime(Time lastTime) {
        this.lastTime = lastTime;
    }

    public ArrayList<Time> getPreferredTimes() {
        return preferredTimes;
    }

    public void setPreferredTimes(ArrayList<Time> preferredTimes) {
        this.preferredTimes = preferredTimes;
    }

    public ArrayList<Time> getUnavailableTimes() {
        return unavailableTimes;
    }

    public void setUnavailableTimes(ArrayList<Time> unavailableTimes) {
        this.unavailableTimes = unavailableTimes;
    }

    public int getTimesSize() {
        return timesSize;
    }

    public void setTimesSize(int timesSize) {
        this.timesSize = timesSize;
    }


    @Override
    public String toString() {
        return "Teacher[id: " + id + ", Name: " + name + ", Hours Left: " + leftHours + ",\nGiven Courses: " + givenCourses.toString() +
                ",\nPossible Courses: " + possibleCourses.toString() + ",\nAvailable Times: " + availableTimes.toString() + "]";
    }
    // Gene timetable [times] [classrooms]
}
