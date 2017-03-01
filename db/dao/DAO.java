package com.alegria.utg.db.dao;

import java.util.ArrayList;

/**
 * @author Alberto Alegria
 */
public interface DAO<T> {
    boolean create(T t) throws DAOException;
    boolean update(T t) throws DAOException;
    boolean delete(int id) throws DAOException;
    T get(int id) throws DAOException;
    ArrayList<T> getAll() throws DAOException;
    void close() throws DAOException;
}
