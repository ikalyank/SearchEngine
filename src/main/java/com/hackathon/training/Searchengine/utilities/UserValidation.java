package com.hackathon.training.Searchengine.utilities;

public class UserValidation {
    public boolean validateUser(int val)
    {
        if(val!=-1){
            return true;
        }
        else {
            try {
                throw new Exception("Login to use the Search engine");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
