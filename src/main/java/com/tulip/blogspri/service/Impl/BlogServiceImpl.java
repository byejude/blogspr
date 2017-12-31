package com.tulip.blogspri.service.Impl;


import com.tulip.blogspri.domain.Catalog;
import com.tulip.blogspri.domain.Blog;
import com.tulip.blogspri.domain.EsBlog;
import com.tulip.blogspri.domain.User;
import com.tulip.blogspri.repository.BlogRepository;
import com.tulip.blogspri.repository.EsBlogRepository;
import com.tulip.blogspri.service.BlogService;
import com.tulip.blogspri.service.EsBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        Page<Blog> blogs = blogRepository.findByTitleLikeAndUserOrTagsLikeUserOrderByCreateTimeDesc(title,user, tags,user, pageable);
        return blogs;
    }

    @Override
    public Page<Blog> listBlogsByTitleVoteAndSort(User user, String title, Pageable pageable) {

        title = "%"+title+"%";
        Page<Blog>blogs = blogRepository.findByUserAndtAndTitleLike(user, title, pageable);
        return blogs;
    }

    @Override
    public Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable) {
        Page<Blog> blogs = blogRepository.findByCatalog(catalog,pageable);
        return null;
    }

    @Override
    public Page<Blog> listBlogByCatalog(Catalog catalog, Pageable pageable) {
        return null;
    }

    @Override
    public void readingIncrease(Long id) {

    }

    @Override
    public Blog creatComment(Long blogId, String commentContent) {
        return null;
    }

    @Override
    public void removeComment(Long blogId, Long commentId) {

    }

    @Override
    public Blog createVote(Long blogId) {
        return null;
    }

    @Override
    public void removeVote(Long blogId, Long voteId) {

    }
}