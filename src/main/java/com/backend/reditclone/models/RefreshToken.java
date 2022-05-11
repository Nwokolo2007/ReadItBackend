package com.backend.reditclone.models;

import java.time.Instant;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor

public class RefreshToken {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	private String token;
	private Instant createdDate;

}
