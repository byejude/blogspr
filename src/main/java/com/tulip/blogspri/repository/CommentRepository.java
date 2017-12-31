package com.tulip.blogspri.repository;

import com.tulip.blogspri.domain.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment,Long> {
}
