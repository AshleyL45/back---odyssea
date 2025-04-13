package com.example.odyssea.services;

import com.example.odyssea.exceptions.ValidationException;
import com.example.odyssea.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    public Integer getCurrentUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();  // Contient les informations sur l'utilisateur connecté
        if (!(auth.getPrincipal() instanceof CustomUserDetails)) {
            throw new ValidationException("Unauthenticated user. Please login or register");
        }
        return ((CustomUserDetails) auth.getPrincipal()).getUserId();
    }
}
