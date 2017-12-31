package com.tulip.blogspri.repository;

import com.tulip.blogspri.domain.Vote;
import org.springframework.data.repository.CrudRepository;

public interface VoteRepository extends CrudRepository<Vote,Long> {
}
