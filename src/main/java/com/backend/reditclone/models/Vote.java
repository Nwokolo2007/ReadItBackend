package com.backend.reditclone.models;

import javax.persistence.*;

import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Vote {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long voteId;
	private VoteType voteType;
	@NotNull
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "postId",
	referencedColumnName = "postId")
	private Post post;
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "userid",
	referencedColumnName = "userId")
	private User user;
	
}