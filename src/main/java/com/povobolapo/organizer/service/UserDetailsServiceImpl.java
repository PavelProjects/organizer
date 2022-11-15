package com.povobolapo.organizer.service;

import com.povobolapo.organizer.model.Role;
import com.povobolapo.organizer.model.UserCreditsEntity;
import com.povobolapo.organizer.repository.UserCreditsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Scope("singleton")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserCreditsRepository userCreditsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCreditsEntity user = userCreditsRepository.findByLogin(username);
        // Если юзер существует, но не активен - не авторизовываем
        if (null != user && user.isActive()) {
            return new User(user.getLogin(), user.getPassword(), getGrantedAuthorities(user.getRoles()));
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    private Collection<GrantedAuthority> getGrantedAuthorities(Collection<Role> roles) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role: roles) {
            authorities.addAll(role.getPrivileges().stream().map(privilege -> new SimpleGrantedAuthority(privilege.getName())).collect(Collectors.toList()));
        }
        return authorities;
    }
}