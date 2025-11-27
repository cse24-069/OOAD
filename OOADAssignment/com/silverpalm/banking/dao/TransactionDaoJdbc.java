package OOADAssignment.com.silverpalm.banking.dao;

import OOADAssignment.com.silverpalm.banking.core.model.Transaction;
import OOADAssignment.com.silverpalm.banking.core.model.TransactionType;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDaoJdbc implements ITransactionDao {

    @Override
    public void save(Transaction t) {
        String sql = "INSERT INTO transactions(transaction_id,account_number,transaction_type,amount,description,balance_after,transaction_date) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement ps = DaoBaseJdbc.getConnection().prepareStatement(sql)) {
            ps.setString(1, t.getTransactionId());
            ps.setString(2, t.getAccountNumber());
            ps.setString(3, t.getType().name());
            ps.setBigDecimal(4, BigDecimal.valueOf(t.getAmount()));
            ps.setString(5, t.getDescription());
            ps.setBigDecimal(6, BigDecimal.valueOf(t.getBalanceAfter()));
            ps.setTimestamp(7, Timestamp.valueOf(t.getTimestamp()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("save transaction", e);
        }
    }

    @Override
    public List<Transaction> findByAccount(String accountNumber, int limit) {
        String sql = "SELECT * FROM transactions WHERE account_number = ? ORDER BY transaction_date DESC LIMIT ?";
        List<Transaction> list = new ArrayList<>();
        try (PreparedStatement ps = DaoBaseJdbc.getConnection().prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("findByAccount", e);
        }
    }

    private Transaction map(ResultSet rs) throws SQLException {
        return new Transaction(
                rs.getString("transaction_id"),
                rs.getString("account_number"),
                TransactionType.valueOf(rs.getString("transaction_type")),
                rs.getBigDecimal("amount").doubleValue(),
                rs.getString("description"),
                rs.getBigDecimal("balance_after").doubleValue()
        );
    }
}