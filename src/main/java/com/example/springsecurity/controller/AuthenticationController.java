package com.example.springsecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springsecurity.dto.JwtAuthenticationResponse;
import com.example.springsecurity.dto.RefreshTokenRequest;
import com.example.springsecurity.dto.SigninRequest;
import com.example.springsecurity.dto.SignupRequest;
import com.example.springsecurity.entities.User;
import com.example.springsecurity.services.AuthenticationService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
	
	@Autowired
	private AuthenticationService authenticationService;

	@PostMapping("/signup")
	public ResponseEntity<User> signup( @RequestBody SignupRequest signupRequest){
		System.out.println("SignupReq: "+ signupRequest.getEmail()+signupRequest.getPassword());
		return new ResponseEntity<>(authenticationService.signup(signupRequest) , HttpStatus.OK);
	}
	
	@PostMapping("/signin")
	public ResponseEntity<JwtAuthenticationResponse> signin( @RequestBody SigninRequest signinRequest){
		return new ResponseEntity<JwtAuthenticationResponse>(authenticationService.signin(signinRequest), HttpStatus.OK);
	}
	
	@PostMapping("/refreshToken")
	public ResponseEntity<JwtAuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
		return new ResponseEntity<>(authenticationService.refreshToken(refreshTokenRequest), HttpStatus.OK);
	}
	
}
