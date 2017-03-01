package com.alegria.utg.ga;

import com.alegria.utg.model.Course;
import com.alegria.utg.model.Teacher;

/**
 * @author Alberto Alegria
 */
public class Gene {
    //private int courseId;
    //private int teacherId;
    private Teacher teacher;
    private Course course;
    private Position position;

    public Gene (Gene copy) {
        this.course = copy.course; //new Course(copy.course);
        this.teacher = copy.teacher; //new Teacher(copy.teacher);
        //this.position = new Position(copy.position);
    }

    public Gene() {
        teacher = null;
        course = null;
        position = new Position();
    }

    public Gene(Course course, Teacher teacher) {
        this.course = course;
        this.teacher = teacher;
    }

    public static class Builder {
        private Course course;
        private Teacher teacher;
        private Position position;

        public Builder() {}

        public Builder setCourse(Course course) {
            this.course = course;
            return this;
        }

        public Builder setTeacher(Teacher teacher) {
            this.teacher = teacher;
            return this;
        }

        public Builder setPosition(Position position) {
            this.position = position;
            return this;
        }

        public Gene build() {
            return new Gene(this);
        }

    }

    private Gene(Builder builder) {
        course = builder.course;
        teacher = builder.teacher;

        position = builder.position;
    }

    public boolean isNull() {
        return course == null && teacher == null;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Course getCourse() {
        return course;
    }

    public int getTeacherId() {
        return teacher.getId();
    }

    public int getCourseId() {
        return course.getId();
    }


    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String toSimpleString() {
        if (course == null || teacher == null) {
            return "=,=";
        } else {
            String courseId = String.valueOf(course.getId());
            String teacherId = String.valueOf(teacher.getId());

            if (course.getId() < 10) {
                courseId = "0" + courseId;
            }

            if (teacher.getId() < 10) {
                teacherId = "0" + teacherId;
            }
            return courseId + "," + teacherId;
        }
    }


    @Override
    public String toString() {
        if (course == null || teacher == null) {
            return "= , =";
        } else {
            return course.getName() + ", " + teacher.getName();
        }
    }

    public boolean similarTo(Gene gene) {
        return this.position.equals(gene.position);
    }

    @Override
    public boolean equals(Object anotherGene) {
        if (anotherGene == null || !(anotherGene instanceof Gene)) {
            return false;
        }
        Gene gene = (Gene) anotherGene;
        return course.equals(gene.course) && teacher.equals(gene.teacher);// && position.equals(gene.position);
    }
}