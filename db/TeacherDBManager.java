package com.alegria.utg.db;

import com.alegria.utg.db.dao.DAOException;
import com.alegria.utg.db.dao.DAOTeacher;
import com.alegria.utg.db.utils.DBManager;
import com.alegria.utg.model.Teacher;
import com.alegria.utg.model.Time;
import com.alegria.utg.utilities.Constants;
import com.alegria.utg.utilities.Methods;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Alberto Alegria
 */
public class TeacherDBManager implements DAOTeacher {
    private DBManager dbManager;

    public TeacherDBManager() {
        dbManager = new DBManager();
    }

    @Override
    public boolean create(Teacher teacher) throws DAOException {
        return dbManager.insert(Constants.DB.DATABASE_NAME, Constants.DB.Teacher.TB_NAME, Constants.DB.Teacher.DB_COLS, teacher.toArray());
    }

    @Override
    public boolean update(Teacher teacher) throws DAOException  {
        return dbManager.update(Constants.DB.Teacher.TB_NAME, Constants.DB.Teacher.DB_COLS, teacher.toArray(), "id = " + teacher.getId());
    }

    @Override
    public boolean delete(int id) throws DAOException  {
        return dbManager.delete(Constants.DB.Teacher.TB_NAME, "id = " + id);
    }

    @Override
    public Teacher retrieve(int id) throws DAOException {
        Teacher teacher = null;

        try {
            ResultSet resultSet = dbManager.selectAll(Constants.DB.Teacher.TB_NAME, "id = " + id);

            if (resultSet.next()) {
                teacher = convert(resultSet);
            } else {
                throw new DAOException("No teacher found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teacher;
    }

    @Override
    public ArrayList<Teacher> getAll() throws DAOException  {
        ArrayList<Teacher> teachers = new ArrayList<>();

        try {
            ResultSet resultSet = dbManager.selectAll(Constants.DB.Teacher.TB_NAME);

            while (resultSet.next()) {
                teachers.add(convert(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teachers;
    }

    @Override
    public void close() {
        dbManager.closeConnection();
    }

    private Teacher convert(ResultSet resultSet) throws SQLException {
        return new Teacher.Builder()
                .setId(resultSet.getInt("id"))
                .setName(resultSet.getString(Constants.DB.Teacher.COL_NAME))
                .setTotalHours(resultSet.getInt(Constants.DB.Teacher.COL_HOURS))
                .setFirstTime(new Time(resultSet.getInt(Constants.DB.Teacher.COL_FTIME)))
                .setLastTime(new Time(resultSet.getInt(Constants.DB.Teacher.COL_LTIME)))
                .setPossibleCourses(Methods.stringToCourseArray(resultSet.getString(Constants.DB.Teacher.COL_COURSES)))
                .setAvailableTimes(Methods.stringToTimeArray(resultSet.getString(Constants.DB.Teacher.COL_TIMES)))
                .build();

    }
}

/*try {
            ResultSet resultSet = dbManager.selectAll(TB_NAME, "id = " + id);
            int teacherId = -1;
            String name = "null";
            int hours = -1;
            String possibleCourses = "null";
            String availableTimes = "null";

            while (resultSet.next()) {
                teacherId = resultSet.getInt("id");
                name = resultSet.getString(COL_NAME);
                hours = resultSet.getInt(COL_HOURS);
                possibleCourses = resultSet.getString(COL_COURSES);
                availableTimes = resultSet.getString(COL_TIMES);
            }

            return new Teacher.Builder().setId(teacherId)
                    .setName(name)
                    .setTotalHours(hours)
                    .setPossibleCourses(Methods.stringToCourseArray(possibleCourses))
                    .setAvailableTimes(Methods.stringToTimeArray(availableTimes))
                    .build();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }*/
