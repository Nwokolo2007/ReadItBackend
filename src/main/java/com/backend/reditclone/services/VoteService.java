package com.backend.reditclone.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.reditclone.dto.VoteDto;
import com.backend.reditclone.exceptions.PostNotFoundException;
import com.backend.reditclone.models.Post;
import com.backend.reditclone.models.Vote;
import com.backend.reditclone.repository.PostRepository;
import com.backend.reditclone.repository.VoteRepository;
import static com.backend.reditclone.models.VoteType.UPVOTE;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VoteService {
	
	private final VoteRepository voteRepository;
	private final PostRepository postRepository;
	private final AuthService authService;
	
	@Transactional
	public void vote(VoteDto voteDto)
	{
		Post post = postRepository.findById(voteDto.getPostId())
				.orElseThrow(()-> new PostNotFoundException("Post Not Found with ID -" + voteDto.getPostId()));
		
		Optional<Vote>voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
		
		if(UPVOTE.equals(voteDto.getVoteType()))
		{
			post.setVoteCount(post.getVoteCount() +1);
		}
		else
		{
			post.setVoteCount(post.getVoteCount() - 1);
		}
		
		voteRepository.save(mapToVote(voteDto, post));
		postRepository.save(post);
		
	}
	
	private Vote mapToVote(VoteDto voteDto, Post post)
	{
		return Vote.builder()
				.voteType(voteDto.getVoteType())
				.post(post)
				.user(authService.getCurrentUser())
				.build();
	}

}
