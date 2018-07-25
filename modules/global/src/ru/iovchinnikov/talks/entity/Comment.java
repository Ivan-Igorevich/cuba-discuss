package ru.iovchinnikov.talks.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.haulmont.cuba.security.entity.User;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.Listeners;

@Listeners("discuss_CommentListener")
@NamePattern(" %s|author")
@Table(name = "DISCUSS_COMMENT")
@Entity(name = "discuss$Comment")
public class Comment extends StandardEntity {
    private static final long serialVersionUID = 8893336517780968916L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    protected Comment parent;

    @Column(name = "HAS_ANSWER")
    protected Boolean hasAnswer;

    @Lob
    @Column(name = "CONTENTS")
    protected String contents;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "AUTHOR_ID")
    protected User author;

    @Column(name = "ENTITY")
    protected UUID entity;

    @Column(name = "ENTITY_NAME")
    protected String entityName;


    @Column(name = "COMMENT_STATUS")
    protected String commentStatus;

    public CommentStatus getCommentStatus() {
        return commentStatus == null ? null : CommentStatus.fromId(commentStatus);
    }

    public void setCommentStatus(CommentStatus commentStatus) {
        this.commentStatus = commentStatus == null ? null : commentStatus.getId();
    }

    public void setHasAnswer(Boolean hasAnswer) {
        this.hasAnswer = hasAnswer;
    }

    public Boolean getHasAnswer() {
        return hasAnswer;
    }


    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
    }

    public Comment getParent() {
        return parent;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getAuthor() {
        return author;
    }

    public void setEntity(UUID entity) {
        this.entity = entity;
    }

    public UUID getEntity() {
        return entity;
    }

}
