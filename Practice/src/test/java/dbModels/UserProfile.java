package dbModels;


import lombok.Data;
import utilities.DB.DBUtilsV2;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Data
public class UserProfile {
    private int id;
    private String address;
    private String country;
    private String first_name;
    private String last_name;
    private String region;
    private String ssn;
    private String title;
    private String dob;
    private String dom;
    private String email_address;
    private String gender;
    private String home_phone;
    private String locality;
    private String mobile_phone;
    private String postal_code;
    private String work_phone;

    private static final String INSERT_QUERY = "INSERT INTO digitalbank.user_profile VALUE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private final String[] fields = {"id", "address", "country", "dob", "dom", "email_address", "first_name", "gender", "home_phone", "last_name", "locality", "mobile_phone", "postal_code", "region", "ssn", "title", "work_phone"};
    // Custom ResultSet to Bean, same as beanProcessor.toBeanList
    public UserProfile(ResultSet resultSet) {
        try {
            this.id = resultSet.getInt("id");
            this.address = resultSet.getString("address");
            this.country = resultSet.getString("country");
            this.first_name = resultSet.getString("first_name");
            this.last_name = resultSet.getString("last_name");
            this.region = resultSet.getString("region");
            this.ssn = resultSet.getString("ssn");
            this.title = resultSet.getString("title");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public UserProfile() {
    }

    public static UserProfile getById(int id) {
        String query = "SELECT *\n" +
                " FROM user_profile" +
                " WHERE id = ?;";
        List<UserProfile> userProfiles = DBUtilsV2.query(query, id).toBeans(UserProfile.class);
        if (userProfiles.size() == 0) return null;
        return userProfiles.get(0);
    }

    public boolean insert() {
        return DBUtilsV2.insertBean(INSERT_QUERY, this, fields);
    }
}
