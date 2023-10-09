package com.example.springsecurity.services.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.springsecurity.dto.JwtAuthenticationResponse;
import com.example.springsecurity.dto.RefreshTokenRequest;
import com.example.springsecurity.dto.SigninRequest;
import com.example.springsecurity.dto.SignupRequest;
import com.example.springsecurity.entities.Role;
import com.example.springsecurity.entities.User;
import com.example.springsecurity.repository.UserRepository;
import com.example.springsecurity.services.AuthenticationService;
import com.example.springsecurity.services.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
	@Autowired
	private UserRepository userRepository;
	
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	public User signup(SignupRequest signupRequest) {
		User user = new User();
		user.setEmail(signupRequest.getEmail());
		user.setFirstname(signupRequest.getFirstname());
		user.setSecondname(signupRequest.getLastname());
		user.setRole(Role.USER);
		user.setPassword( new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
		
		return userRepository.save(user);
	}
	
	public JwtAuthenticationResponse signin( SigninRequest signinRequest) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						signinRequest.getEmail(), signinRequest.getPassword()));
		
		User user = userRepository.findByEmail(signinRequest.getEmail()).orElseThrow(()-> new IllegalArgumentException("Invalid Email ID"));
		String jwtToken = jwtService.generateToken(user);
		String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
		
		JwtAuthenticationResponse jwtResponse = new JwtAuthenticationResponse();
		
		jwtResponse.setEmail(signinRequest.getEmail());
		jwtResponse.setToken(jwtToken);
		jwtResponse.setRefreshToken(refreshToken);
		
		return jwtResponse;
	}
	
	public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
		String userEmail = jwtService.extractUserName(refreshTokenRequest.getToken());
		User user = userRepository.findByEmail(userEmail).orElseThrow( ()-> new UsernameNotFoundException("Email not registered") );
		
		if( jwtService.isTokenValid( refreshTokenRequest.getToken(), user)) {
			String jwtToken = jwtService.generateToken(user);
			
			JwtAuthenticationResponse jwtResponse = new JwtAuthenticationResponse();
			
			jwtResponse.setEmail(userEmail);
			jwtResponse.setToken(jwtToken);
			jwtResponse.setRefreshToken( refreshTokenRequest.getToken());
			
			return jwtResponse;
			
		}
		return null;
	}
}
