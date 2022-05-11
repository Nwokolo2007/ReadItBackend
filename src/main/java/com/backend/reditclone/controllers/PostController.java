package com.backend.reditclone.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.reditclone.dto.PostRequest;
import com.backend.reditclone.dto.PostResponse;
import com.backend.reditclone.services.PostService;

import static org.springframework.http.ResponseEntity.status;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/posts/")
@AllArgsConstructor
public class PostController {

	private final PostService postService;
	
	@PostMapping
	public ResponseEntity<Void>createPost(@RequestBody PostRequest postRequest)
	{
		postService.save(postRequest);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	 @GetMapping
	  public ResponseEntity<List<PostResponse>> getAllPosts() {
	      return status(HttpStatus.OK).body(postService.getAllPosts());
	  }

	    @GetMapping("by-subreddit/{id}")
	    public ResponseEntity<List<PostResponse>> getPostsBySubreddit(Long id) {
	        return status(HttpStatus.OK).body(postService.getPostsBySubreddit(id));
	    }

	    @GetMapping("by-user/{name}")
	    public ResponseEntity<List<PostResponse>> getPostsByUsername(String username) {
	        return status(HttpStatus.OK).body(postService.getPostsByUsername(username));
	    }
}
