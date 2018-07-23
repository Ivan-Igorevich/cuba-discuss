package ru.iovchinnikov.talks.web.testentity;

import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.security.global.UserSession;
import ru.iovchinnikov.talks.entity.TestEntity;
import ru.iovchinnikov.talks.web.comment.CommentBrowse;

import javax.inject.Inject;

public class TestEntityEdit extends AbstractEditor<TestEntity> {
    @Inject
    private CommentBrowse comments;

    @Inject
    private Metadata metadata;

    @Inject
    private UserSession userSession;

    @Override
    public void ready() {
        super.ready();
        String dbName = metadata.getSession().getClassNN(getItem().getClass()).getName();
        comments.initialize(false)
                .setFrameVisible(true)
                .setCurrentUser(userSession.getUser())
                .setCurrentEntityId(getItem().getId())
                .setCurrentEntityName(dbName)
                .applyAndShow();
        super.ready();
    }
}