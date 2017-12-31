package com.tulip.blogspri.repository;

import com.tulip.blogspri.domain.EsBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

public interface EsBlogRepository extends ElasticsearchCrudRepository<EsBlog,Long> {

    Page<EsBlog> findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(String title,String Summary,String content,String tags,Pageable pageable);

    EsBlog findByBlogId(Long blogId);
}
