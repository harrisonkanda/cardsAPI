package com.logicea.logiceacardsproject.service;

import com.logicea.logiceacardsproject.exception.UserNotFoundException;
import com.logicea.logiceacardsproject.model.UserModel;
import com.logicea.logiceacardsproject.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Method for retrieving a user from the DB and loading it into a UserDetails Object
     *
     * @param email - the unique email Id for the user
     * @return - {@link UserDetails}
     */
    public UserDetails loadUserByEmail(final String email) {

        System.out.println("loadUserByEmail() invoked with user email [=" + email + "] as method argument");
        log.info("loadUserByEmail() invoked with user email [=" + email + "] as method argument");

        Optional<UserModel> userModel = userRepository.findByEmail(email);

        if (userModel.isEmpty()) {
            throw new UserNotFoundException("User not found with email Id[" + email + "]");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(userModel.get().getRole().name()));

        return new User(
                userModel.get().getEmail(),
                userModel.get().getPassword(),
                true,
                true,
                true,
                true,
                authorities
        );
    }

    public Optional<UserModel> findUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }
}
