package com.backend.reditclone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.reditclone.models.Post;
import com.backend.reditclone.models.Subreddit;
import com.backend.reditclone.models.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	
	List<Post> findAllBySubreddit(Subreddit subreddit);
	
	List<Post>findByUser(User user);
	

}
