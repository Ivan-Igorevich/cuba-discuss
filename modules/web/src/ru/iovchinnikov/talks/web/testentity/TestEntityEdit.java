package ru.iovchinnikov.talks.web.testentity;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.security.global.UserSession;
import ru.iovchinnikov.talks.entity.TestEntity;
import ru.iovchinnikov.talks.web.comment.CommentBrowse;

import javax.inject.Inject;

public class TestEntityEdit extends AbstractEditor<TestEntity> {
    @Inject private CommentBrowse comments;
    @Inject private UserSession userSession;
    @Inject private Metadata metadata;

    private boolean isNew = false;

    @Override
    protected void initNewItem(TestEntity item) {
        isNew = true;
        super.initNewItem(item);
    }

    @Override
    public void ready() {
        String dbName = metadata.getSession().getClassNN(getItem().getClass()).getName();
        if (!isNew)
            comments.initialize()
                    .setCurrentUser(userSession.getUser())
                    .setCurrentEntityId(getItem().getId())
                    .setCurrentEntityName(dbName)
                    .applyParameters();
        else
            comments.initialize()
                    .setFrameVisible(false)
                    .setCurrentUser(userSession.getUser())
                    .setCurrentEntityId(getItem().getId())
                    .setCurrentEntityName(dbName)
                    .applyParameters();

        super.ready();
    }
}