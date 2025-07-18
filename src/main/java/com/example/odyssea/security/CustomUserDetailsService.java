package com.example.odyssea.security;

import com.example.odyssea.daos.userAuth.UserDao;
import com.example.odyssea.entities.userAuth.User;
import com.example.odyssea.exceptions.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDao userDao;

    public CustomUserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userDao.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return new CustomUserDetails(user);
    }


    public UserDetails loadById(Integer id){
        User user = userDao.findById(id);
        return new CustomUserDetails(user);
    }
}
