package com.backend.reditclone.services;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.backend.reditclone.dto.CommentsDto;
import com.backend.reditclone.exceptions.PostNotFoundException;
import com.backend.reditclone.exceptions.SpringRedditException;
import com.backend.reditclone.mapper.CommentMapper;
import com.backend.reditclone.models.Comment;
import com.backend.reditclone.models.NotificationEmail;
import com.backend.reditclone.models.Post;
import com.backend.reditclone.models.User;
import com.backend.reditclone.repository.CommentRepository;
import com.backend.reditclone.repository.PostRepository;
import com.backend.reditclone.repository.UserRepository;

import static java.util.stream.Collectors.toList;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentService {
    private static final String POST_URL = "";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void save(CommentsDto commentsDto) throws SpringRedditException {
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
        Comment comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());
        
        
        commentRepository.save(comment);

        String message = mailContentBuilder.build(post.getUser().getUsername() + " posted a comment on your post." + POST_URL);
        sendCommentNotification(message, post.getUser());
    }

    private void sendCommentNotification(String message, User user) throws SpringRedditException {
        mailService.sendMail(new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(), message));
    }

    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto).collect(toList());
    }

    public List<CommentsDto> getAllCommentsForUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }

    public boolean containsSwearWords(String comment) throws SpringRedditException {
        if (comment.contains("shit")) {
            throw new SpringRedditException("Comments contains unacceptable language");
        }
        return false;
    }
}