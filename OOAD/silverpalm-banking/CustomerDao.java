package com.silverpalm.banking.dao;

import com.silverpalm.banking.core.model.Customer;
import java.util.*;

public class CustomerDao {
    private final Map<String, Customer> db = new HashMap<>();
    public void save(Customer c) { db.put(c.getCustomerId(), c); }
    public Customer find(String id) { return db.get(id); }
    public List<Customer> findAll() { return new ArrayList<>(db.values()); }
}