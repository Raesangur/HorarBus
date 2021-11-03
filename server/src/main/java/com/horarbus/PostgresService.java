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
