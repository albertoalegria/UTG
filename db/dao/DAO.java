package com.alegria.utg.db.dao;

import java.util.ArrayList;

/**
 * This interface allows the creation of another generic DAO implementing the basics CRUD operations.
 *
 *
 * @author Alberto Alegria
 * @version 1.3
 * @since 2017-01-30
 *
 * @param <T> the type of element of this interface.
 */
public interface DAO<T> {

    /**
     * Creates the passed object in the database.
     *
     * @param t Object to be created.
     * @return True if the operation was successfully, otherwise False.
     * @throws DAOException
     * */
    boolean create(T t) throws DAOException;

    /**
     * Retrieves an object directly from the database.
     *
     * @param id identifier of the object to be retrieved.
     * @return the retrieved object.
     * @throws DAOException
     * */
    T retrieve(int id) throws DAOException;

    /**
     * Updates an object from the database.
     *
     * @param t the new object.
     * @return True if the operation was successfully, otherwise False.
     * @throws DAOException
     * */
    boolean update(T t) throws DAOException;

    /**
     * Deletes an object from the database.
     *
     * @param id identifier of the object to be deleted
     * @return True if the operation was successfully, otherwise False.
     * @throws DAOException
     * */
    boolean delete(int id) throws DAOException;

    /**
     * Retrieves all the objects stored inside the database.
     *
     * @return An array containing all the objects of the table.
     * @throws DAOException
     * */
    ArrayList<T> getAll() throws DAOException;

    /**
     * Close the database connection.
     *
     * @throws DAOException
     * */
    void close() throws DAOException;
}
