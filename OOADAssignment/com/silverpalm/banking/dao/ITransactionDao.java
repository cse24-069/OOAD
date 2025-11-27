package OOADAssignment.com.silverpalm.banking.dao;

import OOADAssignment.com.silverpalm.banking.core.model.Transaction;
import java.util.List;

public interface ITransactionDao {
    void save(Transaction t);
    List<Transaction> findByAccount(String accountNumber, int limit);
}