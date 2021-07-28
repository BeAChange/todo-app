package com.todolist.security;

import com.todolist.dto.UserLoginDto;
import com.todolist.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository repository;

    @Autowired
    public AppUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) {
        UserLoginDto entity = repository.findByLogin(login);

        if (entity == null) {
            throw new UsernameNotFoundException(login);
        }

        return new AppUserDetails(entity, null, true);
    }

}
