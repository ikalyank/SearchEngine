package com.hackathon.training.Searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hackathon.training.Searchengine.model.Search;

@Repository
public interface IEngineRepository extends JpaRepository<Search,Integer> {
    
}
