package com.silverpalm.banking.dao;

import com.silverpalm.banking.core.model.*;
import com.silverpalm.banking.core.exception.BankingException;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDaoJdbc implements IAccountDao {

    @Override
    public void save(Account a) {
        String sql = "MERGE INTO accounts(account_number,customer_id,account_type,balance,branch,status) " +
                "KEY(account_number) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement ps = DaoBaseJdbc.getConnection().prepareStatement(sql)) {
            ps.setString(1, a.getAccountNumber());
            ps.setString(2, a.getCustomer().getCustomerId());
            ps.setString(3, a.getAccountType().name());
            ps.setBigDecimal(4, BigDecimal.valueOf(a.getBalance()));
            ps.setString(5, a.getBranch());
            ps.setString(6, a.getStatus().name());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("save account", e);
        }
    }

    @Override
    public List<Account> findByCustomer(String customerId) {
        String sql = "SELECT * FROM accounts WHERE customer_id = ?";
        List<Account> list = new ArrayList<>();
        try (PreparedStatement ps = DaoBaseJdbc.getConnection().prepareStatement(sql)) {
            ps.setString(1, customerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException | BankingException e) {
            throw new RuntimeException("findByCustomer", e);
        }
    }

    @Override
    public Account find(String accountNumber) {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        try (PreparedStatement ps = DaoBaseJdbc.getConnection().prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? map(rs) : null;
        } catch (SQLException | BankingException e) {
            throw new RuntimeException("find account", e);
        }
    }

    /* ---- convert row → object ---- */
    private Account map(ResultSet rs) throws SQLException, BankingException {
        String accNum   = rs.getString("account_number");
        double balance  = rs.getBigDecimal("balance").doubleValue();
        String branch   = rs.getString("branch");
        String custId   = rs.getString("customer_id");
        AccountType type= AccountType.valueOf(rs.getString("account_type"));
        Customer customer = new CustomerDaoJdbc().find(custId);

        switch (type) {
            case SAVINGS:
                return new SavingsAccount(accNum, balance, branch, customer);
            case INVESTMENT:
                return new InvestmentAccount(accNum, balance, branch, customer);
            case CHEQUE:
                return new ChequeAccount(accNum, balance, branch, customer,
                        "Employer", "Employer Address");
            default:
                throw new IllegalArgumentException("Unknown type " + type);
        }
    }
}