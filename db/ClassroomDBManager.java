package com.alegria.utg.db;

import com.alegria.utg.db.dao.DAOClassroom;
import com.alegria.utg.model.Classroom;

import java.util.ArrayList;

/**
 * @author Alberto Alegria
 */
public class ClassroomDBManager implements DAOClassroom {
    @Override
    public boolean create(Classroom classroom) {

        return false;
    }

    @Override
    public boolean update(Classroom classroom) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public Classroom get(int id) {
        return null;
    }

    @Override
    public ArrayList<Classroom> getAll() {
        return new ArrayList<>();
    }

    @Override
    public void close() {
    }
}
