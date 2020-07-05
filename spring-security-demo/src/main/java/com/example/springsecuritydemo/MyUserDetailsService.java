package com.example.springsecuritydemo;

import com.example.springsecuritydemo.entity.Users;
import com.example.springsecuritydemo.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Users user = usersRepository.getByUsername(s);
        if (user == null) {
            throw new UsernameNotFoundException("username not found: " + s);
        }
        return new MyUserDetails(user);
    }
}
