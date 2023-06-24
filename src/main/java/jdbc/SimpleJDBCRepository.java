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

    private static final String DBNAME = "myfirstdb";
    private static final String CREATE_USER_SQL = "INSERT INTO " + DBNAME + " (firstname, lastname, age) VALUES (?, ?, ?);";
    private static final String UPDATE_USER_SQL = "UPDATE " + DBNAME + " SET firstname = ?, lastname = ?, age = ? WHERE id = ?";
    private static final String DELETE_USER_SQL = "DELETE FROM " + DBNAME + " WHERE id = ?";
    private static final String FIND_USER_BY_ID_SQL = "SELECT * FROM " + DBNAME + " WHERE id = ?";
    private static final String FIND_USER_BY_NAME_SQL = "SELECT * FROM " + DBNAME + " WHERE firstname = ? OR lastname = ?";
    private static final String FIND_ALL_USERS_SQL = "SELECT * FROM " + DBNAME;
    private Connection connection = null;
    private PreparedStatement ps = null;
    private Statement st = null;

    public Long createUser(User user){
        try {
            ps = connection.prepareStatement(CREATE_USER_SQL);
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setInt(3, user.getAge());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user.getId();
    }

    public User findUserById(Long userId) {
        User user = new User();
        ResultSet res;
        try {
            ps = connection.prepareStatement(FIND_USER_BY_ID_SQL);
            ps.setLong(1, userId);
            res = ps.executeQuery();
            user.setId(res.getLong(1));
            user.setFirstName(res.getString(2));
            user.setLastName(res.getString(3));
            user.setAge(res.getInt(4));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public User findUserByName(String userName) {
        User user = new User();
        try {
            ps = connection.prepareStatement(FIND_USER_BY_NAME_SQL);
            ps.setString(1, userName);
            ps.setString(2, userName);
            ResultSet res = ps.executeQuery();
            user.setId(res.getLong(1));
            user.setFirstName(res.getString(2));
            user.setLastName(res.getString(3));
            user.setAge(res.getInt(4));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public List<User> findAllUser() {
        List<User> result = new ArrayList<>();
        try {
            ps = connection.prepareStatement(FIND_ALL_USERS_SQL);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                result.add(new User(res.getLong(1), res.getString(2), res.getString(3), res.getInt(4)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public User updateUser(User user) {
        try {
            ps = connection.prepareStatement(UPDATE_USER_SQL);
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setInt(3, user.getAge());
            ps.setLong(4, user.getId());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public void deleteUser(Long userId) {
        try {
            ps = connection.prepareStatement(DELETE_USER_SQL);
            ps.setLong(1, userId);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
