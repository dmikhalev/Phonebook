package backend.jdbc;

import backend.db.DBManager;
import backend.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements DAO<User, Long> {

    private final DBManager dbManager;

    public UserDAO() {

        this.dbManager = new DBManager();
    }

    @Override
    public Long create(final User user) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn = dbManager.createConnection();
            st = conn.prepareStatement(SQLUser.INSERT.getQuery());
            st.setString(1, user.getUsername());
            st.setString(2, user.getPassword());
            st.setString(3, user.getTelNumber());
            st.setString(4, user.getEmail());
            rs = st.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.safeClose(rs, st, conn);
        }
        return -1L;
    }

    @Override
    public User read(final Long id) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        User resultUser = new User();
        resultUser.setId(-1L);
        try {
            conn = dbManager.createConnection();
            st = conn.prepareStatement(SQLUser.GET.getQuery());
            st.setLong(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                resultUser = rsToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.safeClose(rs, st, conn);
        }
        return resultUser.getId() == -1 ? null : resultUser;
    }


    private User rsToUser(ResultSet rs) throws SQLException {
        return new User(rs.getLong("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("telephone_number"),
                rs.getString("email"));
    }

    public User readByUsername(String username) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        User resultUser = new User();
        resultUser.setId(-1L);
        try {
            conn = dbManager.createConnection();
            st = conn.prepareStatement(SQLUser.GET_BY_USERNAME.getQuery());
            st.setString(1, username);
            rs = st.executeQuery();
            if (rs.next()) {
                resultUser = rsToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.safeClose(rs, st, conn);
        }
        return resultUser.getId() == -1 ? null : resultUser;
    }

    @Override
    public boolean update(final User user) {
        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = dbManager.createConnection();
            st = conn.prepareStatement(SQLUser.UPDATE.getQuery());
            st.setString(1, user.getPassword());
            st.setString(2, user.getEmail());
            st.setString(3, user.getTelNumber());
            st.setLong(4, user.getId());
            return st.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.safeClose(st, conn);
        }
        return false;
    }

    @Override
    public boolean delete(Long id) {
        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = dbManager.createConnection();
            st = conn.prepareStatement(SQLUser.DELETE.getQuery());
            st.setLong(1, id);
            return st.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.safeClose(st, conn);
        }
        return false;
    }

    public boolean deleteByCredentials(String username, String password) {
        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = dbManager.createConnection();
            st = conn.prepareStatement(SQLUser.DELETE_BY_CREDENTIALS.getQuery());
            st.setString(1, username);
            st.setString(2, password);
            return st.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.safeClose(st, conn);
        }
        return false;
    }

    public List<User> getAll() {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        List<User> users = new ArrayList<>();
        try {
            conn = dbManager.createConnection();
            st = conn.prepareStatement(SQLUser.GET_ALL.getQuery());
            rs = st.executeQuery();
            while (rs.next()) {
                users.add(rsToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.safeClose(rs, st, conn);
        }
        return users;
    }

    enum SQLUser {
        GET("SELECT id, username, password, telephone_number, email FROM users WHERE id=?"),
        GET_BY_USERNAME("SELECT id, username, password, telephone_number, email FROM users WHERE username=?"),
        INSERT("INSERT INTO users (id, username, password, telephone_number, email) VALUES (DEFAULT, ?, ?, ?, ?) RETURNING id"),
        DELETE("DELETE FROM users WHERE id=? RETURNING id"),
        DELETE_BY_CREDENTIALS("DELETE FROM users WHERE username=? AND password=? RETURNING id"),
        UPDATE("UPDATE users SET password=?, email=?, telephone_number=? WHERE id=? RETURNING id"),
        GET_ALL("SELECT * FROM users");
        private String query;

        SQLUser(String query) {
            this.query = query;
        }

        public String getQuery() {
            return query;
        }
    }
}
