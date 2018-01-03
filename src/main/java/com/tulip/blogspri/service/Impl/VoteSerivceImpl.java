package com.tulip.blogspri.service.Impl;

import com.tulip.blogspri.domain.Vote;
import com.tulip.blogspri.repository.VoteRepository;
import com.tulip.blogspri.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteSerivceImpl implements VoteService{

    @Autowired
    private VoteRepository voteRepository;

    @Override
    public Vote getVoteById(Long id) {
        return voteRepository.findOne(id);
    }

    @Override
    public void removeVote(Long id) {
        voteRepository.delete(id);
    }
}
