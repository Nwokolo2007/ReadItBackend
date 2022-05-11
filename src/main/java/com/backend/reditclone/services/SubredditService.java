package com.backend.reditclone.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.reditclone.dto.SubredditDto;
import com.backend.reditclone.exceptions.SpringRedditException;
import com.backend.reditclone.mapper.SubredditMapper;
import com.backend.reditclone.models.Subreddit;
import com.backend.reditclone.repository.SubredditRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

	private final SubredditRepository subredditRepository;
	private final SubredditMapper subredditMapper;
	
	@Transactional
	public SubredditDto save(SubredditDto subredditDto)
	{
		Subreddit save = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
		subredditDto.setId(save.getId());
		
		return subredditDto;
	}
	
	
	@Transactional(readOnly = true)
	public List<SubredditDto>getAll()
	{
		return subredditRepository.findAll()
				.stream()
				.map(subredditMapper:: mapSubredditToDto)
				.collect(toList());
	}
	
	public SubredditDto getSubreddit(Long id)
	{
		Subreddit subreddit = null;
		try {
			subreddit = subredditRepository.findById(id)
					.orElseThrow(() -> new SpringRedditException("No subreddit found with ID - "+ id));
		} catch (SpringRedditException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return subredditMapper.mapSubredditToDto(subreddit);
	}
}
