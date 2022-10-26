package com.hackathon.training.Searchengine.services.TransactionService;

import com.hackathon.training.Searchengine.model.Transaction;

import java.util.List;

public interface ITransactionService {
    void saveTransaction(Transaction transaction);
    List<Transaction> getAllTransactions();
}
