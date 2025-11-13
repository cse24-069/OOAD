package com.silverpalm.banking.dao;

import com.silverpalm.banking.core.model.Customer;
import java.util.List;

public interface ICustomerDao {
    void save(Customer c);
    Customer find(String customerId);
    List<Customer> findAll();
}