package com.backend.reditclone.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.reditclone.models.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	Optional<RefreshToken>findByToken(String token);
	
	void deleteByToken(String token);
}
