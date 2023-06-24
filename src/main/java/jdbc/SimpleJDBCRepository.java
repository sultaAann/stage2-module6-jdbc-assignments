package jdbc;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleJDBCRepository {

    private static final String createUserSQL = "INSERT INTO myusers (id, firstname, lastname, age) VALUES(?, ?, ?, ?);";
    private static final String updateUserSQL = "UPDATE myusers SET firstname = ? WHERE id = ?";
    private static final String deleteUser = "DELETE FROM myusers WHERE id = ?;";
    private static final String findUserByIdSQL = "SELECT * FROM myusers WHERE id = ?;";
    private static final String findUserByNameSQL = "SELECT * FROM myusers WHERE firstname = '%s';";
    private static final String findAllUserSQL = "SELECT * FROM myusers;";
    private Connection connection = null;
    private PreparedStatement ps = null;
    private Statement st = null;
    private CustomDataSource cds = CustomDataSource.getInstance();

    public Long createUser(User user) throws SQLException {
        Long id = null;
        try {
            connection = cds.getConnection();
            ps = connection.prepareStatement(createUserSQL, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, user.getId());
            ps.setString(2, user.getFirstName());
            ps.setString(3, user.getLastName());
            ps.setInt(4, user.getAge());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CustomConnector.close(connection, ps);
        }
        return id;
    }

    public User findUserById(Long userId) throws SQLException {
        connection = cds.getConnection();
        st = connection.createStatement();
        ResultSet res = st.executeQuery(findUserByIdSQL);
        return new User((long) res.getInt("id"), res.getString(2), res.getString(3), res.getInt(4));
    }

    public User findUserByName(String userName) throws SQLException {
        connection = cds.getConnection();
        st = connection.createStatement();
        ResultSet res = st.executeQuery(String.format(findUserByNameSQL, userName));
        return new User((long) res.getInt("id"), res.getString(2), res.getString(3), res.getInt(4));
    }

    public List<User> findAllUser() throws SQLException {
        List<User> users = new ArrayList<>();
        connection = cds.getConnection();
        st = connection.createStatement();
        ResultSet res = st.executeQuery(findAllUserSQL);
        while (res.next()) {
            users.add(new User((long) res.getInt("id"), res.getString(2), res.getString(3), res.getInt(4)));
        }
        return users;
    }

    public User updateUser(User user) throws SQLException {
        try {
            connection = cds.getConnection();
            ps = connection.prepareStatement(updateUserSQL);
            ps.setLong(1, user.getId());
            ps.setString(2, user.getFirstName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CustomConnector.close(connection, ps);
        }
        return user;
    }

    public void deleteUser(Long userId) throws SQLException {
        connection = cds.getConnection();
        ps = connection.prepareStatement(deleteUser);
        ps.setLong(1, userId);
        ps.execute();
    }
}
