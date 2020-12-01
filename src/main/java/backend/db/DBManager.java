package backend.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBManager {

    private static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/phonebook";
    private static final String USER = "postgres";
    private static final String PASS = "12345";

    public Connection createConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
