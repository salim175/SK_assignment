package com.example.backend.service;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, BCryptPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    //create user
    public User register(User user) {
        if(repository.existsByUserEmail(user.getUserEmail())) {
            throw new RuntimeException("Email is already in use");
        }

        if (repository.existsByUserPhoneNumber(user.getUserPhoneNumber())) {
            throw new RuntimeException("Phone number is already in use");
        }
            
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        return repository.save(user);
    }

    //login user
    public User login(String email, String password) {
        User user = repository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("IIInvalid email or password"));

        if (passwordEncoder.matches(password, user.getUserPassword())) {
            return user;
        } else {
            throw new RuntimeException("Invalid email or password");
        }
    }   

    //get all users
    public List<User> findAll() {
        return repository.findAll();
    }

    //get user by id
    public User findById(Long id) {   
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    //delete user
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    //update user
    public User update(Long id, User updatedUser) {
        User existingUser = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setUserName(updatedUser.getUserName());
        existingUser.setUserAge(updatedUser.getUserAge());
        existingUser.setUserPhoneNumber(updatedUser.getUserPhoneNumber());
        existingUser.setUserAddress(updatedUser.getUserAddress());
        existingUser.setUserEmail(updatedUser.getUserEmail());

        // encoded the password if updated
        if (updatedUser.getUserPassword() != null && !updatedUser.getUserPassword().isEmpty()) {
            existingUser.setUserPassword(passwordEncoder.encode(updatedUser.getUserPassword()));
        }

        return repository.save(existingUser);
    }

}
