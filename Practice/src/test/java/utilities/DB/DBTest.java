package utilities.DB;

import dbModels.UserProfile;
import org.junit.AfterClass;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBTest {
    @AfterClass
    public static void tearDown() {
        DBUtilsV2.close();
    }
    // Get User profiles From DB and map to beans using custom constructor
    @Test
    public void getUserProfiles() throws SQLException {
        ResultSet rs = DBUtilsV2.queryToRs("SELECT * FROM user_profile;");
        while (rs.next()) {
            System.out.println(new UserProfile(rs));
        }
    }
}
