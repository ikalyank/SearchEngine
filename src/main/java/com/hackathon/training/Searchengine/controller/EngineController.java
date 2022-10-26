package com.hackathon.training.Searchengine.controller;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


import com.hackathon.training.Searchengine.utilities.UserValidation;
import org.apache.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hackathon.training.Searchengine.Exceptions.UserExistException;
import com.hackathon.training.Searchengine.Exceptions.UserNotFoundException;
import com.hackathon.training.Searchengine.model.Drive;
import com.hackathon.training.Searchengine.model.Search;
import com.hackathon.training.Searchengine.model.SearchDB;
import com.hackathon.training.Searchengine.model.Transaction;
import com.hackathon.training.Searchengine.model.User;
import com.hackathon.training.Searchengine.model.UserPermission;
import com.hackathon.training.Searchengine.services.EngineSearchService.EngineServiceImpl;
import com.hackathon.training.Searchengine.services.NotificationService.EngineEmailService;
import com.hackathon.training.Searchengine.services.RegisterLoginService.UserServeiceImpl;
import com.hackathon.training.Searchengine.services.TransactionService.TransactionServiceImpl;
import com.hackathon.training.Searchengine.utilities.Constants;
import com.hackathon.training.Searchengine.utilities.MailAction;

@RestController

@RequestMapping("/auth/v1")
public class EngineController {
    @Autowired
    EngineServiceImpl engineServiceImpl;

    @Autowired
    private UserServeiceImpl userServeiceImpl;

    @Autowired
    private EngineEmailService engineEmailService;

    @Autowired
    private TransactionServiceImpl transactionService;
    private static final org.apache.log4j.Logger logger = LogManager.getLogger(EngineController.class);

    public EngineController(EngineServiceImpl engineServiceImpl) {
        this.engineServiceImpl = engineServiceImpl;
    }
    List<SearchDB> searchDBs = new ArrayList<>();
    UserValidation validation = new UserValidation();
    // Fetches the Drives and information(Description) About them and returns String
    @GetMapping("/drives")
    public ResponseEntity<List<Drive>> ShowDrives() {
        if (validation.validateUser(Constants.CURRENTUSER))
        {
            Transaction transaction = new Transaction();
            transaction.setDate(new Date(System.currentTimeMillis()));
            transaction.setUserid(Constants.CURRENTUSER);
            transaction.setNotee("User Searched For Drives On Computer");
            transactionService.saveTransaction(transaction);
        }
        return ResponseEntity.status(HttpStatus.OK).body(engineServiceImpl.ShowDrivesOnPc());
    }

