package com.alegria.utg.model;

import java.util.ArrayList;

/**
 * @author Alberto Alegria
 */
public class Classroom {
    private int id;
    private String name;
    private int size;
    private int type;
    private ArrayList<Integer> careers;
    private ArrayList<Time> availableTimes;

    public static class Builder {
        private int id;
        private String name;
        private int size;
        private int type;
        private ArrayList<Integer> careers;
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

        public Builder setSize(int size) {
            this.size = size;
            return this;
        }

        public Builder setType(int type) {
            this.type = type;
            return this;
        }

        public Builder setCareers(ArrayList<Integer> careers) {
            this.careers = careers;
            return this;
        }

        public Builder setAvailableTimes(ArrayList<Time> availableTimes) {
            this.availableTimes = availableTimes;
            return this;
        }

        public Classroom build() {
            return new Classroom(this);
        }
    }

    private Classroom(Builder builder) {
        id = builder.id;
        name = builder.name;
        size = builder.size;
        type = builder.type;
        careers = builder.careers;
        availableTimes = builder.availableTimes;
    }

    @Override
    public boolean equals(Object anotherClassroom) {
        Classroom classroom = (Classroom) anotherClassroom;

        return this.id == classroom.id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ArrayList<Integer> getCareers() {
        return careers;
    }

    public void setCareers(ArrayList<Integer> careers) {
        this.careers = careers;
    }

    public ArrayList<Time> getAvailableTimes() {
        return availableTimes;
    }

    public void setAvailableTimes(ArrayList<Time> availableTimes) {
        this.availableTimes = availableTimes;
    }
}
