package com.backend.reditclone.controllers;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.reditclone.dto.CommentsDto;
import com.backend.reditclone.exceptions.SpringRedditException;
import com.backend.reditclone.services.CommentService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/comments/")
@AllArgsConstructor
public class CommentsController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentsDto commentsDto) {
        try {
			commentService.save(commentsDto);
		} catch (SpringRedditException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return new ResponseEntity<>(CREATED);
    }

    @GetMapping("/by-post/{postId}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@PathVariable Long postId) {
        return ResponseEntity.status(OK)
                .body(commentService.getAllCommentsForPost(postId));
    }

    @GetMapping("/by-user/{userName}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForUser(@PathVariable String userName){
        return ResponseEntity.status(OK)
                .body(commentService.getAllCommentsForUser(userName));
    }

}
