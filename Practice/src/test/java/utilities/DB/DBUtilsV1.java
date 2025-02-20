package utilities.DB;

import org.junit.Assert;
import utilities.ConfigReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtilsV1 {
    private static Connection connection;
    private static Statement statement;
    private static final String JDBC_URL = ConfigReader.getProperty("jdbc_url");

    // Opening connection to a DB. If connection is not yet opened.
    public static void open() {
        try {
            if (connection == null) {
                connection = DriverManager.getConnection(JDBC_URL);
                statement = connection.createStatement();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail("Can't establish connection to DB");
        }
    }

    public static void close() {
        try {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
            connection = null;
            statement = null;
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail("Can't close connection to DB");
        }
    }

    // Get Column names by using Result Set metadata
    private static List<String> getColumnNames(ResultSet rs) {
        List<String> columnNames = new ArrayList<>();
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail(e.getLocalizedMessage());
        }
        return columnNames;
    }

    public static ResultSet query(String query) {
        if (connection == null) open();
        try {
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail("Not Able to execute a query");
        }
        return null;
    }

    // Key - column name, value - data in that column
    public static List<Map<String, Object>> convertResultSet(ResultSet rs) {
        List<Map<String, Object>> table = new ArrayList<>();
        List<String> columnNames = getColumnNames(rs);
        // Populate table From result set
        // Iterate through each Row
        while (true) {
            // Map is created for each row
            Map<String, Object> row = new HashMap<>();
            try {
                if (!rs.next()) break;
                // Iterate through each column in order to populate the map
                for (String columnName : columnNames) {
                    row.put(columnName, rs.getObject(columnName));
                }
                table.add(row);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // Close result set after we are done
        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return table;
    }
}
