package utilities.DB;

import org.apache.commons.dbutils.QueryRunner;
import org.junit.Assert;
import java.sql.*;


public class DBUtilsV2 {
    private static Connection connection;
    private static Statement statement;
    private static QueryRunner queryRunner = new QueryRunner();
    private static final String JDBC_URL =
            "jdbc:mysql://3.131.35.165:3306/{DB}?user=dbank&password=MyCOMPleaxPasSW0rd!12X";

    // Opening connection to a DB. If connection is not yet opened.
    public static void open() {
       open("");
    }

    public static void open(String database) {
        try {
            if (connection == null) {
                connection = DriverManager.getConnection(JDBC_URL.replace("{DB}", database));
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
    //WHERE id IN (?, ?, ?);
// Should have same number of params as we have '?' in our query/statement
    public static boolean executeStatement(String sqlStatement, Object... params) {
        if (connection == null) open();
        try {
           if(params.length == 0) return statement.execute(sqlStatement);
           PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            return preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean insertBean(String query, Object bean, String[] properties) {
        if (connection == null) open();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            queryRunner.fillStatementWithBean(preparedStatement, bean, properties);
            return preparedStatement.execute();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean truncateTable(String tableName) {
        String sqlStatement = String.format("TRUNCATE Table %s;", tableName);
        return executeStatement(sqlStatement);
    }

    public static boolean deleteRecord(String table, String column, String value) {
        String statement = String.format("DELETE FROM %s WHERE %s = '%s'", table, column, value);
        return executeStatement(statement);
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
