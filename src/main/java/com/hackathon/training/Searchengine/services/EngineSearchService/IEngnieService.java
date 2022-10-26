package com.hackathon.training.Searchengine.services.EngineSearchService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.hackathon.training.Searchengine.model.Drive;
import com.hackathon.training.Searchengine.model.Search;
import com.hackathon.training.Searchengine.model.SearchDB;

public interface IEngnieService {
    public ArrayList<Drive> ShowDrivesOnPc();
    Search saveModel(Search search);
    List<Search> getSearchHistory();
    ArrayList<String> searchFile(File folder, String fileName) throws FileNotFoundException;
    void saveLogs(List<String> paths,String logFileName)throws IOException;
    SearchDB saveSearchDB(SearchDB searchDB);
}
