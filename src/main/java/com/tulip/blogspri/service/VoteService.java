package com.tulip.blogspri.service;

import com.tulip.blogspri.domain.Vote;

public interface VoteService {

    Vote getVoteById(Long id);

    void removeVote(Long id);
}
