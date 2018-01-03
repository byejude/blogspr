package com.tulip.blogspri.service.Impl;

import com.tulip.blogspri.domain.Comment;
import com.tulip.blogspri.repository.CommentRepository;
import com.tulip.blogspri.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment getCommentById(Long id) {
        return commentRepository.findOne(id);
    }

    @Override
    public void removeComment(Long id) {
        commentRepository.delete(id);
    }
}
