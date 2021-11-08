package com.horarbus;

import java.sql.*;
import java.util.Properties;

public class PostgresHandler {

    private Statement statement;

    public PostgresHandler() {
        statement = setup_postgres_connection();
    }

    public Statement getStatement() {
        return statement;
    }
    public Connection getConnection() {
        try {
            return getStatement().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet executeQuery(String query) {

        if (getStatement() == null) {
            return null;
        }

        try {
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet executeQuery(PreparedStatement query) {
        if (query == null) {
            return null;
        }

        try {
            return query.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private PreparedStatement generate_prepared_statement(String query) {
        try {
            return getConnection().prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private PreparedStatement generate_select_query(String column, String table, String conditionColumn) {
        String query = "SELECT " + column + " FROM " + table;
        //String query = "SELECT ? FROM ?";

        if (conditionColumn == null) {
            query += ";";
        } else {
            query += " WHERE " + conditionColumn + "= ?;";
        }

        return generate_prepared_statement(query);
    }

    private PreparedStatement generate_update_query(String column, String table, String conditionColumn) {
        String query = "UPDATE " + table +
                " SET " + column + " = ?" +
                " WHERE " + conditionColumn + " = ?;";

        return generate_prepared_statement(query);
    }

    private PreparedStatement generate_insert_query(String table, String[] columns) {
        String query = "INSERT INTO " + table + " (";
        for (int i = 0; i < columns.length; i++) {
            String col = columns[i];
            query += col;

            if (i != columns.length - 1) {
                query += ", ";
            }
        }
        query += ") " +
                "VALUES (";
        for (int i = 0; i < columns.length; i++) {
            query += "?";

            if (i != columns.length - 1) {
                query += ", ";
            }
        }
        query += ");";

        return generate_prepared_statement(query);
    }

    public String select_column(String column,
                                String table,
                                String conditionColumn,
                                String conditionValue) {
        PreparedStatement query = generate_select_query(column, table, conditionColumn);
        if (query == null) {
            return "";
        }

        try {
            query.setString(1, conditionValue);

            ResultSet rs = executeQuery(query);

            if (rs.next())  {
                return rs.getString(column);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String select_column(String column,
                                String table,
                                String conditionColumn,
                                int conditionValue) {
        PreparedStatement query = generate_select_query(column, table, conditionColumn);
        if (query == null) {
            return "";
        }

        try {
            query.setInt(1, conditionValue);

            ResultSet rs = executeQuery(query);

            if (rs.next())  {
                return String.valueOf(rs.getInt(column));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void insert_row(String table, String[] columns, String[] values) {
        if (columns.length != values.length){
            return;
        }

        PreparedStatement query = generate_insert_query(table, columns);
        if (query == null) {
            return;
        }

        try {
            for (int i = 1; i <= values.length; i++) {
                query.setString(i, values[i - 1]);
            }

            executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update_column(String column,
                              String table,
                              String value,
                              String conditionColumn,
                              String conditionValue) {
        PreparedStatement query = generate_update_query(column, table, conditionColumn);
        if (query == null) {
            return;
        }

        try {
            query.setString(1, value);
            query.setString(2, conditionValue);

            executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void update_column(String column,
                              String table,
                              String value,
                              String conditionColumn,
                              int conditionValue) {
        PreparedStatement query = generate_update_query(column, table, conditionColumn);
        if (query == null) {
            return;
        }

        try {
            query.setString(1, value);
            query.setInt(2, conditionValue);

            executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void update_column(String column,
                              String table,
                              int value,
                              String conditionColumn,
                              String conditionValue) {
        PreparedStatement query = generate_update_query(column, table, conditionColumn);
        if (query == null) {
            return;
        }

        try {
            query.setInt(1, value);
            query.setString(2, conditionValue);
            executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void update_column(String column,
                              String table,
                              int value,
                              String conditionColumn,
                              int conditionValue) {
        PreparedStatement query = generate_update_query(column, table, conditionColumn);
        if (query == null) {
            return;
        }

        try {
            query.setInt(1, value);
            query.setInt(2, conditionValue);
            executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void update_column(String column,
                              String table,
                              long value,
                              String conditionColumn,
                              String conditionValue) {
        PreparedStatement query = generate_update_query(column, table, conditionColumn);
        if (query == null) {
            return;
        }

        try {
            query.setTimestamp(1, new Timestamp(value));
            query.setString(2, conditionValue);
            executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void update_column(String column,
                              String table,
                              long value,
                              String conditionColumn,
                              int conditionValue) {
        PreparedStatement query = generate_update_query(column, table, conditionColumn);
        if (query == null) {
            return;
        }

        try {
            query.setTimestamp(1, new Timestamp(value));
            query.setInt(2, conditionValue);
            executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Statement setup_postgres_connection() {
        // Setup drivers
        // https://forum.katalon.com/t/java-sql-sqlexception-no-suitable-driver-found-for-jdbc-localhost-5433/30308
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // https://docs.microsoft.com/en-us/sql/connect/jdbc/step-3-proof-of-concept-connecting-to-sql-using-java?view=sql-server-ver15
        String connectionUrl =  "jdbc:postgresql://localhost:5433/postgres";
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "postgres");

        try {
            Connection connection = DriverManager.getConnection(connectionUrl, props);
            Statement statement = connection.createStatement();

            System.out.println("Connection successful");
            return statement;
        } catch(SQLException e)
        {
            System.out.println("Connection error");
            e.printStackTrace();

            return null;
        }
    }
}
