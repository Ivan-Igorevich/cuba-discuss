package ru.iovchinnikov.talks.web.comment;

import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.CreateAction;
import com.haulmont.cuba.gui.components.actions.EditAction;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import ru.iovchinnikov.talks.entity.Comment;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommentBrowse extends AbstractLookup {

    @Inject private GroupDatasource<Comment, UUID> commentsDs;
    @Inject private TimeSource timeSource;
    @Inject private GroupTable<Comment> commentsTable;
    @Inject private UserSession userSession;
    @Inject private Metadata metadata;
    @Named("commentsTable.create") private CreateAction commentsTableCreate;
    @Named("commentsTable.edit") private EditAction commentsTableEdit;
    private Initializer parentInfo;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        // doesn't work in initializer's methods
        commentsTable.addAction(new RemoveAction(commentsTable) {
            @Override
            protected boolean isApplicable() {
                // super implementation checks that target != null and target has selected items,
                boolean applicable = super.isApplicable();
                    if (applicable) {
                        for(Comment c : commentsTable.getSelected()) {
                            if (!(c.getAuthor().getLogin().equals(userSession.getCurrentOrSubstitutedUser().getLogin()))) {
                                applicable = false;
                            }
                        }
                    }
                return applicable;
            }
        });
    }

    /**
     * Preferred initializer
     */
    public Initializer initialize(boolean isNew) {
        this.parentInfo = new Initializer(this);
        if (isNew) parentInfo.setFrameVisible(false);
        return this.parentInfo;
    }

    public class Initializer {
        private CommentBrowse screen;
        private User currentUser;
        private String entityName;
        private UUID currentEntity;

        private Initializer(CommentBrowse parent) {
            this.screen = parent;
        }

        public Initializer setFrameVisible(boolean visible) {
            screen.setVisible(visible);
            return this;
        }

        public Initializer setCurrentUser(User user) {
            currentUser = user;
            return this;
        }

        public Initializer setCurrentEntityName(String name) {
            entityName = name;
            return this;
        }

        public Initializer setCurrentEntityId(UUID uid) {
            currentEntity = uid;
            return this;
        }

        /**
         * This method should be invoked to initialize the frame with current parameters,
         * and send parameters to 'create' action.
         */
        public void applyParameters() {
            Map<String, Object> params = new HashMap<>();
            params.put("user", parentInfo.currentUser);
            params.put("entity", parentInfo.currentEntity);
            params.put("eName", parentInfo.entityName);
            params.put("ts", timeSource.currentTimestamp());
            commentsTableCreate.setWindowParams(params);
            commentsTableEdit.setCaption(getMessage("editView"));
            commentsDs.setQuery("SELECT e " +
                                "FROM discuss$Comment e " +
                                "WHERE e.entity " +
                                "IN (SELECT n.id " +
                                    "FROM " + parentInfo.entityName + " n " +
                                    "WHERE n.id = '" + currentEntity + "') " +
                                "ORDER BY e.date DESC");
            commentsDs.refresh();
        }
    }

    public void onBtnReplyClick() {
        Comment currentComment = commentsTable.getSingleSelected();
        if (currentComment == null) {
            showNotification(getMessage("selectComment"));
            return;
        }
        Comment newComment = metadata.create(Comment.class);
        newComment.setDate(timeSource.currentTimestamp());
        newComment.setAuthor(parentInfo.currentUser);
        newComment.setEntity(parentInfo.currentEntity);
        newComment.setEntityName(parentInfo.entityName);
        // move this to editor close listener
//        currentComment.setHasAnswer(true);
        Map<String, Object> param = new HashMap<>();
        param.put("parent", currentComment);
        AbstractEditor editorWindow = openEditor(newComment, WindowManager.OpenType.DIALOG, param);
        editorWindow.addCloseListener(actionId -> {
            switch (actionId) {
                case "commit":
                    currentComment.setHasAnswer(true);
                    break;
                case "windowClose":
                    break;
                default:
                    break;
            }
            commentsDs.commit();
            commentsDs.refresh();
        });
    }
}