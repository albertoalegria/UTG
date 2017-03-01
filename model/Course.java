package com.alegria.utg.model;

import java.util.ArrayList;

/**
 * @author Alberto Alegria
 */
public class Course {
    private int id;
    private String name;
    private int group;
    private int size;
    private int hours;
    private int career;
    private int shift;
    private int type;
    private ArrayList<Time> availableTimes;

    public Course(Course copy) {
        this.id = copy.id;
        this.name = copy.name;
        this.group = copy.group;
        this.size = copy.size;
        this.hours = copy.hours;
        this.career = copy.career;
        this.shift = copy.shift;
        this.type = copy.type;
        this.availableTimes = new ArrayList<>(copy.availableTimes);
    }

    public static class Builder {
        private int id;
        private String name;
        private int group;
        private int size;
        private int hours;
        private int career;
        private int shift;
        private int type;
        private ArrayList<Time> availableTimes;

        public Builder() {
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name){
            this.name = name;
            return this;
        }

        public Builder setGroup(int group) {
            this.group = group;
            return this;
        }

        public Builder setSize(int size) {
            this.size = size;
            return this;
        }

        public Builder setHours(int hours) {
            this.hours = hours;
            return this;
        }

        public Builder setCareer(int career) {
            this.career = career;
            return this;
        }

        public Builder setShift(int shift) {
            this.shift = shift;
            return this;
        }

        public Builder setType(int type) {
            this.type = type;
            return this;
        }

        public Builder setAvailableTimes(ArrayList<Time> availableTimes) {
            this.availableTimes = availableTimes;
            return this;
        }
        public Course build() {
            return new Course(this);
        }
    }

    private Course(Builder builder) {
        id = builder.id;
        name = builder.name;
        group = builder.group;
        size = builder.size;
        hours = builder.hours;
        career = builder.career;
        shift = builder.shift;
        type = builder.type;
        availableTimes = builder.availableTimes;
    }

    public void removeTime(Time time) {
        availableTimes.remove(time);
    }

    public String[] toArray() {
        return new String[] {
                name,
                String.valueOf(size),
                String.valueOf(hours),
                String.valueOf(career),
                String.valueOf(shift),
                String.valueOf(type)};
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getCareer() {
        return career;
    }

    public void setCareer(int career) {
        this.career = career;
    }

    public int getShift() {
        return shift;
    }

    public void setShift(int shift) {
        this.shift = shift;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Time> getAvailableTimes() {
        return availableTimes;
    }

    public void setAvailableTimes(ArrayList<Time> availableTimes) {
        this.availableTimes = availableTimes;
    }

    public void setTimes(ArrayList<Time> availableTimes) {
        this.availableTimes = availableTimes;
    }



    @Override
    public boolean equals(Object course) {
        Course anotherCourse = (Course) course;
        return id == anotherCourse.getId() && name.equals(anotherCourse.getName());
    }

    @Override
    public String toString() {
        return "Course[Id: " + id + ", Name: " + name + ", Size: " + size +
                ",\nHours: " + hours + ", Career: " + career + ", Shift: " + shift;
    }
}
