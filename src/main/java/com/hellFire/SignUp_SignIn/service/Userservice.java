package com.hellFire.SignUp_SignIn.service;


import com.hellFire.SignUp_SignIn.entity.Users;
import com.hellFire.SignUp_SignIn.repo.userRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Userservice {


    private final userRepo repo;

    @Autowired
    public Userservice(userRepo userRepo){
        this.repo=userRepo;
    }

    public boolean isUsernameAvailable(String username) {
        return repo.findByUsername(username) == null;
    }

    public Users addUser(Users user) {
        // Validate input
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        // Save and return the user
        return repo.save(user);
    }

    public Optional<Users> getUser(int id) {
        return repo.findById(id);
    }

    public boolean deleteUser(int id) {
        if (repo.existsById(id)) { // Check if the user exists before deleting
            repo.deleteById(id); // Delete the user
            System.out.println("User with ID " + id + " deleted successfully."); // Log the success
            return true;
        } else {
//            throw new IllegalArgumentException("User with ID " + id + " does not exist.");
            return false;
        }
    }

    public Users updateUser(Users newUser) {
        // Check if the user exists in the database
        Optional<Users> existingUser = repo.findById(newUser.getId());

        if (existingUser.isPresent()) {
            // Update only specific fields (if necessary)
            Users userToUpdate = existingUser.get();
            userToUpdate.setUsername(newUser.getUsername());
            userToUpdate.setPassword(newUser.getPassword());
            // Save the updated user
            return repo.saveAndFlush(userToUpdate);
        } else {
            // Handle the case where the user doesn't exist
            throw new IllegalArgumentException("User with id " + newUser.getId() + " does not exist.");
        }
    }

}
