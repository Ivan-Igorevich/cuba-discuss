package ru.iovchinnikov.talks.web.comment;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.security.entity.User;
import ru.iovchinnikov.talks.entity.Comment;

import javax.inject.Named;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class CommentEdit extends AbstractEditor<Comment> {
    private User user;
    private UUID entity;
    private String entityName;
    private Date timestamp;
    private boolean isView;
    @Named("fieldGroup.author") private PickerField authorField;
    @Named("fieldGroup.contents") private ResizableTextArea contentsField;
    @Named("fieldGroup.date") private DateField dateField;
    @Named("fieldGroup.parent") private PickerField parentField;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        if (params.get("user") == null) {
            isView = true;
        } else {
            user = (User) params.get("user");
            entity = (UUID) params.get("entity");
            entityName = (String) params.get("eName");
            timestamp = (Date) params.get("ts");
        }
    }

    @Override
    public void ready() {
        if (!isView) {
            getItem().setAuthor(user);
            getItem().setEntity(entity);
            getItem().setEntityName(entityName);
            getItem().setDate(timestamp);
            getItem().setHasAnswer(false);
        } else {
            contentsField.setEditable(false);
            parentField.setVisible(false);      // ATTENTION! visibility
        }
        authorField.setEditable(false);
        dateField.setEditable(false);
        parentField.setEditable(false);

        super.ready();
    }

    @Override
    protected boolean preCommit() {
        // checking if the created comment is empty
        if (contentsField.getRawValue() == null || "".equals(contentsField.getRawValue())) {
            showNotification(getMessage("emptyComment"));
            return false;
        } else
            return super.preCommit();
    }
}