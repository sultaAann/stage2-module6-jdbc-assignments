package jdbc;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomConnector {
    public static void close(Connection connection, PreparedStatement ps) throws SQLException {
        ps.close();
        connection.close();
    }

    public Connection getConnection(String url) throws SQLException {
        return DriverManager.getConnection(url);
    }

    public Connection getConnection(String url, String user, String password) throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
