package com.silverpalm.banking.dao;

import com.silverpalm.banking.core.model.Account;
import java.util.List;

public interface IAccountDao {
    void save(Account a);
    List<Account> findByCustomer(String customerId);
    Account find(String accountNumber);
}