package com.hackathon.training.Searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hackathon.training.Searchengine.model.Transaction;

import java.util.List;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction,Integer> {
    List<Transaction> findAllByUserid(int userid);
}
