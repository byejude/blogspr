package com.tulip.blogspri.service.Impl;


import com.tulip.blogspri.domain.*;
import com.tulip.blogspri.repository.BlogRepository;
import com.tulip.blogspri.repository.EsBlogRepository;
import com.tulip.blogspri.service.BlogService;
import com.tulip.blogspri.service.EsBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class BlogServiceImpl implements BlogService{

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private EsBlogService esBlogService;

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        boolean isNew = (blog.getId() == null);
        EsBlog esBlog = null;

        Blog returnBlog = blogRepository.save(blog);

        if(isNew){
            esBlog = new EsBlog(returnBlog);
        }else{
              esBlog = esBlogService.getEsBlogByBlogId(blog.getId());
        }
        esBlogService.updateEsBlog(esBlog);
        return returnBlog;
    }

    @Transactional
    @Override
    public void removeBlog(Long id) {
        blogRepository.delete(id);
        EsBlog esBlog = esBlogService.getEsBlogByBlogId(id);
        esBlogService.removeEsBlog(esBlog.getId());
    }

    @Override
    public Blog getBlogById(Long id) {
        return blogRepository.findOne(id);
    }

    @Override
    public Page<Blog> listBlogByTitleVote(User user, String title, Pageable pageable) {
        title = "%"+title+"%";
        String tags = title;
        Page<Blog> blogs = blogRepository.findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(title,user, tags,user, pageable);
        return blogs;
    }

    @Override
    public Page<Blog> listBlogsByTitleVoteAndSort(User user, String title, Pageable pageable) {

        title = "%"+title+"%";
        Page<Blog>blogs = blogRepository.findByUserAndTitleLike(user, title, pageable);
        return blogs;
    }

    @Override
    public Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable) {
        Page<Blog> blogs = blogRepository.findByCatalog(catalog,pageable);
        return blogs;
    }


    @Override
    public void readingIncrease(Long id) {
        Blog blog = blogRepository.findOne(id);
        blog.setReadSize(blog.getReadSize()+1);
        saveBlog(blog);
    }

    @Override
    public Blog creatComment(Long blogId, String commentContent) {
        Blog blog = blogRepository.findOne(blogId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = new Comment(user,commentContent);
        blog.addComment(comment);

        return this.saveBlog(blog);
    }

    @Override
    public void removeComment(Long blogId, Long commentId) {
        Blog blog = blogRepository.findOne(blogId);
        blog.removeComment(commentId);
        saveBlog(blog);
    }

    @Override
    public Blog createVote(Long blogId) throws Exception{
        Blog blog = blogRepository.findOne(blogId);
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Vote vote = new Vote(user);
        boolean isExist =blog.addVote(vote);
        if(isExist){
            throw new IllegalAccessException("此用户已点过赞了");
        }
        return saveBlog(blog);
    }

    @Override
    public void removeVote(Long blogId, Long voteId) {
      Blog blog = blogRepository.findOne(blogId);
      blog.removeVote(voteId);
      saveBlog(blog);

    }
}