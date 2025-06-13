package com.example.odyssea.services;

import com.example.odyssea.exceptions.UserNotFoundException;
import com.example.odyssea.exceptions.ValidationException;
import com.example.odyssea.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    public Integer getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new UserNotFoundException("Unauthenticated user. Please login or register.");
        }

        System.out.println("Auth principal: " + auth.getPrincipal().getClass().getName());

        if (!(auth.getPrincipal() instanceof CustomUserDetails customUser)) {
            throw new UserNotFoundException("Unexpected principal type.");
        }

        return customUser.getUserId();
    }


}
