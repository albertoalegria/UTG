package com.alegria.utg.data;

import com.alegria.utg.db.CourseDBManager;
import com.alegria.utg.db.TeacherDBManager;
import com.alegria.utg.db.dao.DAOException;
import com.alegria.utg.model.Classroom;
import com.alegria.utg.model.Course;
import com.alegria.utg.model.Teacher;
import com.alegria.utg.model.Time;
import com.alegria.utg.utilities.Constants;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * <h1>Data</h1>
 *
 * This class consists exclusively of static methods that either request objects from the database
 * through the
 *
 * @author Alberto Alegria
 * @version 1.3
 * @since 2017-01-30
 */
public class Data {
    private static ArrayList<Teacher> teachers;
    private static ArrayList<Course> courses;
    private static ArrayList<Classroom> classrooms;
    private static ArrayList<Time> times;


    public static void get() {
        teachers = new ArrayList<>();
        courses = new ArrayList<>();
        classrooms = new ArrayList<>();
        times = new ArrayList<>();

        getDBTimes();
        getDBCourses();
        getDBTeachers();
        getDBClassrooms();
    }

    public static void close() {
        teachers = null;
        courses = null;
        classrooms = null;
        times = null;
    }


    public static Course getCourse(int id) {
        Course course = null;

        for (Course course1 : courses) {
            if (course1.getId() == id) {
                course = course1;
            }
        }
        return new Course(course);
    }

    public static Teacher getTeacher(int id) {
        Teacher teacher = null;
        for (Teacher teacher1 : teachers) {
            if (teacher1.getId() == id) {
                teacher = teacher1;
            }
        }
        return new Teacher(teacher);
    }

    public static ArrayList<Course> getCourses() {
        return new ArrayList<>(courses);
    }

    public static ArrayList<Teacher> getTeachers() {
        return new ArrayList<>(teachers);
    }

    public static ArrayList<Classroom> getClassrooms() {
        return new ArrayList<>(classrooms);
    }

    public static ArrayList<Time> getTimes() {
        return new ArrayList<>(times);
    }

    public static int getCoursesSize() {
        return courses.size();
    }

    public static int getTeachersSize() {
        return teachers.size();
    }

    public static int getClassroomsSize() {
        return classrooms.size();
    }

    public static Classroom getClassroom(int id) {
        Classroom classroom = null;
        for (Classroom classroom1 : classrooms) {
            if (classroom1.getId() == id) {
                classroom = classroom1;
            }
        }
        return classroom;
    }

    public static Time getTime(int id) {
        Time time = null;
        for (Time time1 : times) {
            if (time1.getAbsHour() - 1 == id) {
                time = time1;
            }
        }
        return time;
    }

    private static void getDBCourses() {
        CourseDBManager dbManager = new CourseDBManager();
        try {
            courses = dbManager.getAll();
        } catch (DAOException e) {
            e.printStackTrace();
        }
        dbManager.close();
    }

    private static void getDBTimes() {
        for (int i = 0; i < 70; i++) {
            times.add(new Time(i + 1));
        }
    }

    private static void getDBTeachers() {
        TeacherDBManager dbManager = new TeacherDBManager();
        try {
            teachers = dbManager.getAll();
        } catch (DAOException e) {
            e.printStackTrace();
        }
        dbManager.close();
    }

    private static void getDBClassrooms() {
        for (int i = 0; i < 21; i++) {
            ArrayList<Time> times1 = new ArrayList<>(times);
            //System.out.println("Times size: " + times1.size());
            ArrayList<Integer> careers = new ArrayList<>();
            for (int j = 1; j <= 9; j++) {
                careers.add(j);
            }

            int type = Constants.Type.CLASSROOM;

            if (i > 17) {
                type = Constants.Type.LABORATORY;
            }

            classrooms.add(new Classroom.Builder()
                    .setId(i)
                    .setName("Classroom " + i + 1)
                    .setSize(40)
                    .setType(type)
                    .setCareers(careers)
                    .setAvailableTimes(times1)
                    .build());
        }
    }


    public static ArrayList<String> getCoursesNames() {
        return courses.stream().map(Course::getName).collect(Collectors.toCollection(ArrayList<String>::new));
    }

    public static Course getCourseFromString(String courseName) {
        Course course = null;
        for (Course course1 : courses) {
            if (course1.getName().equals(courseName)) {
                course = course1;
            }
        }
        return course;
    }


}
