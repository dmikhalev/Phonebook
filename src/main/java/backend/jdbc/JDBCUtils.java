package backend.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCUtils {

    public static void safeClose(Object... objects) {
        for (Object object : objects) {
            if (object instanceof Connection) {
                safeClose((Connection) object);
            } else if (object instanceof Statement) {
                safeClose((Statement) object);
            } else if (object instanceof ResultSet) {
                safeClose((ResultSet) object);
            }
        }
    }

    private static void safeClose(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        }
    }


    private static void safeClose(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException ignored) {
            }
        }
    }


    private static void safeClose(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ignored) {
            }
        }
    }
}
