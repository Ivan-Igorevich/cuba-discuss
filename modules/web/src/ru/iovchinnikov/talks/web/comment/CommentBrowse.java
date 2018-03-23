package ru.iovchinnikov.talks.web.comment;

import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.actions.CreateAction;
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.haulmont.cuba.security.entity.User;
import ru.iovchinnikov.talks.entity.Comment;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommentBrowse extends AbstractLookup {

    @Inject private GroupDatasource<Comment, UUID> commentsDs;
    @Named("commentsTable.create")
    private CreateAction commentsTableCreate;
    private Initializer parentInfo;

    public Initializer initialize() {
        this.parentInfo = new Initializer(this);
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

        public void applyParameters() {
            Map<String, Object> params = new HashMap<>();
            params.put("user", parentInfo.currentUser);
            params.put("entity", parentInfo.currentEntity);
            params.put("eName", parentInfo.entityName);
            commentsTableCreate.setWindowParams(params);

            commentsDs.setQuery("SELECT e " +
                                "FROM discuss$Comment e " +
                                "WHERE e.entity " +
                                "IN (SELECT n.id " +
                                    "FROM " + parentInfo.entityName + " n " +
                                    "WHERE n.id = '" + currentEntity + "') ");
            commentsDs.refresh();
        }
    }

}