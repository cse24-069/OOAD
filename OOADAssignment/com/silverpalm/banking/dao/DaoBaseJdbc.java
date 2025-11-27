package OOADAssignment.com.silverpalm.banking.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DaoBaseJdbc {
    private static final String URL  = "jdbc:h2:./data/banking;DB_CLOSE_ON_EXIT=FALSE;AUTO_RECONNECT=TRUE";
    private static final String USER = "sa";
    private static final String PASS = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}