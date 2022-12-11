package com.chabbay.store;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * connection to a local SQlite data base
 *
 * @author Linus Englert
 */
public class DataBaseController {
    private static final DataBaseController instance = new DataBaseController();
    private static Connection connection;
    private static final String DB_PATH = "database.sqlite";

    /**
     * this class is a singleton and should not be instantiated directly!
     */
    public static DataBaseController getInstance() {
        return instance;
    }

    /**
     * private constructor so people know to use the getInstance() function instead
     */
    private DataBaseController() {
    }

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Fehler beim Laden des JDBC-Treibers");
            e.printStackTrace();
        }
    }

    public void init() {
        try {
            if (connection != null) return;
            System.out.println("Creating Connection to Database...");
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
            if (!connection.isClosed()) System.out.println("...Connection established");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (!connection.isClosed() && connection != null) {
                    connection.close();
                    if (connection.isClosed()) System.out.println("Connection to Database closed");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }));
    }

    public void test() {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DROP TABLE IF EXISTS books;");
            stmt.executeUpdate("CREATE TABLE books (author, title, publication, pages, price);");
            stmt.execute("INSERT INTO books (author, title, publication, pages, price) VALUES ('Paulchen Paule'," +
                    "'Paul der Penner', '2001-05-06', '1234', '5.67')");

            ResultSet rs = stmt.executeQuery("SELECT * FROM books;");
            while (rs.next()) {
                System.out.println("Autor = " + rs.getString("author"));
                System.out.println("Titel = " + rs.getString("title"));
                System.out.println("Erscheinungsdatum = "
                        + rs.getString("publication"));
                System.out.println("Seiten = " + rs.getInt("pages"));
                System.out.println("Preis = " + rs.getDouble("price"));
            }

            rs.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println("Couldn't handle DB-Query");
            e.printStackTrace();
        }
    }
}