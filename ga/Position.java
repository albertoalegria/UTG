package com.alegria.utg.ga;

import com.alegria.utg.model.Classroom;
import com.alegria.utg.model.Time;

/**
 * @author Alberto Alegria
 */
public class Position {
    private Classroom classroom;
    private Time time;

    public Position() {
        classroom = null;
        time = null;
    }

    public Position(Position copy) {
        this.time = copy.time;
        this.classroom = copy.classroom;
    }

    public Position(Classroom classroom, Time time) {
        this.classroom = classroom;
        this.time = time;
    }

    public int getTimeId() {
        return time.getAbsHour() - 1;
    }

    public int getClassroomId() {
        return classroom.getId();
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public boolean isNull() {
        return classroom == null || time == null;
    }

    @Override
    public boolean equals(Object anotherPosition) {
        Position position = (Position) anotherPosition;
        return time.equals(position.time) && classroom.equals(position.classroom);
    }

    @Override
    public String toString() {
        return time.getAbsHour() + ", " + classroom.getId();
    }
}
