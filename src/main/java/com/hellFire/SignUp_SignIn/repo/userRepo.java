package com.hellFire.SignUp_SignIn.repo;

import com.hellFire.SignUp_SignIn.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface userRepo extends JpaRepository<Users,Integer> {
    Users findByUsername(String username);
}
