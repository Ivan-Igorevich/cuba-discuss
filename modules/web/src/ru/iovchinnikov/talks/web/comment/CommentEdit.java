package ru.iovchinnikov.talks.web.comment;

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.security.entity.User;
import com.sun.javafx.binding.StringFormatter;
import ru.iovchinnikov.talks.entity.Comment;

import javax.inject.Inject;
import javax.inject.Named;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class CommentEdit extends AbstractEditor<Comment> {
    private static final int STATE_CREATE = 0;
    private static final int STATE_VIEW = 1;
    private static final int STATE_REPLY = 2;

    private User user;
    private UUID entity;
    private String entityName;
    private Date timestamp;
    private Comment parent;
    private int state;

    @Inject private Button btnClose;
    @Inject private Frame windowActions;
    @Inject private LinkButton lbtnParent;
    @Inject private Label lblParent;
    @Named("fieldGroup.contents") private ResizableTextArea contentsField;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        if (params.size() == 1) {
            state = STATE_VIEW;
        } else if (params.get("parent") == null) {
            state = STATE_CREATE;
            user = (User) params.get("user");
            entity = (UUID) params.get("entity");
            entityName = (String) params.get("eName");
            timestamp = (Date) params.get("ts");
        } else {
            state = STATE_REPLY;
            parent = (Comment) params.get("parent");
        }
    }

    @Override
    public void ready() {
        if (state == STATE_CREATE) {
            getItem().setAuthor(user);
            getItem().setEntity(entity);
            getItem().setEntityName(entityName);
            getItem().setDate(timestamp);
            getItem().setHasAnswer(false);
            btnClose.setVisible(false);
            lblParent.setVisible(false);
        } else if (state == STATE_VIEW) {
            contentsField.setEditable(false);
            if (getItem().getParent() == null) {
                lblParent.setVisible(false);
                lbtnParent.setVisible(false);
            } else {
                lbtnParent.setCaption(String.format("%s, %s", getItem().getParent().getDate(), getItem().getParent().getAuthor().getName()));
                lbtnParent.setVisible(true);
            }
            windowActions.setVisible(false);
        } else if (state == STATE_REPLY) {
            getItem().setParent(parent);
            lbtnParent.setCaption(String.format("%s, %s", parent.getDate(), parent.getAuthor().getName()));
            lbtnParent.setVisible(true);
            contentsField.setEditable(true);
            btnClose.setVisible(false);
        }

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

    public void btnCloseClick() {
        close("CLOSE_ID");
    }

    public void btnParentClick() {
        openEditor(getItem().getParent(), WindowManager.OpenType.DIALOG);
    }
}