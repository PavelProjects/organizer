package com.povobolapo.organizer.service;


import com.povobolapo.organizer.repository.UserRepository;
import com.povobolapo.organizer.model.UserEntity;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UserService {
    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity createUser(UserEntity user) {
        String encodedPassword = encodePassword(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        user.setPassword(null);
        return user;
    }

    public boolean deleteUser(String login) {
        UserEntity user = userRepository.findByLogin(login);
        if (login == null || user.getId() < 1) {
            return false;
        }
        userRepository.delete(user);
        return true;
    }

    public UserEntity getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }
    public String encodePassword(String password) {
        return DigestUtils.sha1Hex(password);
    }
}
