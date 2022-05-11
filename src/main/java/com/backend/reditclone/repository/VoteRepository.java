package com.backend.reditclone.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.reditclone.models.Post;
import com.backend.reditclone.models.User;
import com.backend.reditclone.models.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

	
	Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);

}
