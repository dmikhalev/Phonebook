package backend.jdbc;

import backend.db.DBManager;
import backend.entity.Address;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class AddressDAO implements DAO<Address, Long> {

    private final DBManager dbManager;

    public AddressDAO() {

        this.dbManager = new DBManager();
    }

    @Override
    public Long create(final Address address) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        Long cityId = getExistedCityId(address.getCity(), address.getCountry());
        try {
            conn = dbManager.createConnection();
            st = conn.prepareStatement(SQLAddress.INSERT.getQuery());
            st.setLong(1, cityId);
            st.setString(2, address.getStreet());
            st.setString(3, address.getBuilding());
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
    public Address read(final Long id) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        final Address resultAddress = new Address();
        resultAddress.setId(-1L);
        try {
            conn = dbManager.createConnection();
            st = conn.prepareStatement(SQLAddress.GET.getQuery());
            st.setLong(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                resultAddress.setId(rs.getLong("id"));
                resultAddress.setCity(rs.getString("city"));
                resultAddress.setCountry(rs.getString("country"));
                resultAddress.setStreet(rs.getString("street"));
                resultAddress.setBuilding(rs.getString("building"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.safeClose(rs, st, conn);
        }
        return resultAddress.getId() == -1 ? null : resultAddress;
    }

    @Override
    public boolean update(final Address address) {
        Connection conn = null;
        PreparedStatement st = null;

        Long cityId = getExistedCityId(address.getCity(), address.getCountry());
        try {
            conn = dbManager.createConnection();
            st = conn.prepareStatement(SQLAddress.UPDATE.getQuery());
            st.setLong(1, cityId);
            st.setString(2, address.getStreet());
            st.setString(3, address.getBuilding());
            st.setLong(4, address.getId());
            return st.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.safeClose(st, conn);
        }
        return false;
    }

    private Long getExistedCityId(String cityName, String countryName) {
        Long cityId = isCityExisted(cityName, countryName);
        if (cityId != null) {
            return cityId;
        }
        Long countryId = null;
        Map<String, Object> country = getCountryByName(countryName);
        if (!country.isEmpty()) {
            countryId = Long.parseLong(String.valueOf(country.get("id")));
        }

        if (cityName != null) {
            if (countryName != null && countryId == null) {
                countryId = insertCountry(countryName);
            }
            cityId = insertCity(cityName, countryId);
        }
        return cityId;
    }

    private Long isCityExisted(String cityName, String countryName) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn = dbManager.createConnection();
            st = conn.prepareStatement(SQLAddress.GET_CITY_ID.getQuery());
            st.setString(1, cityName);
            st.setString(2, countryName);
            rs = st.executeQuery();
            if (rs.next()) {
                return rs.getLong("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.safeClose(rs, st, conn);
        }
        return null;
    }

    private Map<String, Object> getCountryByName(String name) {
        if (name == null || name.isEmpty()) {
            return new HashMap<>();
        }
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        Map<String, Object> result = new HashMap<>();
        try {
            conn = dbManager.createConnection();
            st = conn.prepareStatement(SQLAddress.GET_COUNTRY_BY_NAME.getQuery());
            st.setString(1, name);
            rs = st.executeQuery();
            if (rs.next()) {
                result.put("id", rs.getLong("id"));
                result.put("name", rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.safeClose(rs, st, conn);
        }
        return result;
    }

    private Long insertCity(String name, Long country_id) {
        if (name == null || country_id == null) {
            return -1L;
        }
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn = dbManager.createConnection();
            st = conn.prepareStatement(SQLAddress.INSERT_CITY.getQuery());
            st.setString(1, name);
            st.setLong(2, country_id);
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

    private Long insertCountry(String name) {
        if (name == null) {
            return -1L;
        }
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn = dbManager.createConnection();
            st = conn.prepareStatement(SQLAddress.INSERT_COUNTRY.getQuery());
            st.setString(1, name);
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
    public boolean delete(Long id) {
        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = dbManager.createConnection();
            st = conn.prepareStatement(SQLAddress.DELETE.getQuery());
            st.setLong(1, id);
            return st.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.safeClose(st, conn);
        }
        return false;
    }

    enum SQLAddress {
        GET("SELECT a.id, country.name AS country, city.name AS city, a.street, a.building FROM address a LEFT JOIN city ON address.city_id=city.id LEFT JOIN country ON city.country_id=country.id WHERE address.id=?"),
        INSERT("INSERT INTO address (id, city_id, street, building) VALUES (DEFAULT, ?, ?, ?) RETURNING id"),
        DELETE("DELETE FROM address WHERE id=? RETURNING id"),
        UPDATE("UPDATE address SET city_id=?, street=?, building=? WHERE id=? RETURNING id"),
        GET_CITY_ID("SELECT id FROM city c WHERE c.name=? AND c.country_id=(SELECT id FROM country WHERE country.name=?)"),
        GET_COUNTRY_BY_NAME("SELECT id, name FROM country WHERE name=?"),
        INSERT_CITY("INSERT INTO city (id, name, country_id) VALUES (DEFAULT, ?, ?) RETURNING id"),
        INSERT_COUNTRY("INSERT INTO country (id, name) VALUES (DEFAULT, ?) RETURNING id");

        private String query;

        SQLAddress(String query) {
            this.query = query;
        }

        public String getQuery() {
            return query;
        }
    }
}
