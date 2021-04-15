package utilities.DB;

import dbModels.UserProfile;
import org.junit.AfterClass;
import org.junit.Assert;
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
//        DBUtilsV2.open("digitalbank");
        ResultSet rs = DBUtilsV2.queryToRs("SELECT * FROM digitalbank.user_profile;");
        while (rs.next()) {
            System.out.println(new UserProfile(rs));
        }
    }

    @Test
    public void truncate() {
        DBUtilsV2.truncateTable("Test.my_table");
    }

    @Test
    public void update() {
        String email = "newjaneemail@gmail.com";
        int id = 1;
        String query = "SELECT email FROM Test.my_table WHERE id = ?";
        String updateStatement = "UPDATE Test.my_table SET email = ? WHERE id = ?;";
        // UPDATE RECORD
        DBUtilsV2.executeStatement(updateStatement, email, id);
        // VALIDATE RECORD WAS UPDATED
        Assert.assertEquals(
                DBUtilsV2.query(query, id).toListOfMaps().get(0).get("email"),
                email
        );
    }

    @Test
    public void delete() {
        DBUtilsV2.deleteRecord("Test.my_table", "id", "1");
    }

    @Test
    public void insertWithQuery() {
        DBUtilsV2.executeStatement(
                "INSERT INTO Test.my_table VALUE (null, 'Jack', 'Doe', null)");
    }

    @Test
    public void paramInsert() {
        DBUtilsV2.executeStatement(
                "INSERT INTO Test.my_table VALUE (?, ?, ?, ?)",
                null, "James", "Bond", "James@gmail.com");
    }

    @Test
    public void insertProfile() {
        UserProfile userProfile = new UserProfile();
        userProfile.setId(999);
        userProfile.setDob("1964-05-18 02:31:26");
        userProfile.setEmail_address("email@gmail.com");
        userProfile.setSsn("178-14-8401");
        userProfile.insert();
    }
}
