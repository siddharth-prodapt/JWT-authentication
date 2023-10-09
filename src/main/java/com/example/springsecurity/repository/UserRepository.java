package com.example.springsecurity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springsecurity.entities.Role;
import com.example.springsecurity.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
 
	Optional<User> findByEmail(String email);
	User findByRole(Role role);
}
