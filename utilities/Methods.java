package com.alegria.utg.utilities;

import com.alegria.utg.data.Data;
import com.alegria.utg.model.Course;
import com.alegria.utg.model.Teacher;
import com.alegria.utg.model.Time;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Alberto Alegria
 */
public class Methods {
    public static int getRandomNumber(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    public static double getRandomDouble(double min, double max) {
        return min + (max - min) * new Random().nextDouble();
    }

    public static double map(double value, double relStart, double relEnd, double absStart, double absEnd) {
        return absStart + (absEnd - absStart) * ((value - relStart) / (relEnd - relStart));
    }

    public static ArrayList<Course> stringToCourseArray(String strCourse) {
        ArrayList<String> strCourses = stringToArray(strCourse);
        ArrayList<Course> courses = new ArrayList<>();

        for (String string : strCourses) {
            courses.addAll(Data.getCourses().stream().filter(course -> course.getName().equals(string)).collect(Collectors.toList()));
        }

        return courses;
    }

    public static ArrayList<Time> stringToTimeArray(String string) {
        ArrayList<String> strings = stringToArray(string);

        return strings.stream().map(s -> new Time(Integer.valueOf(s))).collect(Collectors.toCollection(ArrayList::new));
    }

    public static ArrayList<String> stringToArray(String string) {
        ArrayList<String> array = new ArrayList<>();
        int flag = 0;

        for (int i = 0; i < string.length(); i++) {
            char buffer = string.charAt(i);

            if (buffer == ',') {
                array.add(buildSubString(string, flag, i));
                flag = i;
            }

            if (i == string.length() - 1) {
                array.add(buildSubString(string, flag, i + 1));
            }
        }
        return array;
    }

    public static String coursesToString(ArrayList<Course> courses) {
        String string = "";

        for (Course course : courses) {
            string += course.getName() + ", ";
        }

        return string.substring(0, string.length() - 2);
    }

    public static String teachersToString(ArrayList<Teacher> teachers) {
        String string = "";

        for (Teacher teacher : teachers) {
            string += teacher.getName() + ", ";
        }

        return string.substring(0, string.length() - 2);
    }

    public static String timesToString(ArrayList<Time> times) {
        String string = "";

        for (Time time : times) {
            string += time.getAbsHour() + ", ";
        }

        return string.substring(0, string.length() - 2);
    }

    public static void exportData(String info, String path) throws IOException {
        Path targetPath = Paths.get(path);
        byte[] bytes = info.getBytes(StandardCharsets.UTF_8);
        Files.write(targetPath, bytes, StandardOpenOption.CREATE);
    }

    private static String buildSubString(String content, int begin, int end) {
        return content.substring(begin, end).replace(", ", "");
    }
}
