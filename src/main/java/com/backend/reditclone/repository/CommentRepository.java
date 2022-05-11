package com.backend.reditclone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.reditclone.models.Comment;
import com.backend.reditclone.models.Post;
import com.backend.reditclone.models.User;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

	List<Comment>findByPost(Post post);
	
	List<Comment>findAllByUser(User user);
	
}
