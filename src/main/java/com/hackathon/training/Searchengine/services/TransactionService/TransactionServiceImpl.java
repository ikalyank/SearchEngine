package com.hackathon.training.Searchengine.services.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hackathon.training.Searchengine.model.Transaction;
import com.hackathon.training.Searchengine.repository.ITransactionRepository;

import java.util.List;

@Service
public class TransactionServiceImpl implements ITransactionService{
    @Autowired
    ITransactionRepository transactionRepository;
    @Override
    public void saveTransaction(Transaction transaction)
    {
        transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
    public List<Transaction> getUserTransaction(int userid)
    {
        return transactionRepository.findAllByUserid(userid);
    }
}
