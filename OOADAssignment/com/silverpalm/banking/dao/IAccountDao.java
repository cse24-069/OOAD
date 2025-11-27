package OOADAssignment.com.silverpalm.banking.dao;

import OOADAssignment.com.silverpalm.banking.core.model.Account;
import java.util.List;

public interface IAccountDao {
    void save(Account a);
    List<Account> findByCustomer(String customerId);
    Account find(String accountNumber);
}