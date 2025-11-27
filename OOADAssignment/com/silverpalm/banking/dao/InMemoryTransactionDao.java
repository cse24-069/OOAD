package OOADAssignment.com.silverpalm.banking.dao;

import OOADAssignment.com.silverpalm.banking.core.model.Transaction;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTransactionDao implements ITransactionDao {
    private final List<Transaction> list = new ArrayList<>();

    @Override
    public void save(Transaction t) {
        list.add(t);
    }

    @Override
    public List<Transaction> findByAccount(String accountNumber, int limit) {
        return list.stream()
                .filter(t -> t.getAccountNumber().equals(accountNumber))
                .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
                .limit(limit)
                .collect(Collectors.toList());
    }
}