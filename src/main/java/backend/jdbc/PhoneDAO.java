package backend.jdbc;

import backend.db.DBManager;
import backend.entity.Phone;
import backend.entity.PhoneType;

import java.sql.*;

public class PhoneDAO implements DAO<Phone, Long> {

    private final DBManager dbManager;

    public PhoneDAO() {
        this.dbManager = new DBManager();
    }

    @Override
    public Long create(final Phone phone) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn = dbManager.createConnection();
            st = conn.prepareStatement(SQLPhone.INSERT.getQuery());
            st.setString(1, phone.getTelNumber());
            st.setString(2, phone.getModel());
            st.setString(3, phone.getPhoneType().toString());
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
    public Phone read(final Long id) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        final Phone resultPhone = new Phone();
        resultPhone.setId(-1L);
        try {
            conn = dbManager.createConnection();
            st = conn.prepareStatement(SQLPhone.GET.getQuery());
            st.setLong(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                resultPhone.setId(rs.getLong("id"));
                resultPhone.setTelNumber(rs.getString("telephone_number"));
                resultPhone.setModel(rs.getString("model"));
                resultPhone.setPhoneType(PhoneType.getPhoneTypeByName(rs.getString("type")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.safeClose(rs, st, conn);
        }
        return resultPhone.getId() == -1 ? null : resultPhone;
    }

    @Override
    public boolean update(final Phone phone) {
        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = dbManager.createConnection();
            st = conn.prepareStatement(SQLPhone.UPDATE.getQuery());
            st.setString(1, phone.getTelNumber());
            st.setString(2, phone.getModel());
            st.setString(3, phone.getPhoneType().toString());
            st.setLong(4, phone.getId());
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
            st = conn.prepareStatement(SQLPhone.DELETE.getQuery());
            st.setLong(1, id);
            return st.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.safeClose(st, conn);
        }
        return false;
    }

    enum SQLPhone {
        GET("SELECT p.id, p.telephone_number, p.model, pt.name AS type FROM phone p JOIN phone_type pt ON p.type_id=pt.id WHERE p.id=?"),
        INSERT("INSERT INTO phone (id, telephone_number, model, phone_type_id) VALUES (DEFAULT, ?, ?, (SELECT id FROM phone_type pt WHERE pt.name=?)) RETURNING id"),
        DELETE("DELETE FROM phone WHERE id=? RETURNING id"),
        UPDATE("UPDATE phone SET telephone_number=?, model=?, phone_type_id=(SELECT id FROM phone_type WHERE name=?) WHERE id=? RETURNING id");

        private String query;

        SQLPhone(String query) {
            this.query = query;
        }

        public String getQuery() {
            return query;
        }
    }
}
