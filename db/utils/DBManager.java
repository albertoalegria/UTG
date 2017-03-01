package com.alegria.utg.db.utils;

import com.alegria.utg.utilities.Constants;

import java.sql.*;

/**
 * @author Alberto Alegria
 */
public class DBManager {
    private Connection sqlConnection;
    private PreparedStatement sqlPreparedStatement;
    private ResultSet sqlResultSet;

    public DBManager() {
        connect();
    }

    private void connect() {
        try {

            Class.forName(Constants.DB.CLASS_NAME);
            sqlConnection = DriverManager.getConnection(Constants.DB.DATABASE_URL);
            //System.out.println("Connected to DB");


        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean insert(String databaseName, String tableName, String[] fields, String[] values){
        try {
            String query = QueryGenerator.insert(databaseName, tableName, fields);
            System.out.println(query);
            sqlPreparedStatement = sqlConnection.prepareStatement(query);

            for (int i = 0; i < fields.length; i++) {
                sqlPreparedStatement.setObject(i + 1, values[i]);
            }

            return sqlPreparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(String tableName, String[] columnNames, String[] newValues, String whereClause) {
        if (columnNames.length != newValues.length) {
            throw new IllegalArgumentException();
        }

        try {
            sqlPreparedStatement = sqlConnection.prepareStatement(QueryGenerator.update(tableName, columnNames, whereClause));

            for (int i = 0; i < newValues.length; i++) {
                sqlPreparedStatement.setObject(i + 1, newValues[i]);
            }

            return sqlPreparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String tableName, String whereClause) {
        try {
            return sqlPreparedStatement.execute(QueryGenerator.delete(tableName, whereClause));
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet select(String[] columnNames, String tableName, boolean useWhere, String whereClause) {
        try {
            sqlConnection.prepareStatement(QueryGenerator.select(columnNames, tableName, useWhere, whereClause));
            return sqlPreparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet selectAll(String tableName, String whereClause) {
        try {
            sqlPreparedStatement = sqlConnection.prepareStatement(QueryGenerator.selectAll(tableName, whereClause));
            return sqlPreparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet selectAll(String tableName) {
        try {
            sqlPreparedStatement = sqlConnection.prepareStatement(QueryGenerator.selectAll(tableName));
            return sqlPreparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void closeConnection() {
        try {
            if (sqlConnection != null) {
                sqlConnection.close();
            }

            if (sqlPreparedStatement != null) {
                sqlPreparedStatement.close();
            }

            if (sqlResultSet != null) {
                sqlResultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
