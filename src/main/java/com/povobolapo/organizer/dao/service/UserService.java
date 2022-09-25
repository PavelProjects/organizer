package com.povobolapo.organizer.dao.service;


import com.povobolapo.organizer.dao.repository.UserRepository;
import com.povobolapo.organizer.model.TUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {
    @Autowired
    UserRepository userRepository;

    public TUser getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }
}
