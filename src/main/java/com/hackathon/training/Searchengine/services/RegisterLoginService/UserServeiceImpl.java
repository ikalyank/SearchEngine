package com.hackathon.training.Searchengine.services.RegisterLoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hackathon.training.Searchengine.model.User;
import com.hackathon.training.Searchengine.repository.IEngineUserRepository;

@Service
public class UserServeiceImpl implements IUserService{
    @Autowired
    private IEngineUserRepository engineUserRepository;
    public UserServeiceImpl(IEngineUserRepository engineUserRepository)
    {
        this.engineUserRepository=engineUserRepository;
    }

    @Override
    public User saveUser(User user)
    {
        return engineUserRepository.save(user);
    }

    @Override
    public User fetchUserByEmailId(String email) {
        return engineUserRepository.findByEmail(email);
    }
    @Override
    public User fetchUserByEmailandPassword(String email,String password)
    {
        return engineUserRepository.findByEmailAndPassword(email,password);
    }
    public User fetchUserByUsername(String username)
    {
        return engineUserRepository.findByUsername(username);
    }
}
