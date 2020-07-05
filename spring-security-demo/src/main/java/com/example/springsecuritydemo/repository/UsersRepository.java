package com.example.springsecuritydemo.repository;

import com.example.springsecuritydemo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Users getByUsername(String s);
}