    // takes filename As input in the endpoint URL and searches the file and returns
    // the List of files Found
    // Saves the Searches user performed to the DB
    @PostMapping("/saveSearch/{filename}")
    public ResponseEntity<List<String>> saveSearchFindfile(@PathVariable String filename) throws InterruptedException  
    {
        ArrayList<String> paths = new ArrayList<>();
        if (validation.validateUser(Constants.CURRENTUSER)) {
            // Checks the DB first for the previous results (Paths) if present with same keyword it adds paths to List
            Thread thread1 = new Thread() {
                @Override
                public void run() {
                    searchDBs = engineServiceImpl.fetchbyKeyword(filename);
                    for (SearchDB s : searchDBs) {
                        paths.add(s.getPath());
                    }
                    Transaction transaction = new Transaction();
                    transaction.setDate(new Date(System.currentTimeMillis()));
                    transaction.setUserid(Constants.CURRENTUSER);
                    transaction.setNotee("User Searched For " + filename);
                    transactionService.saveTransaction(transaction);
                    // if not found in Db then it searches the computer
                    Search search = new Search();
                    search.setName(filename);
                    engineServiceImpl.saveModel(search);
                    // engineServiceImpl.searchFile(dir, filename);

                    // Multi Threading Implementation
                    ArrayList<Drive> drivesOnPc = engineServiceImpl.ShowDrivesOnPc();
                    //System.out.println(drivesOnPc.size());
                    logger.info(drivesOnPc.size());
                    Thread[] threadArray = new Thread[drivesOnPc.size()];
                    int threadCount = 0;
                    for (Drive drive : drivesOnPc) {
                        threadArray[threadCount] = new Thread() {
                            @Override
                            public void run() {
                                File dir = new File(drive.getDrive().toString());
                                logger.info(dir);
                                paths.addAll(engineServiceImpl.searchFile(dir, filename));
                            }
                        };
                    }
                    for (int j = 0; j < drivesOnPc.size(); j++) {
                        threadArray[0].start();
                    }
                    logger.info("---------------------------------------------------------------------");
                    logger.info("Search Performed");
                    logger.info("---------------------------------------------------------------------");
                    // paths=engineServiceImpl.searchFile(dir, filename);
                    // Adding Paths to DB along with Keyword
                    for (String s : paths) {
                        SearchDB searchDB = new SearchDB();
                        searchDB.setKeyword(filename);
                        searchDB.setPath(s);
                        engineServiceImpl.saveSearchDB(searchDB);
                    }
                }
            };
            thread1.start();
//        thread2.sleep(500);
//        thread2.start();
        }
    return ResponseEntity.status(HttpStatus.OK).body(paths);
    }
    @PostMapping("/saveSearchNothread/{filename}")
    public ResponseEntity<List<String>> saveSearchFindfileNoThread(@PathVariable String filename)
    {
        List<String> paths = new ArrayList<>();
        if (validation.validateUser(Constants.CURRENTUSER)) {
            File drive = new File("/Users/kalyan/Desktop");
            paths.addAll(engineServiceImpl.searchFile(drive, filename));
            logger.info("---------------------------------------------------------------------");
            logger.info("Search Performed");
            logger.info("---------------------------------------------------------------------");
            // paths=engineServiceImpl.searchFile(dir, filename);
            // Adding Paths to DB along with Keyword
            for (String s : paths) {
                SearchDB searchDB = new SearchDB();
                searchDB.setKeyword(filename);
                searchDB.setPath(s);
                engineServiceImpl.saveSearchDB(searchDB);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(paths);
    }

    // used to get the Search History preformed by the user
    @GetMapping("/getSearchHistory")
    public List<Search> getSearchHistory()
    {
        if (validation.validateUser(Constants.CURRENTUSER)) {
            Transaction transaction = new Transaction();
            transaction.setDate(new Date(System.currentTimeMillis()));
            transaction.setUserid(Constants.CURRENTUSER);
            transaction.setNotee("User Searched For Searched History");
            transactionService.saveTransaction(transaction);
        }
        return engineServiceImpl.getSearchHistory();
    }

    // Registers user And sends Confirmation message to User Saves User to the DB
    @PostMapping("/registerUser")
    public User registerUser(@RequestBody User user) throws UserExistException {
        String tempEmailId = user.getEmail();
        if (tempEmailId != null && !"".equals(tempEmailId)) {
            User userobj = userServeiceImpl.fetchUserByEmailId(tempEmailId);
            if (userobj != null) {
                throw new UserExistException("User with the Email " + tempEmailId + "Already Exists");
            }
            //System.out.println("Sending Confirmation Email");
            logger.info("Sending Confirmation Email");
            engineEmailService.sendRegisterEmail(user.getEmail(), user.getUsername(),MailAction.REGISTER);
            //System.out.println("Closing Confirmation Email");
            logger.info("Closing Confirmation Email");
            Transaction transaction = new Transaction();
            transaction.setDate(new Date(System.currentTimeMillis()));
            transaction.setUserid(user.getId());
            transaction.setNotee("User : "+user.getUsername()+" registered");
            transactionService.saveTransaction(transaction);
        }
        return userServeiceImpl.saveUser(user);
    }

    // Login user by checking the DB And sends Confirmation message to User After
    // Successfull login
    @PostMapping("/loginUser")
    public ResponseEntity<User> loginUser(@RequestBody User user) throws UserNotFoundException {
        String tempEmailId = user.getEmail();
        String tempPassword = user.getPassword();
        User userobj = null;
        if (tempEmailId != null && tempPassword != null) {
            userobj = userServeiceImpl.fetchUserByEmailandPassword(tempEmailId, tempPassword);

        }
        if (userobj != null) {
            Constants.CURRENTUSER=userobj.getId();
            Transaction transaction = new Transaction();
            transaction.setDate(new Date(System.currentTimeMillis()));
            transaction.setUserid(userobj.getId());
            transaction.setNotee("User : "+userobj.getUsername()+" Logged in");
            transactionService.saveTransaction(transaction);
            System.out.println("Sending Confirmation Email");
            engineEmailService.sendloginEmail(user.getEmail(), userobj.getUsername(),MailAction.LOGIN);
            System.out.println("Closing Confirmation Email");
        } else
            throw new UserNotFoundException("User Not Found");
        return ResponseEntity.status(HttpStatus.OK).body(userobj);
    }

    // Allows User To Change password
    @PostMapping("/changePassword")
    public ResponseEntity<User> changePassword(@RequestBody User user) throws UserNotFoundException {
        String tempEmailId = user.getEmail();
        User userObj = null;
        if (tempEmailId != null && !"".equals(tempEmailId)) {
            userObj = userServeiceImpl.fetchUserByEmailId(tempEmailId);
            if (userObj != null) {
                userObj.setPassword(user.getPassword());
                userServeiceImpl.saveUser(userObj);
                System.out.println("Sending Confirmation Email");
                engineEmailService.sendPasswordChangeEmail(userObj.getEmail(), userObj.getUsername(),MailAction.CHANGEPASSWORD);
                System.out.println("Closing Confirmation Email");
                Transaction transaction = new Transaction();
                transaction.setDate(new Date(System.currentTimeMillis()));
                transaction.setUserid(Constants.CURRENTUSER);
                transaction.setNotee("User: "+userObj.getUsername() +"changed his password");
                transactionService.saveTransaction(transaction);
            } else
                throw new UserNotFoundException("The User is not Found with the Entered Email");
        }
        return ResponseEntity.status(HttpStatus.OK).body(userObj);
    }

    // Clears Search History From Data Base
    @DeleteMapping("/clearSearchHistoy")
    public String clearHistory() {
        if (validation.validateUser(Constants.CURRENTUSER)) {
            Transaction transaction = new Transaction();
            transaction.setDate(new Date(System.currentTimeMillis()));
            transaction.setUserid(Constants.CURRENTUSER);
            transaction.setNotee("User Cleared Search History");
            transactionService.saveTransaction(transaction);
        }
        return engineServiceImpl.deleteSearchHistory();
    }

    // Delete the Searched paths form DB
    @DeleteMapping("/clearPaths/{keyword}")
    public void clearPathHistory(@PathVariable String keyword) {
        engineServiceImpl.deletewithKeyword(keyword);
    }

    // View The User Details
    @GetMapping("/viewUser/{username}")
    public ResponseEntity<User> viewUser(@PathVariable String username) throws UserNotFoundException {
        User user = null;
        if (validation.validateUser(Constants.CURRENTUSER)) {
            user = userServeiceImpl.fetchUserByUsername(username);
            if (user == null) {
                throw new UserNotFoundException("User not found");
            }
            Transaction transaction = new Transaction();
            transaction.setDate(new Date(System.currentTimeMillis()));
            transaction.setUserid(Constants.CURRENTUSER);
            transaction.setNotee("User Viewed His/Her Details");
            transactionService.saveTransaction(transaction);
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    // Update the User permission
    @PostMapping("/updateUserPermission/{username}")
    public ResponseEntity<User> updateUserPermission(@PathVariable String username,
            @RequestBody UserPermission userPermission) throws UserNotFoundException {
        User updatedUser = null;
        if (validation.validateUser(Constants.CURRENTUSER)) {
            User user = userServeiceImpl.fetchUserByUsername(username);
            if (user == null) {
                throw new UserNotFoundException("User not found");
            }
            user.setDrives(userPermission.getDrives());
            user.setCanDeleteSearchRecord(userPermission.isCanDeleteSearchRecord());
            updatedUser = userServeiceImpl.saveUser(user);
            engineEmailService.sendPemissionUpdateEmail(user.getEmail(), username, MailAction.PERMISSION);
            Transaction transaction = new Transaction();
            transaction.setDate(new Date(System.currentTimeMillis()));
            transaction.setUserid(Constants.CURRENTUSER);
            transaction.setNotee("User : " + username + " Updated the permissions");
            transactionService.saveTransaction(transaction);
        }
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    // View Permision details
    @GetMapping("/viewUserPermission/{username}")
    public ResponseEntity<UserPermission> viewUserPermission(@PathVariable String username)
            throws UserNotFoundException {
        User user = userServeiceImpl.fetchUserByUsername(username);
        UserPermission userPermisionObj = null;
        if (validation.validateUser(Constants.CURRENTUSER)) {
            if (user == null) {
                throw new UserNotFoundException("User not found");
            }
            userPermisionObj = new UserPermission();
            userPermisionObj.setCanDeleteSearchRecord(user.isCanDeleteSearchRecord());
            userPermisionObj.setDrives(user.getDrives());
            Transaction transaction = new Transaction();
            transaction.setDate(new Date(System.currentTimeMillis()));
            transaction.setUserid(Constants.CURRENTUSER);
            transaction.setNotee("User : " + username + " Viewed the Permisions");
            transactionService.saveTransaction(transaction);
        }
        return ResponseEntity.status(HttpStatus.OK).body(userPermisionObj);
    }
//    View Transactions performed by user
    @GetMapping("/viewTransations/{username}")
    public ResponseEntity<List<Transaction>> getTransactionsperformedbyuser(@PathVariable String username)
    {
        List<Transaction> transactionList=null;
        int userid = userServeiceImpl.fetchUserByUsername(username).getId();
        if(validation.validateUser(Constants.CURRENTUSER))
        {
            transactionList= new ArrayList<>();
            transactionList = transactionService.getUserTransaction(userid);
        }
        return ResponseEntity.status(HttpStatus.OK).body(transactionList);
    }
}