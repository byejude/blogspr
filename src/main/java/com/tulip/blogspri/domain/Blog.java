package com.tulip.blogspri.domain;

import com.github.rjeschke.txtmark.Processor;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Document(indexName = "blog",type = "bl;og")
public class Blog implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "标题不能为空")
    @Size(min = 2,max = 300)
    @Column(nullable = false,length = 50)
    private String title;

    @NotEmpty(message = "摘要不能为空")
    @Size(min =2,max = 300)
    @Column(nullable = false)
    private String summary;

    @Lob //大对象 映射mysql的Long Text类型
    @Basic(fetch = FetchType.LAZY) //懒加载 表示一个简单的属性到数据库表的字段的映射
    @NotEmpty(message = "内容不能为空")
    @Size(min =2)
    @Column(nullable = false)
    private String content;

    @Lob //大对象 映射mysql的Long Text类型
    @Basic(fetch = FetchType.LAZY) //懒加载 表示一个简单的属性到数据库表的字段的映射
    @NotEmpty(message = "内容不能为空")
    @Size(min =2)
    @Column(nullable = false)
    private String htmlContent;

    @OneToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable=false)
    @CreationTimestamp
    private Timestamp createTime;

    @Column(name = "readSize")
    private Integer readSize = 0;

    @Column(name = "commentSize")
    private Integer commentSize = 0;

    @Column(name = "voteSize")
    private Integer voteSize = 0;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "blog_comment",joinColumns = @JoinColumn(name = "blog_id",referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name="comment_id",referencedColumnName = "id"))
    private List<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "blog_vote",joinColumns = @JoinColumn(name = "blog_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="vote_id",referencedColumnName = "id"))
    private List<Vote> votes;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name="catalog_id")
    private Catalog catalog;

    @Column(name="tags", length = 100)
    private String tags;  // 标签

    public Blog(String title, String summary,String content) {
        this.title = title;
        this.summary = summary;
        this.content = content;
    }


    public void setContent(String content) {

        this.content = content;
        this.htmlContent = Processor.process(content);
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        this.commentSize = this.comments.size();
    }
}
