package com.tulip.blogspri.repository;

import com.tulip.blogspri.domain.Blog;
import com.tulip.blogspri.domain.Catalog;
import com.tulip.blogspri.domain.User;
import org.hibernate.annotations.CascadeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface BlogRepository extends CrudRepository<Blog,Long> {

    @Deprecated
    Page<Blog> findByUserAndTitleLikeOrderByCreateTimeDesc(User user, String title, Pageable pageable);

    Page<Blog> findByUserAndtAndTitleLike(User user,String title,Pageable pageable);

    Page<Blog> findByTitleLikeAndUserOrTagsLikeUserOrderByCreateTimeDesc(String title,User user,String tags,User user2,Pageable pageable);

    Page<Blog> findByCatalog(Catalog catalog,Pageable pageable);
}
