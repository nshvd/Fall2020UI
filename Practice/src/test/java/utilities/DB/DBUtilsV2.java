package utilities.DB;

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
    // VarArgs
    // ["String"]
    // ["String1", "String2"]
    //SELECT *
    //FROM digitalbank.user_profile
    //WHERE id = ?;

    public static boolean executeStatement(String sqlStatement) {
        if (connection == null) open();
        try {
            return statement.execute(sqlStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Object... = Object[]
    public static ResultSetHandler query(String query, Object... params) {
        return new ResultSetHandler(queryToRs(query, params));
    }

    public static ResultSet queryToRs(String query, Object... params) {
        if (connection == null) open();
        try {
            if (params.length == 0) statement.executeQuery(query);

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
