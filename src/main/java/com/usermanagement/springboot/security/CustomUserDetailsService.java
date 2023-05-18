package com.usermanagement.springboot.security;

import com.usermanagement.springboot.daos.UserDAO;
import com.usermanagement.springboot.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static com.usermanagement.springboot.common.Constants.USER_NOT_FOUND;

@Service
public class CustomUserDetailsService implements UserDetailsService {
   @Autowired
    private UserDAO userDAO;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userDAO.findUserByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(USER_NOT_FOUND)
                );
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        );
    }


}
