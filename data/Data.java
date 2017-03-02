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
 * This class consists exclusively of static methods that retrieve
 * necessary information for the timetable generation process.
 *
 * <p>In order to avoid <tt>NullPointerException</tt> using this methods first you need to call {@link #get()}.
 * This is going to initialize the objects arrays retrieving their information from the database using
 * the database helpers {@link com.alegria.utg.db.CourseDBManager}, {@link com.alegria.utg.db.TeacherDBManager} and
 * {@link com.alegria.utg.db.ClassroomDBManager}.
 *
 * @author Alberto Alegria
 * @version 1.3
 * @since 2017-01-30
 */
public class Data {

    /*
    * These static variables holds the information that's retrieved from the database.
    * */
    private static ArrayList<Teacher> teachers;
    private static ArrayList<Course> courses;
    private static ArrayList<Classroom> classrooms;
    private static ArrayList<Time> times;

    /**
     * Initialize the variables that will hold the information and then fill them with the information retrieved from the
     * database.
     * */
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

    /**
     * Nullify all the objects, in order to avoid that all the individuals hold the same information.
     * */

    public static void close() {
        teachers = null;
        courses = null;
        classrooms = null;
        times = null;
    }

    /**
     * Find the specified course inside the course array.
     *
     * @param id identifier of the desired course.
     * @return the course that matches the id param
     * */
    public static Course getCourse(int id) {
        Course course = null;

        for (Course course1 : courses) {
            if (course1.getId() == id) {
                course = course1;
            }
        }
        return new Course(course);
    }

    /**
     * Find the specified teacher inside the teacher array.
     *
     * @param id identifier of the desired teacher.
     * @return the teacher that matches the id param
     * */
    public static Teacher getTeacher(int id) {
        Teacher teacher = null;
        for (Teacher teacher1 : teachers) {
            if (teacher1.getId() == id) {
                teacher = teacher1;
            }
        }
        return new Teacher(teacher);
    }

    /**
     * Find the specified classroom inside the classroom array.
     *
     * @param id identifier of the desired classroom.
     * @return the classroom that matches the id param
     * */
    public static Classroom getClassroom(int id) {
        Classroom classroom = null;
        for (Classroom classroom1 : classrooms) {
            if (classroom1.getId() == id) {
                classroom = classroom1;
            }
        }
        return classroom;
    }

    /**
     * Find the specified time inside the time array.
     *
     * @param id identifier of the desired time.
     * @return the time that matches the id param
     * */
    public static Time getTime(int id) {
        Time time = null;
        for (Time time1 : times) {
            if (time1.getAbsHour() - 1 == id) {
                time = time1;
            }
        }
        return time;
    }

    /**
     * Retrieves all the courses inside the database using {@link CourseDBManager#getAll()}, and store them in
     * the courses array.
     * */
    private static void getDBCourses() {
        CourseDBManager dbManager = new CourseDBManager();
        try {
            courses = dbManager.getAll();
        } catch (DAOException e) {
            e.printStackTrace();
        }
        dbManager.close();
    }

    /**
     * Creates 70 time objects and stores them in the times array.
     * */
    private static void getDBTimes() {
        for (int i = 0; i < 70; i++) {
            times.add(new Time(i + 1));
        }
    }

    /**
     * Retrieves all the teachers inside the database using {@link TeacherDBManager#getAll()}, and store them in
     * the teachers array.
     * */
    private static void getDBTeachers() {
        TeacherDBManager dbManager = new TeacherDBManager();
        try {
            teachers = dbManager.getAll();
        } catch (DAOException e) {
            e.printStackTrace();
        }
        dbManager.close();
    }

    /**
     * Creates 21 time objects and stores them in the times array. 21 is the number of available classrooms.
     * The first 18 classrooms are standard ones, while the last three classrooms represents the laboratories.
     * */
    private static void getDBClassrooms() {
        for (int i = 0; i < 21; i++) {
            ArrayList<Time> times1 = new ArrayList<>(times);
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

    /**
     * @return String ArrayList with the names of all the courses.
     * */
    public static ArrayList<String> getCoursesNames() {
        return courses.stream().map(Course::getName).collect(Collectors.toCollection(ArrayList<String>::new));
    }

    /**
     * Find the specified cours inside the course array.
     *
     * @param courseName the name of the desired course.
     * @return the course that matches with the name.
     * */
    public static Course getCourseFromString(String courseName) {
        Course course = null;
        for (Course course1 : courses) {
            if (course1.getName().equals(courseName)) {
                course = course1;
            }
        }
        return course;
    }

    /*
    * Getters & Setters
    * */
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
}
