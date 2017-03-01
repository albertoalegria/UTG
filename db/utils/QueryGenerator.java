package com.alegria.utg.db.utils;

/**
 * @author Alberto Alegria
 */
public class QueryGenerator {
    public static String insert(String databaseName, String tableName, String[] tableFields) {
        return "INSERT INTO " + databaseName + "." + tableName + getFields(tableFields) + getValues(tableFields.length);
    }

    public static String update(String tableName, String[] columnNames, String whereClause) {

        String query = "UPDATE " + tableName + " SET ";

        for (String columnName : columnNames) {
            query += columnName + "= ?, ";
        }

        query = query.substring(0, query.length() - 2) + " WHERE " + whereClause;

        return query;
    }

    public static String delete(String tableName, String whereClause) {
        return "DELETE FROM " + tableName + " WHERE " + whereClause;
    }

    public static String select(String[] columnNames, String tableName, boolean useWhere, String whereClause) {
        String query = "SELECT ";

        for (String columnName : columnNames) {
            query += columnName + ", ";
        }

        query = query.substring(0, query.length() - 2) + " FROM " + tableName;

        if (useWhere) {
            query += " WHERE " + whereClause;
        }

        return query;
    }

    public static String selectAll(String tableName, String whereClause) {
        /*String query = "SELECT * FROM " + tableName;

        if (useWhere) {
            query += " WHERE " + whereClause;
        }*/

        return "SELECT * FROM " + tableName + " WHERE " + whereClause;
    }

    public static String selectAll(String tableName) {
        return "SELECT * FROM " + tableName;
    }

    private static String getFields(String... fields) {
        String values = "";
        for (String field : fields) {
            values += field + ", ";
        }

        return " (" + values.substring(0, values.length() - 2) + ") ";
    }

    private static String getValues(int fieldsCount) {
        String values = "";

        for (int i = 0; i < fieldsCount; i++) {
            values += "?, ";
        }

        return "VALUES (" + values.substring(0, values.length() - 2) + ")";
    }
}
