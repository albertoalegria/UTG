package com.alegria.utg.db;

import com.alegria.utg.data.Data;
import com.alegria.utg.db.dao.DAOCourse;
import com.alegria.utg.db.dao.DAOException;
import com.alegria.utg.db.utils.DBManager;
import com.alegria.utg.model.Course;
import com.alegria.utg.model.Time;
import com.alegria.utg.utilities.Constants;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.alegria.utg.utilities.Constants.DB.Course.*;

/**
 * @author Alberto Alegria
 */
public class CourseDBManager implements DAOCourse {
    private DBManager dbManager;

    public CourseDBManager() {
        dbManager = new DBManager();
    }

    @Override
    public boolean create(Course course) throws DAOException {
        return dbManager.insert(Constants.DB.DATABASE_NAME, TB_NAME, DB_COLS, course.toArray());
    }

    @Override
    public boolean update(Course course) throws DAOException {
        return dbManager.update(TB_NAME, DB_COLS, course.toArray(), "id = " + course.getId());
    }

    @Override
    public boolean delete(int id) throws DAOException {
        return dbManager.delete(TB_NAME, "id = " + id);
    }

    @Override
    public Course retrieve(int id) throws DAOException {
        Course course = null;

        try {
            ResultSet resultSet = dbManager.selectAll(TB_NAME, "id = " + id);

            if (resultSet.next()) {
                course = convert(resultSet);
            } else {
                throw new DAOException("No course found");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return course;
    }

    @Override
    public ArrayList<Course> getAll() throws DAOException {
        ArrayList<Course> courses = new ArrayList<>();
        try {
            ResultSet resultSet = dbManager.selectAll(TB_NAME);

            while (resultSet.next()) {
                courses.add(convert(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    @Override
    public void close() {
        dbManager.closeConnection();
    }

    private Course convert(ResultSet resultSet) throws SQLException {
        int shift = resultSet.getInt(COL_SHIFT); //because of the times stream
        ArrayList<Time> availableTimes = new ArrayList<>(Data.getTimes());
        if (shift == Constants.Shift.MORNING) {
            availableTimes = availableTimes.stream().filter(time -> time.getHour() <= 9).collect(Collectors.toCollection(ArrayList<Time>::new));
        } else {
            availableTimes = availableTimes.stream().filter(time -> time.getHour() >= 6).collect(Collectors.toCollection(ArrayList<Time>::new));
        }

        return new Course.Builder()
                .setId(resultSet.getInt("id"))
                .setName(resultSet.getString(COL_NAME))
                .setGroup(resultSet.getInt(COL_GROUP))
                .setSize(resultSet.getInt(COL_SIZE))
                .setHours(resultSet.getInt(COL_HOURS))
                .setCareer(resultSet.getInt(COL_CAREER))
                .setShift(shift)
                .setType(resultSet.getInt(COL_TYPE))
                .setAvailableTimes(availableTimes)
                //.setAvailableTimes(Data.times.stream().filter(time -> time.getShift() == shift).collect(Collectors.toCollection(ArrayList<Time>::new)))
                .build();
    }
}