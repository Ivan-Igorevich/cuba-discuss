package ru.iovchinnikov.talks.web.comment;

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
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
    private static final int STATE_EDIT = 3;

    private User user;
    private UUID entity;
    private String entityName;
    private Comment parent;
    private int state;

    @Inject private UserSession userSession;
    @Inject private Button btnClose;
    @Inject private Frame windowActions;
    @Inject private LinkButton lbtnParent;
    @Inject private Label lblParent;
    @Named("fieldGroup.contents") private ResizableTextArea contents;
    @Named("fieldGroup.timestamp") private DateField timestamp;


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
        } else {
            state = STATE_REPLY;
            parent = (Comment) params.get("parent");
        }
    }

    @Override
    public void ready() {
        if (getItem().getAuthor() != null && getItem().getAuthor().getId().equals(userSession.getUser().getId()))
            state = STATE_EDIT;
        if (state == STATE_CREATE) {
            getItem().setAuthor(user);
            getItem().setEntity(entity);
            getItem().setEntityName(entityName);
            getItem().setHasAnswer(false);
            btnClose.setVisible(false);
            lblParent.setVisible(false);
            timestamp.setVisible(false);
        } else if (state == STATE_VIEW || state == STATE_EDIT) {
            contents.setEditable(state == STATE_EDIT);
            timestamp.setVisible(true);
            if (getItem().getParent() == null) {
                lblParent.setVisible(false);
                lbtnParent.setVisible(false);
            } else {
                lbtnParent.setCaption(String.format("%s, %s", getItem().getParent().getUpdateTs(), getItem().getParent().getAuthor().getName()));
                lbtnParent.setVisible(true);
            }
            windowActions.setVisible(state == STATE_EDIT);
            btnClose.setVisible(state == STATE_VIEW);
        } else if (state == STATE_REPLY) {
            getItem().setParent(parent);
            lbtnParent.setCaption(String.format("%s, %s", parent.getUpdateTs(), parent.getAuthor().getName()));
            lbtnParent.setVisible(true);
            contents.setEditable(true);
            btnClose.setVisible(false);
        }

        super.ready();
    }

    @Override
    protected boolean preCommit() {
        // checking if the created comment is empty
        if (contents.getRawValue() == null || "".equals(contents.getRawValue())) {
            showNotification(getMessage("emptyComment"));
            return false;
        } else
            return super.preCommit();
    }

    @Override
    protected boolean preClose(String actionId) {
        close(actionId, true);
        return true;
    }

    public void btnCloseClick() {
        close("CLOSE_ID");
    }

    public void btnParentClick() {
        openEditor(getItem().getParent(), WindowManager.OpenType.DIALOG);
    }
}