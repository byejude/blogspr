package com.tulip.blogspri.service;

import com.tulip.blogspri.domain.Comment;

public interface CommentService {

    Comment getCommentById(Long id);

    void removeComment(Long id);
}
