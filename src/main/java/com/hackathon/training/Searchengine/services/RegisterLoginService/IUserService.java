package com.hackathon.training.Searchengine.services.RegisterLoginService;

import com.hackathon.training.Searchengine.model.User;

public interface IUserService {
    User saveUser(User user);
    User fetchUserByEmailId(String email);
    User fetchUserByEmailandPassword(String email,String password);
}
