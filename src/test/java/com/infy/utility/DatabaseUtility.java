package com.infy.utility;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;

public class DatabaseUtility {

	/**
     * Establishes a connection to the database.
     * 
     * This method loads the MySQL JDBC driver and establishes a connection to the
     * database using the provided connection string, username, and password.
     * 
     * @param connectionString the connection string to the database
     * @param username the database username
     * @param password the database password
     * @return a {@code Connection} object representing the database connection, or
     *         {@code null} if the connection fails
     */
    public static Connection getConnection(String connectionString, String username, String password) {
        Connection conn = null;
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            // Establish the connection
            conn = DriverManager.getConnection(connectionString, username, password);
            System.out.println("Connection established successfully.");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
    
    /**
     * Executes a given SQL query on the database and prints the results.
     * 
     * This method establishes a connection to the database using provided parameters,
     * creates a statement, and executes the provided SQL query. It then prints the
     * specified columns from the result set.
     * 
     * @param connectionString the connection string to the database
     * @param username the database username
     * @param password the database password
     * @param query the SQL query to execute
     * @param columns the columns to retrieve and print from the result set
     * @throws SQLException if a database access error occurs
     */
    public static void executeQuery(String connectionString, String username, String password, String query, String[] columns) throws SQLException {
        Connection conn = DatabaseUtility.getConnection(connectionString, username, password);
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                for (String column : columns) {
                    System.out.println(column + ": " + rs.getString(column));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DatabaseUtility.closeConnection(conn);
        }
    }
    
    /**
     * Validates data in the specified table in the database.
     * 
     * This method establishes a connection to the database using provided parameters,
     * creates a statement, and executes an SQL SELECT statement to retrieve data from 
     * the specified table. It then compares the actual value from the database with the 
     * expected value and asserts whether they match.
     * 
     * @param connectionString the connection string to the database
     * @param username the database username
     * @param password the database password
     * @param tableName the name of the table to validate data from
     * @param whereClause the WHERE clause to filter the data
     * @param columnName the column name to retrieve data from
     * @param expectedValue the expected value to compare with the actual value
     * @throws SQLException if a database access error occurs
     */
    public static void validateData(String connectionString, String username, String password, String tableName, String whereClause, String columnName, String expectedValue) throws SQLException {
        Connection conn = DatabaseUtility.getConnection(connectionString, username, password);
        try {
            Statement stmt = conn.createStatement();
            String query = String.format("SELECT %s FROM %s WHERE %s", columnName, tableName, whereClause);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String actualValue = rs.getString(columnName);
                Assert.assertEquals(actualValue, expectedValue, "Data does not match!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DatabaseUtility.closeConnection(conn);
        }
    }
    
    /**
     * Creates a record in the specified table in the database.
     * 
     * This method establishes a connection to the database using provided parameters,
     * creates a statement, and executes an SQL INSERT statement to add a record to the
     * specified table.
     * 
     * @param connectionString the connection string to the database
     * @param username the database username
     * @param password the database password
     * @param tableName the name of the table to insert into
     * @param columns the columns to insert values into, formatted as "(column1, column2, ...)"
     * @param values the values to insert into the columns, formatted as "('value1', 'value2', ...)"
     * @throws SQLException if a database access error occurs
     */
    public static void createRecord(String connectionString, String username, String password, String tableName, String columns, String values) throws SQLException {
        Connection conn = DatabaseUtility.getConnection(connectionString, username, password);
        try {
            Statement stmt = conn.createStatement();
            String query = String.format("INSERT INTO %s %s VALUES %s", tableName, columns, values);
            stmt.executeUpdate(query);
            System.out.println("Record created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DatabaseUtility.closeConnection(conn);
        }
    }


    /**
     * Updates a record in the specified table in the database.
     * 
     * This method establishes a connection to the database using provided parameters,
     * creates a statement, and executes an SQL UPDATE statement to modify a record 
     * in the specified table where the given condition is met.
     * 
     * @param connectionString the connection string to the database
     * @param username the database username
     * @param password the database password
     * @param tableName the name of the table to update
     * @param setColumn the column to update
     * @param setValue the new value to set
     * @param whereColumn the column for the WHERE clause
     * @param whereValue the value for the WHERE clause
     * @throws SQLException if a database access error occurs
     */
    public static void updateRecord(String connectionString, String username, String password, String tableName, String setColumn, String setValue, String whereColumn, String whereValue) throws SQLException {
        Connection conn = DatabaseUtility.getConnection(connectionString, username, password);
        try {
            Statement stmt = conn.createStatement();
            String query = String.format("UPDATE %s SET %s='%s' WHERE %s='%s'", tableName, setColumn, setValue, whereColumn, whereValue);
            stmt.executeUpdate(query);
            System.out.println("Record updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DatabaseUtility.closeConnection(conn);
        }
    }
    
    /**
     * Deletes a record from the specified table in the database.
     * 
     * This method establishes a connection to the database using provided parameters,
     * creates a statement, and executes an SQL DELETE statement to remove a record from 
     * the specified table where the given condition is met.
     * 
     * @param connectionString the connection string to the database
     * @param username the database username
     * @param password the database password
     * @param tableName the name of the table to delete from
     * @param whereColumn the column for the WHERE clause
     * @param whereValue the value for the WHERE clause
     * @throws SQLException if a database access error occurs
     */
    public static void deleteRecord(String connectionString, String username, String password, String tableName, String whereColumn, String whereValue) throws SQLException {
        Connection conn = DatabaseUtility.getConnection(connectionString, username, password);
        try {
            Statement stmt = conn.createStatement();
            String query = String.format("DELETE FROM %s WHERE %s='%s'", tableName, whereColumn, whereValue);
            stmt.executeUpdate(query);
            System.out.println("Record deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DatabaseUtility.closeConnection(conn);
        }
    }
    
    /**
     * Compares the data between two maps and prints the differences.
     * 
     * This method takes two maps as input, each representing a set of data from two
     * different databases. It compares the key-value pairs between the two maps and
     * prints out any differences, such as missing keys or mismatched values.
     * 
     * @param dataMap1 the {@code Map} containing data from the first database. The
     *                 keys are data identifiers, and the values are the data values.
     * @param dataMap2 the {@code Map} containing data from the second database. The
     *                 keys are data identifiers, and the values are the data values.
     */
    public static Map<String, String> getDataFromTable(Connection conn, String tableName) {
        Map<String, String> dataMap = new HashMap<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
            while (rs.next()) {
                String id = rs.getString("id");
                String value = rs.getString("value");
                dataMap.put(id, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataMap;
    }
    
    public static void compareData(Map<String, String> dataMap1, Map<String, String> dataMap2) {
        for (Map.Entry<String, String> entry : dataMap1.entrySet()) {
            String key = entry.getKey();
            String value1 = entry.getValue();
            String value2 = dataMap2.get(key);

            if (value2 == null) {
                System.out.println("Missing in DB2: " + key + " = " + value1);
            } else if (!value1.equals(value2)) {
                System.out.println("Mismatch: " + key + " = " + value1 + " (DB1) vs " + value2 + "(DB2)");
            }
        }

        for (Map.Entry<String, String> entry : dataMap2.entrySet()) {
            String key = entry.getKey();
            if (!dataMap1.containsKey(key)) {
                System.out.println("Missing in DB1: " + key + " = " + entry.getValue());
            }
        }
    }
    
    /**
     * Retrieves the schema of the given database connection.
     * 
     * This method fetches the metadata for all the tables in the provided database
     * connection and constructs a nested map where the key is the table name and
     * the value is another map representing the columns of that table. The inner
     * map's key is the column name and the value is the column's data type.
     * 
     * @param conn the {@code Connection} object representing the database connection
     * @return a {@code Map} where the keys are table names and the values are maps
     *         of column names to column types for each table
     * @throws SQLException if a database access error occurs
     */    
    public static Map<String, Map<String, String>> getSchema(Connection conn) throws SQLException {
        Map<String, Map<String, String>> schemaMap = new HashMap<>();
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});
        while (tables.next()) {
            String tableName = tables.getString("TABLE_NAME");
            Map<String, String> columnsMap = new HashMap<>();
            ResultSet columns = metaData.getColumns(null, null, tableName, "%");
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String columnType = columns.getString("TYPE_NAME");
                columnsMap.put(columnName, columnType);
            }
            schemaMap.put(tableName, columnsMap);
        }
        return schemaMap;
    }
    
    /**
     * Compares the schemas of two databases and prints the differences.
     * 
     * This method takes two schema maps as input, each representing the schema of a
     * database. It compares the tables and their columns between the two schemas
     * and prints out any differences, such as missing tables or columns, and
     * mismatched column types.
     * 
     * @param schema1 a {@code Map} representing the first database schema. The keys
     *                are table names, and the values are maps where the keys are
     *                column names and the values are column types.
     * @param schema2 a {@code Map} representing the second database schema. The
     *                keys are table names, and the values are maps where the keys
     *                are column names and the values are column types.
     */
       
    //Map<String, Map<String, String>> schema1 = SchemaRetriever.getSchema(conn1);
    //Map<String, Map<String, String>> schema2 = SchemaRetriever.getSchema(conn2);

    public static void compareSchemas(Map<String, Map<String, String>> schema1, Map<String, Map<String, String>> schema2) {
        for (String table : schema1.keySet()) {
            if (!schema2.containsKey(table)) {
                System.out.println("Table missing in second database: " + table);
                continue;
            }

            Map<String, String> columns1 = schema1.get(table);
            Map<String, String> columns2 = schema2.get(table);

            for (String column : columns1.keySet()) {
                if (!columns2.containsKey(column)) {
                    System.out.println("Column missing in table " + table + " of second database: " + column);
                } else if (!columns1.get(column).equals(columns2.get(column))) {
                    System.out.println("Column type mismatch in table " + table + ": " + column + " (" + columns1.get(column) + " vs " + columns2.get(column) + ")");
                }
            }

            for (String column : columns2.keySet()) {
                if (!columns1.containsKey(column)) {
                    System.out.println("Column missing in table " + table + " of first database: " + column);
                }
            }
        }

        for (String table : schema2.keySet()) {
            if (!schema1.containsKey(table)) {
                System.out.println("Table missing in first database: " + table);
            }
        }
    }
    
    /**
     * Closes the given database connection if it is not already closed.
     * 
     * @param conn the {@code Connection} object to be closed. If {@code conn} is
     *             {@code null} or already closed, this method will do nothing.
     * @throws SQLException if a database access error occurs when closing the connection.
     */
    
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}