package com.alegria.utg.db.dao;

/**
 * Throws exceptions based whether if any of the CRUD operations defined in {@link DAO} where successful or not.
 * @author Alberto Alegria
 */
public class DAOException extends Exception {
    //TODO document these methods.
    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }

    public DAOException(Throwable cause) {
        super(cause);
    }
}
