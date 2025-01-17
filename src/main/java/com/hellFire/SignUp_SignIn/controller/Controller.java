package com.hellFire.SignUp_SignIn.controller;

import com.hellFire.SignUp_SignIn.entity.Users;
import com.hellFire.SignUp_SignIn.service.Userservice;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class Controller {


    private final Userservice userservice;

    @Autowired
    public Controller(Userservice userservice){
        this.userservice=userservice;
    }



    @GetMapping("/")
    public String sayHello(HttpServletRequest request){
        return "Welcome to HomePage "+request.getSession().getId();
    }

//    @GetMapping("/signin")
//    public String signin(){
//        return  "SignIn page please provide credentials";
//    }
//
//    @GetMapping("/signup")
//    public  String signup(){
//        return "welcome to signup page. Wanna SignUp!!";
//    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable int id) {
        return userservice.getUser(id)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK)) // If user is found, return with 200 OK
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // If not found, return 404
    }

    @PostMapping("/addUser")
    public ResponseEntity<?> addUser(@RequestBody Users user) {
        if (userservice.isUsernameAvailable(user.getUsername())) {
            Users savedUser = userservice.addUser(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED); // Return 201 Created with the saved user
        } else {
            return new ResponseEntity<>("Username is already taken.", HttpStatus.CONFLICT); // Return 409 Conflict
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable int id){
        if(userservice.deleteUser(id)){
            return new ResponseEntity<>("Deleted Successfully",HttpStatus.OK);
        }
        return  new ResponseEntity<>("User not found",HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody Users user) {
        // Check if the user exists
        Optional<Users> existingUserOptional = userservice.getUser(user.getId());

        if (existingUserOptional.isPresent()) {
            // Get the existing user
            Users existingUser = existingUserOptional.get();

            // Update fields selectively (only if provided in the request)
            if (user.getUsername() != null && !user.getUsername().isEmpty()) {
                existingUser.setUsername(user.getUsername());
            }
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(user.getPassword());
            }

            // Save the updated user
            Users updatedUser = userservice.updateUser(existingUser);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK); // Return the updated user
        } else {
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND); // User not found
        }
    }



}
