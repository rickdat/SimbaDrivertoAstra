import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Objects;

public class SimbaConnection {

    private static Connection instance;

    private SimbaConnection(Connection connection) {
        instance = connection;
    }

    public static Connection getInstance() throws Exception {
        if (instance == null) {
            instance = getDBConnection();
        }
        return instance;
    }

    /**
     * Create a connection to the database.
     */
    private static Connection getDBConnection() throws Exception {
        Connection connection = null;
        connection = DriverManager.getConnection(buildConnectionUrl());
        return connection;
    }

    private static String buildConnectionUrl() {
        String path = Objects.requireNonNull(SimbaConnection.class.getClassLoader().getResource("secure-connect-dbtest.zip")).getPath();
        return "jdbc:cassandra://;" +
                "AuthMech=2;" +
                "TunableConsistency=6;" +
                "UID=RHvKbghcBscHnFR" +
                "PWD=NtkTSHh1EZ__9uDQeKXvHSIvCKlHyW+8bI7-WmMMoRldfnsr5tvtpoQ4j+" +
                "SecureConnectionBundlePath=" + path;
    }

}
