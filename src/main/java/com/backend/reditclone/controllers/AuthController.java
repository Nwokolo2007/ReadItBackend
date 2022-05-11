package com.backend.reditclone.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.reditclone.dto.AuthenticationResponse;
import com.backend.reditclone.dto.LoginRequest;
import com.backend.reditclone.dto.RefreshTokenRequest;
import com.backend.reditclone.dto.RegisterRequest;
import com.backend.reditclone.exceptions.SpringRedditException;
import com.backend.reditclone.services.AuthService;
import com.backend.reditclone.services.RefreshTokenService;

import lombok.AllArgsConstructor;

import static org.springframework.http.HttpStatus.OK;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final RefreshTokenService refreshTokenService;
	
	@PostMapping("/signup")
	public ResponseEntity<String>signup(@RequestBody RegisterRequest registerRequest)
	{
		try {
			authService.signup(registerRequest);
		} catch (SpringRedditException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<>("User Registration Successful", OK);
	}
	
	@GetMapping("accountVerification/{token}")
	public ResponseEntity<String>verifyAccount(@PathVariable String token)
	{
		try {
			authService.verifyAccount(token);
		} catch (SpringRedditException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new ResponseEntity<>("Account Activated Successfully", OK);
	}
	
	@PostMapping("/login")
	public AuthenticationResponse login(@RequestBody LoginRequest loginRequest)
	{
		
		return authService.login(loginRequest);
	}
	
	
	@PostMapping("/refresh/token")
	public AuthenticationResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) throws SpringRedditException
	{
		return authService.refreshToken(refreshTokenRequest);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<String>logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest)
	{
		refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
		return ResponseEntity.status(OK).body("Refresh Token Deleted Successfully!!");
	}
	
	
}
