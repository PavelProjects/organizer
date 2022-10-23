package com.povobolapo.organizer.service;

import java.util.ArrayList;

import com.povobolapo.organizer.model.UserCreditsEntity;
import com.povobolapo.organizer.model.UserEntity;
import com.povobolapo.organizer.repository.UserCreditsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserCreditsRepository userCreditsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCreditsEntity user = userCreditsRepository.findByLogin(username);
        if (null != user) {
            return new User(user.getLogin(), user.getPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}