package ru.iovchinnikov.talks.web.comment;

import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.security.entity.User;
import ru.iovchinnikov.talks.entity.Comment;

import java.util.Map;
import java.util.UUID;

public class CommentEdit extends AbstractEditor<Comment> {
    private User user;
    private UUID entity;
    private String entityName;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        user = (User) params.get("user");
        entity = (UUID) params.get("entity");
        entityName = (String) params.get("eName");
    }

    @Override
    public void ready() {
        getItem().setAuthor(user);
        getItem().setEntity(entity);
        getItem().setEntityName(entityName);
        super.ready();
    }
}