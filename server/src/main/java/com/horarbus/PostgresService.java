package com.horarbus;

import java.sql.*;
import java.util.Properties;

public class PostgresService {

    private Statement statement;

    public PostgresService() {
        statement = setup_postgres_connection();
    }

    public Statement getStatement() {
        return statement;
    }

    public ResultSet executeQuery(String query) {

        if (statement == null) {
            return null;
        }

        try {
            return statement.executeQuery(query);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String select_column(String column, String table, String condition) {
        try {
            ResultSet rs = null;
            String query = "SELECT " + column + " FROM " + table;
            if (condition == null) {
                rs = executeQuery(query + ";");
            } else {
                rs = executeQuery(query + " WHERE " + condition + ";");
            }

            if(rs.next()) {
                String value = rs.getString(column);
                //System.out.println(key);
                return value;
            }

            return "";
        } catch(SQLException e) {
            e.printStackTrace();

            return "";
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
