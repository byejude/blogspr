package com.tulip.blogspri.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;

import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.sql.Timestamp;

@Document(indexName = "blog",type="blog")
@XmlRootElement //mediaTypez to xml
@NoArgsConstructor
@Data
public class EsBlog implements Serializable {

    private static final long serialVersionUID= 1L;

    @Id
    private String id;

    @Field(index = FieldIndex.not_analyzed)
    private Long blogId;

    private String title;

    private String summary;

    private String content;

    @Field(index = FieldIndex.not_analyzed)
    private String username;

    @Field(index = FieldIndex.not_analyzed)
    private String avatar;

    @Field(index = FieldIndex.not_analyzed)
    private Timestamp createTime;

    @Field(index = FieldIndex.not_analyzed)
    private Integer readSize = 0;

    @Field(index = FieldIndex.not_analyzed)
    private Integer commentSize = 0;

    @Field(index = FieldIndex.not_analyzed)
    private Integer voteSize = 0;

    private String tags;

    public EsBlog(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public EsBlog(Long blogId, String title, String summary, String content, String username, String avatar,Timestamp createTime,
                  Integer readSize,Integer commentSize, Integer voteSize , String tags) {
        this.blogId = blogId;
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.username = username;
        this.avatar = avatar;
        this.createTime = createTime;
        this.readSize = readSize;
        this.commentSize = commentSize;
        this.voteSize = voteSize;
        this.tags = tags;
    }

    public EsBlog(Blog blog){
        this.blogId = blog.getId();
        this.title = blog.getTitle();
        this.summary = blog.getSummary();
        this.content = blog.getContent();
        this.username = blog.getUser().getUsername();
        this.avatar = blog.getUser().getAvatar();
        this.createTime = blog.getCreateTime();
        this.readSize = blog.getReadSize();
        this.commentSize = blog.getCommentSize();
        this.voteSize = blog.getVoteSize();
        this.tags = blog.getTags();
    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%d, title='%s', content='%s']",
                blogId, title, content);
    }
}
