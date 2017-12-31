package com.tulip.blogspri.service;

import com.sun.org.apache.xml.internal.resolver.Catalog;
import com.tulip.blogspri.domain.Blog;
import com.tulip.blogspri.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlogService {

    Blog saveBlog(Blog blog);

    void removeBlog(Long id);

    Blog getBlogById(Long id);

    Page<Blog> listBlogByTitleVote(User user, String title, Pageable pageable);

    Page<Blog> listBlogsByTitleVoteAndSort(User user,String title,Pageable pageable);

    Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable);

    Page<Blog> listBlogByCatalog(Catalog catalog,Pageable pageable);

    void readingIncrease(Long id);

    Blog creatComment(Long blogId,String commentContent);

    void removeComment(Long blogId,Long commentId);

    Blog createVote(Long blogId);

    void removeVote(Long blogId,Long voteId);
}
