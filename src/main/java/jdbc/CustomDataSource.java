package jdbc;

import lombok.Getter;
import lombok.Setter;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

@Getter
@Setter
public class CustomDataSource implements DataSource {
    private static volatile CustomDataSource instance;
    private final String driver;
    private final String url;
    private final String name;
    private final String password;

    private CustomDataSource(String driver, String url, String password, String name) {
        this.driver = driver;
        this.url = url;
        this.password = password;
        this.name = name;
    }

    public static CustomDataSource getInstance() {
        if (instance == null) {
            synchronized (CustomDataSource.class) {
                if (instance == null) {
                    instance = new CustomDataSource("org.postgresql.Driver", "jdbc:postgres://localhost:5432/myfirstdb", "postgres", "user");
                }
            }
        }
        return instance;
    }

    public Connection getConnection(String url) throws SQLException {
        return new CustomConnector().getConnection(url, name, password);
    }

    public Connection getConnection() throws SQLException {
        return new CustomConnector().getConnection(getUrl(), getName(), getPassword());
    }

    public Connection getConnection(String username, String password) throws SQLException {
        return new CustomConnector().getConnection(url, username, password);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}