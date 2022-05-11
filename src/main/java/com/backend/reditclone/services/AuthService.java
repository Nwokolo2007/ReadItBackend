package com.backend.reditclone.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.reditclone.dto.AuthenticationResponse;
import com.backend.reditclone.dto.LoginRequest;
import com.backend.reditclone.dto.RefreshTokenRequest;
import com.backend.reditclone.dto.RegisterRequest;
import com.backend.reditclone.exceptions.SpringRedditException;
import com.backend.reditclone.models.NotificationEmail;
import com.backend.reditclone.models.User;
import com.backend.reditclone.models.VerificationToken;
import com.backend.reditclone.repository.UserRepository;
import com.backend.reditclone.repository.VerificationTokenRepository;
import com.backend.reditclone.security.JwtProvider;
import com.sun.security.auth.UserPrincipal;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
@Transactional
public class AuthService {
	
	   private final PasswordEncoder passwordEncoder;
	    private final UserRepository userRepository;
	    private final VerificationTokenRepository verificationTokenRepository;
	   private final MailService mailService;
	    private final AuthenticationManager authenticationManager;
	    private final JwtProvider jwtProvider;
	    private final RefreshTokenService refreshTokenService;
	
	public void signup(RegisterRequest registerRequest) throws SpringRedditException
	{
		User user =  new User();
		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setCreated(Instant.now());
		user.setEnabled(false);
		
		userRepository.save(user);
		
		String token = generateVerificationToken(user);
		mailService.sendMail(new NotificationEmail("Please Activate your Account",
               user.getEmail(), "Thank you for signing up to Spring Reddit, " +
            "please click on the below url to activate your account : " +
               "http://localhost:8080/api/auth/accountVerification/" + token));
	}
	
	 @Transactional(readOnly = true)
	    public User getCurrentUser() {
		 UserPrincipal principal = (UserPrincipal)SecurityContextHolder.
	                getContext().getAuthentication().getPrincipal();
	        return userRepository.findByUsername(principal.getName())
	                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getName()));
	    }

	
	private String generateVerificationToken(User user)
	{
		String token =  UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(user);
		
		verificationTokenRepository.save(verificationToken);
		return token;
		
	}
	
	
	   private void fetchUserAndEnable(VerificationToken verificationToken) throws SpringRedditException {
	        String username = verificationToken.getUser().getUsername();
	        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringRedditException("User not found with name - " + username));
	        user.setEnabled(true);
	        userRepository.save(user);
	    }
	
    public void verifyAccount(String token) throws SpringRedditException {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        fetchUserAndEnable(verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token")));
    }
	
	

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginRequest.getUsername())
                .build();
    }
	
	
	public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) throws SpringRedditException
	{
		refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
		String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
		
		return AuthenticationResponse.builder()
				.authenticationToken(token)
				.refreshToken(refreshTokenRequest.getRefreshToken())
				.expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
				.username(refreshTokenRequest.getUsername())
				.build();
	}
	
	public boolean isLoggedIn() 
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		return !(authentication instanceof
				AnonymousAuthenticationToken) && authentication.isAuthenticated();
	}
}
