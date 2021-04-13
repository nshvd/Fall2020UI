package utilities;

import org.apache.commons.dbutils.BeanProcessor;
import org.junit.Assert;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtilsV2 {
    private static Connection connection;
    private static Statement statement;
    private static final String JDBC_URL = "jdbc:mysql://3.131.35.165:3306/digitalbank?user=dbank&password=MyCOMPleaxPasSW0rd!12X";
    private static BeanProcessor beanProcessor;

    // Opening connection to a DB. If connection is not yet opened.
    public static void open() {
        try {
            if (connection == null) {
                connection = DriverManager.getConnection(JDBC_URL);
                statement = connection.createStatement();
                beanProcessor = new BeanProcessor();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail("Can't establish connection to DB");
        }
    }

    // Get Column names by using Result Set metadata
    public static List<String> getColumnNames(ResultSet rs) {
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


                                                    // VarArgs
                                                    // ["String"]
                                                    // ["String1", "String2"]
    //SELECT *
   //FROM digitalbank.user_profile
   //WHERE id = ?;
    public static ResultSet query(String query, Object... params) {
        if (connection == null) open();
        try {
            if (params.length == 0) return statement.executeQuery(query);

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            // Replace question marks with params
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail("Not Able to execute a query");
        }
        return null;
    }

    // T - Declares generic data type
    // In a way it postpones the Data type declaration
    public static <T> List<T> convertResultSetToBeans(ResultSet rs, Class<T> beanClass) {
        try {
            return beanProcessor.toBeanList(rs, beanClass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    // Key - column name, value - data in that column
    public static List<Map<String, Object>> convertResultSetToListOfMaps(ResultSet rs) {
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
}
