package dbModels;

import lombok.Data;
import utilities.DBUtilsV2;

import java.sql.ResultSet;
import java.util.List;

@Data
public class Transaction {
    private String transactionDescription;
    private double transactionAmount;
    private double runningBalance;
    private String transactionDate;
    private String transactionCategory;
    private String transactionName;
    private String transactionState;

    private static final String BASE_QUERY = "SELECT T1.description AS transactionDescription, T1.amount AS transactionAmount,\n" +
            "\t      T1.running_balance AS runningBalance, T1.transaction_date AS transactionDate,\n" +
            "        T2.category AS transactionCategory, T2.name AS transactionName," +
            "        T3.name AS transactionState\n" +
            "FROM account_transaction T1\n" +
            "JOIN transaction_type T2\n" +
            "ON T1.transaction_type_id = T2.id\n" +
            "JOIN transaction_state T3\n" +
            "ON T1.transaction_state_id = T3.id\n";

    public static Transaction getById(int id) {
        String query = BASE_QUERY + "WHERE T1.id = ?;";

        ResultSet rs = DBUtilsV2.query(query, id);
        List<Transaction> transactionList = DBUtilsV2.convertResultSetToBeans(rs, Transaction.class);
        if(transactionList.isEmpty()) return null;
        return transactionList.get(0);
    }

    public static List<Transaction> getByCategory(String category) {
        String query = BASE_QUERY + "WHERE T2.category = ?;";
        ResultSet rs = DBUtilsV2.query(query, category);
        return DBUtilsV2.convertResultSetToBeans(rs, Transaction.class);
    }

}
