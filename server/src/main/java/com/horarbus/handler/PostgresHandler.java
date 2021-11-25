package com.horarbus.handler;

import java.sql.*;
import java.util.Properties;

import com.horarbus.handler.PostgresValue.PostgresValueType;

import java.util.ArrayList;

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
            // e.printStackTrace();
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

    private PreparedStatement generate_select_query(String column, String table, String[] conditionColumns) {
        StringBuilder query = new StringBuilder("SELECT " + column + " FROM " + table);
        //String query = "SELECT ? FROM ?";

        if (conditionColumns == null || conditionColumns.length == 0) {
            query.append(";");
        } else {
            query.append(" WHERE ");

            // Insert condition columns
            for (int i = 0; i < conditionColumns.length; i++) {
                query.append(conditionColumns[i] + "=");
                if (i == conditionColumns.length - 1) {
                    query.append(" ");
                } else {
                    query.append(", ");
                }
            }

            // Insert blank values
            query.append("? ".repeat(conditionColumns.length));
            query.append(";");
        }

        return generate_prepared_statement(query.toString());
    }

    private PreparedStatement generate_update_query(String column, String table, String[] conditionColumns) {
        StringBuilder query = new StringBuilder("UPDATE " + table +
                " SET " + column + " = ?" + " WHERE ");

        if (conditionColumns.length == 1) {
            query.append(conditionColumns[0]).append(" = ?;");
        }
        else {
            for(int i = 0; i < conditionColumns.length; i++) {
                query.append(conditionColumns[i]).append(" = ?");

                if (i < conditionColumns.length + 1) {
                    query.append(" AND ");
                }
                else {
                    query.append(";");
                }
            }
        }

        return generate_prepared_statement(query.toString());
    }

    private PreparedStatement generate_insert_query(String table, String[] columns) {
        StringBuilder query = new StringBuilder("INSERT INTO " + table + " (");
        for (int i = 0; i < columns.length; i++) {
            String col = columns[i];
            query.append(col);

            if (i != columns.length - 1) {
                query.append(", ");
            }
        }
        query.append(") " + "VALUES (");
        for (int i = 0; i < columns.length; i++) {
            query.append("?");

            if (i != columns.length - 1) {
                query.append(", ");
            }
        }
        query.append(");");

        return generate_prepared_statement(query.toString());
    }

    private void insert_query(PreparedStatement query, PostgresValue value, int index) {
        try {
            if (value.getType() == PostgresValue.PostgresValueType.integer) {
                query.setInt(index, value.getInt());
            }
            if (value.getType() == PostgresValue.PostgresValueType.string) {
                query.setString(index, value.getString());
            }
            if(value.getType() == PostgresValueType.timestamp){
                query.setTimestamp(index, value.getTimestamp());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String[] select_column(String column,
                                  String table,
                                  String[] conditionColumns,
                                  PostgresValue[] conditionValues) {
        PreparedStatement query = generate_select_query(column, table, conditionColumns);
        if (query == null) {
            return null;
        }

        try {
            for(int i = 1; i <= conditionValues.length; i++) {
                insert_query(query, conditionValues[i - 1], i);
            }
            ResultSet rs = executeQuery(query);

            ArrayList<String> result = new ArrayList<String>();
            while(rs.next())  {
                result.add(rs.getString(column));
            }
            String[] results = new String[result.size()];
            result.toArray(results);
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insert_row(String table, String[] columns, PostgresValue[] values) {
        if (columns.length != values.length){
            return;
        }

        PreparedStatement query = generate_insert_query(table, columns);
        if (query == null) {
            return;
        }

        for (int i = 1; i <= values.length; i++) {
            insert_query(query, values[i - 1], i);
        }

        executeQuery(query);
    }

    public void update_column(String column,
                              String table,
                              PostgresValue value,
                              String[] conditionColumns,
                              PostgresValue[] conditionValues) {
        if (conditionColumns.length != conditionValues.length) {
            return;
        }

        PreparedStatement query = generate_update_query(column, table, conditionColumns);
        if (query == null) {
            return;
        }

        insert_query(query, value, 1);

        for (int i = 2; i <= conditionValues.length + 1; i++) {
            insert_query(query, conditionValues[i - 2], i);
        }

        executeQuery(query);
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
