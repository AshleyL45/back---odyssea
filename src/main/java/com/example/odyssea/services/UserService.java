package com.example.odyssea.services;

import com.example.odyssea.daos.userAuth.UserDao;
import com.example.odyssea.dtos.UserName;
import com.example.odyssea.entities.userAuth.User;
import com.example.odyssea.exceptions.InvalidPasswordException;
import com.example.odyssea.exceptions.UserAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final UserDao userDao;
    private final PasswordEncoder encoder;
    private final CurrentUserService currentUserService;

    public UserService(UserDao userDao, PasswordEncoder encoder, CurrentUserService currentUserService) {
        this.userDao = userDao;
        this.encoder = encoder;
        this.currentUserService = currentUserService;
    }

    public void register(User user){
        boolean alreadyExists = userDao.existsByEmail(user.getEmail());
        if (alreadyExists) {
            throw new UserAlreadyExistsException("Email " + user.getEmail() + " already in use.");
        }
        User newUser = new User(
                user.getEmail(),
                encoder.encode(user.getPassword()),
                "USER",
                user.getFirstName(),
                user.getLastName()
        );

        userDao.save(newUser);
    }

    public void changeUserInformation(User user){
        Integer userId = getUserId();
        userDao.update(userId, user);
    }

    public UserName getUserName(){
        User user = userDao.findById(currentUserService.getCurrentUserId());
        return new UserName(user.getFirstName(), user.getLastName());
    }

    public void changePassword(String newPassword){
        Integer userId = getUserId();
        if (newPassword == null || newPassword.isBlank()) {
            throw new InvalidPasswordException("Password cannot be null.");
        }
        userDao.updatePassword(userId, newPassword);
    }

    public void deleteAccount(){
        Integer userId = getUserId();
        userDao.delete(userId);
    }

    private Integer getUserId(){
        return currentUserService.getCurrentUserId();
    }
}
