package backend.jdbc;

import backend.db.DBManager;
import backend.entity.Address;
import backend.entity.Contact;
import backend.entity.Phone;
import backend.entity.PhoneType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO implements DAO<Contact, Long> {

    private final DBManager dbManager;
    private final AddressDAO addressDAO;
    private final PhoneDAO phoneDAO;
    private final long userId;

    public ContactDAO(long userId) {
        this.dbManager = new DBManager();
        this.addressDAO = new AddressDAO();
        this.phoneDAO = new PhoneDAO();
        this.userId = userId;
    }

    @Override
    public Long create(final Contact contact) {
        Connection conn = null;
        PreparedStatement st1 = null;
        PreparedStatement st2 = null;
        ResultSet rs = null;

        long contactId = -1L;
        try {
            conn = dbManager.createConnection();
            st1 = conn.prepareStatement(SQLContact.INSERT.getQuery());
            st1.setString(1, contact.getFirstName());
            st1.setString(2, contact.getMiddleName());
            st1.setString(3, contact.getLastName());
            st1.setString(4, contact.getCompany());
            st1.setString(5, contact.getEmail());
            Long addressId = addressDAO.create(contact.getAddress());
            Long phoneId = phoneDAO.create(contact.getPhone());
            st1.setLong(6, addressId);
            st1.setLong(7, phoneId);
            st1.setString(8, contact.getNote());
            rs = st1.executeQuery();
            if (rs.next()) {
                contactId = rs.getLong(1);
            }
            if (contactId > 0) {
                st2 = conn.prepareStatement(SQLContact.INSERT_USER_CONTACT.getQuery());
                st2.setLong(1, userId);
                st2.setLong(2, contactId);
                st2.executeQuery();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.safeClose(rs, st1, st2, conn);
        }
        return contactId;
    }

    @Override
    public Contact read(final Long id) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn = dbManager.createConnection();
            st = conn.prepareStatement(SQLContact.GET.getQuery());
            st.setLong(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                return rsToContact(rs);
            }
        } catch (SQLException e) {
            return null;
        } finally {
            JDBCUtils.safeClose(rs, st, conn);
        }
        return null;
    }

    private Contact rsToContact(ResultSet rs) throws SQLException {
        Address address = new Address();
        address.setId(rs.getLong("addressId"));
        address.setStreet(rs.getString("street"));
        address.setBuilding(rs.getString("building"));
        address.setCity(rs.getString("city"));
        address.setCountry(rs.getString("country"));

        Phone phone = new Phone();
        phone.setId(rs.getLong("phoneId"));
        phone.setTelNumber(rs.getString("telephone_number"));
        phone.setModel(rs.getString("model"));
        phone.setPhoneType(PhoneType.getPhoneTypeByName(rs.getString("phoneType")));

        return new Contact(rs.getLong("contactId"),
                rs.getString("first_name"),
                rs.getString("middle_name"),
                rs.getString("last_name"),
                rs.getString("company"),
                phone,
                rs.getString("email"),
                address,
                rs.getString("note"));
    }

    @Override
    public boolean update(final Contact contact) {
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dbManager.createConnection();
            st = conn.prepareStatement(SQLContact.UPDATE.getQuery());
            st.setString(1, contact.getFirstName());
            st.setString(2, contact.getMiddleName());
            st.setString(3, contact.getLastName());
            st.setString(4, contact.getCompany());
            st.setString(5, contact.getEmail());
            addressDAO.update(contact.getAddress());
            phoneDAO.update(contact.getPhone());
            st.setLong(6, contact.getId());
            return st.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.safeClose(st, conn);
        }
        return false;
    }

    @Override
    public boolean delete(final Long id) {
        Connection conn = null;
        PreparedStatement st1 = null;
        PreparedStatement st2 = null;
        ResultSet rs = null;

        long contactId = -1L;
        try {
            conn = dbManager.createConnection();
            st1 = conn.prepareStatement(SQLContact.DELETE_USER_CONTACT.getQuery());
            st1.setLong(1, userId);
            st1.setLong(2, id);
            rs = st1.executeQuery();
            if (rs.next()) {
                contactId = rs.getLong(1);
            }
            if (contactId > 0) {
                st2 = conn.prepareStatement(SQLContact.DELETE.getQuery());
                st2.setLong(1, id);
                return st2.executeQuery().next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.safeClose(rs, st1, st2, conn);
        }
        return false;
    }

    public List<Contact> getAll(Long userId) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        List<Contact> contacts = new ArrayList<>();
        try {
            conn = dbManager.createConnection();
            st = conn.prepareStatement(SQLContact.GET_ALL.getQuery());
            st.setLong(1, userId);
            rs = st.executeQuery();
            while (rs.next()) {
                contacts.add(rsToContact(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.safeClose(rs, st, conn);
        }
        return contacts;
    }

    enum SQLContact {
        GET("SELECT c.id AS contactId, c.first_name, c.middle_name, c.last_name, c.company, c.email, c.note, p.id AS phoneId, " +
                "p.telephone_number, p.model, pt.name AS phoneType, a.id AS addressId, a.street, a.building, city.name AS city, " +
                "country.name AS country FROM contact c JOIN phone p ON c.phone_id=p.id LEFT JOIN phone_type pt ON p.phone_type_id=pt.id " +
                "LEFT JOIN address a ON c.address_id a.id LEFT JOIN city ON a.city_id=city.id LEFT JOIN country ON city.country_id=country.id WHERE c.id=?"),
        INSERT("INSERT INTO contact (id, first_name, middle_name, last_name, company, email, address_id, phone_id, note) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id"),
        DELETE("DELETE FROM contact WHERE id=? RETURNING id"),
        UPDATE("UPDATE contact SET first_name=?, middle_name=?, last_name=?, company=?, email=? WHERE id=? RETURNING id"),
        INSERT_USER_CONTACT("INSERT INTO user_contact (id, user_id, contact_id) VALUES (DEFAULT, ?, ?) RETURNING id"),
        DELETE_USER_CONTACT("DELETE FROM user_contact WHERE user_id=? AND contact_id=? RETURNING id"),
        GET_ALL("SELECT c.id AS contactId, c.first_name, c.middle_name, c.last_name, c.company, c.email, c.note, p.id AS phoneId, " +
                "p.telephone_number, p.model, pt.name AS phoneType, a.id AS addressId, a.street, a.building, city.name AS city, " +
                "country.name AS country FROM contact c JOIN phone p ON c.phone_id=p.id LEFT JOIN phone_type pt ON p.phone_type_id=pt.id " +
                "LEFT JOIN address a ON c.address_id=a.id LEFT JOIN city ON a.city_id=city.id LEFT JOIN country ON city.country_id=country.id " +
                "JOIN user_contact uc ON uc.contact_id=c.id AND uc.user_id=?");
        private String query;

        SQLContact(String query) {
            this.query = query;
        }

        public String getQuery() {
            return query;
        }
    }
}
