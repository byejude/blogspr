package com.tulip.blogspri.service;

import com.tulip.blogspri.domain.EsBlog;
import com.tulip.blogspri.domain.User;
import com.tulip.blogspri.vo.TagVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EsBlogService {

    void removeEsBlog(String id);

    EsBlog updateEsBlog(EsBlog esBlog);

    EsBlog getEsBlogByBlogId(Long blogId);

    Page<EsBlog> listNewestEsBlogs(String keyword, Pageable pageable);

    Page<EsBlog> listHotestEsBlogs(String keyword,Pageable pageable);

    Page<EsBlog> listEsBlogs(Pageable pageable);

    List<TagVo> listTopTags();

    List<User> listTop12User();
}
