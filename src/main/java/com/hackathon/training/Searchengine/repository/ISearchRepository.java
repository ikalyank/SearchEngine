package com.hackathon.training.Searchengine.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hackathon.training.Searchengine.model.SearchDB;


@Repository
public interface ISearchRepository extends JpaRepository<SearchDB,Integer> {
    List<SearchDB> findAllByKeyword(String keyword);
     String deleteAllByKeyword(String keyword);
}
