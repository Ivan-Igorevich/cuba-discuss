package ru.iovchinnikov.talks.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;

@NamePattern("%s|moderationType")
@Table(name = "DISCUSS_COMMENT_PREFERENCE")
@Entity(name = "discuss$CommentPreference")
public class CommentPreference extends StandardEntity {
    private static final long serialVersionUID = -5413571300242877988L;

    @Column(name = "MODERATION_TYPE")
    protected String moderationType;

    public CommentPreferenceType getModerationType() {
        return moderationType == null ? null : CommentPreferenceType.fromId(moderationType);
    }

    public void setModerationType(CommentPreferenceType moderationType) {
        this.moderationType = moderationType == null ? null : moderationType.getId();
    }



}