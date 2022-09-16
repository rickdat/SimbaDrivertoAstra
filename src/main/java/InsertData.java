import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InsertData {
    private static final String KEYSPACE = "test";

    public static void main(String[] argv) {

        try {
            Connection dbConnection = SimbaConnection.getInstance();
            insertRecordsIntoTable(dbConnection);
            getRows(dbConnection);
            dbConnection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Insert some records using batch updates.
     * Assumes a table exists:  CREATE TABLE "jdbc_example" ( "text" varchar(10) );
     */
    private static void insertRecordsIntoTable(Connection dbConnection) throws Exception {
        System.out.println("Inserting records.");
        PreparedStatement preparedStatement = null;

        String createTable = "CREATE TABLE IF NOT EXISTS " + KEYSPACE + ".example (\n" +
                "  id int PRIMARY KEY,\n" +
                "  description text\n" +
                ");";

        String insertTableSQL = "INSERT INTO " + KEYSPACE + ".example (id,description) VALUES (?,?)";
        try {
            dbConnection.prepareStatement(createTable).execute();
            preparedStatement = dbConnection.prepareStatement(insertTableSQL);

            // Create multiple statements and add to a batch update.
            for (int cnt = 1; cnt <= 50; cnt++) {
                preparedStatement.setString(2, "some string " + cnt);
                preparedStatement.setInt(1, cnt);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();  // For large numbers of records, recommend doing sets of executeBatch commands.
            System.out.println("Records committed");


        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    private static void getRows(Connection dbConnection) throws SQLException {
        String selectTableSQL = "SELECT * FROM " + KEYSPACE + ".example";
        ResultSet results = dbConnection.prepareStatement(selectTableSQL).executeQuery();
        while (results.next()) {
            System.out.println(results.getInt(1) + " " + results.getString(2));
        }
    }


}