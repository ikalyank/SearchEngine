package com.hackathon.training.Searchengine.services.EngineSearchService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.filechooser.FileSystemView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hackathon.training.Searchengine.model.Drive;
import com.hackathon.training.Searchengine.model.Search;
import com.hackathon.training.Searchengine.model.SearchDB;
import com.hackathon.training.Searchengine.repository.IEngineRepository;
import com.hackathon.training.Searchengine.repository.ISearchRepository;


@Service
public class EngineServiceImpl implements IEngnieService {
    @Autowired
    IEngineRepository engineRepository;

    @Autowired
    ISearchRepository searchRepository;
    // Show the Drives on the Pc
    ArrayList<String> filesFoundPath = new ArrayList<>();
    @Override
    public ArrayList<Drive> ShowDrivesOnPc()
    {
        File[] paths;
        FileSystemView fsv = FileSystemView.getFileSystemView();
        ArrayList<Drive> driveList = new ArrayList<>();
        paths = File.listRoots();        
        for(File path:paths)
        {
            // String k = "Drive Name: "+path+"Description: "+fsv.getSystemTypeDescription(path);
            Drive d = new Drive();
            d.setDesc(fsv.getSystemTypeDescription(path));
            d.setDrive(path);
            driveList.add(d);
        }
        return driveList;
    }
    // Fetches the Files by taking The input
    @Override
    public ArrayList<String> searchFile(File folder, String fileName)  {
		File files[] = folder.listFiles();

        if(files != null)
		for(File currentFile : files ) {
			if(currentFile.isDirectory()) {
                String foldername = currentFile.getName().toLowerCase();
                if(foldername.equals("system") || foldername.equals("library")){
                    continue;
                }
                filesFoundPath.addAll(searchFile(currentFile, fileName));
			}
			else if(currentFile.isFile()) {
				if(currentFile.getName().contains(fileName)) {
				System.out.println(currentFile.getAbsolutePath());
					filesFoundPath.add(currentFile.getAbsolutePath());
				}
			}
		}
        return filesFoundPath;
	}
    // Saves the searched file name to DB
    @Override
    public Search saveModel(Search search) {
        return engineRepository.save(search);
    }
    // Returns the Search History DB
    @Override
    public List<Search> getSearchHistory()
    {
        return engineRepository.findAll();
    }
    // Clears the Search History From DB
    public String deleteSearchHistory()
    {
        engineRepository.deleteAll();
        return "Search History Cleared";
    }
    // Writes Logs For the Files Searched in SearchLogs.log
    @Override
    public void saveLogs(List<String> paths,String logFileName) throws IOException
    {
        FileWriter fileWriter = new FileWriter(new File(logFileName));
        for(String path : paths)
        {
            fileWriter.write(path +"\n");
        }
        fileWriter.close();
    }
    // Saves the paths of the searches performed with the help of Keyword to search;
    @Override
    public SearchDB saveSearchDB(SearchDB searchDB)
    {
        return searchRepository.save(searchDB);
    }
    // returns the Paths saved in DB when a keyword is passed as parameter
    public List<SearchDB> fetchbyKeyword(String keyword)
    {
        return searchRepository.findAllByKeyword(keyword);
    }
    // Delete the paths from DB by passing keyword
     public String deletewithKeyword(String keyword)
     {
         searchRepository.deleteAllByKeyword(keyword);
         return "Deleted";
     }
}
