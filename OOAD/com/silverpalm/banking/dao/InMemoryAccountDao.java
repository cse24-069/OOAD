package com.silverpalm.banking.dao;

import com.silverpalm.banking.core.model.Account;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryAccountDao implements IAccountDao {
    private final Map<String, Account> map = new HashMap<>();

    @Override
    public void save(Account a) {
        map.put(a.getAccountNumber(), a);
    }

    @Override
    public List<Account> findByCustomer(String customerId) {
        return map.values()
                .stream()
                .filter(a -> a.getCustomer().getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

    @Override
    public Account find(String accountNumber) {
        return map.get(accountNumber);
    }
}