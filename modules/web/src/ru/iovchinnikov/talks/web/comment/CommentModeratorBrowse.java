package ru.iovchinnikov.talks.web.comment;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.data.GroupDatasource;
import ru.iovchinnikov.talks.entity.Comment;
import ru.iovchinnikov.talks.entity.CommentPreference;
import ru.iovchinnikov.talks.entity.CommentPreferenceType;
import ru.iovchinnikov.talks.entity.CommentStatus;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;

public class CommentModeratorBrowse extends AbstractLookup {

    @Inject
    private GroupDatasource<Comment, UUID> commentsPreDs;

    @Inject
    private GroupDatasource<Comment, UUID> commentsPostDs;

    @Inject
    private DataManager dataManager;

    @Inject
    private Button moderationTypeBtn;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        LoadContext<CommentPreference> cplc=LoadContext.create(CommentPreference.class);
        cplc.setQuery(LoadContext.createQuery("select e from discuss$CommentPreference e"));
        CommentPreference commentPreference=dataManager.load(cplc);
        if(commentPreference!=null) {
            if (commentPreference.getModerationType() == CommentPreferenceType.Postmoderation) {
                moderationTypeBtn.setCaption(messages.getMessage(CommentPreferenceType.class, "CommentPreferenceType.Postmoderation"));
            } else {
                moderationTypeBtn.setCaption(messages.getMessage(CommentPreferenceType.class, "CommentPreferenceType.Premoderation"));
            }
        }
    }

    public void onApproveBtnClick() {
        if (selected(commentsPreDs)) {
            commentsPreDs.getItem().setCommentStatus(CommentStatus.approved);
            dataManager.commit(commentsPreDs.getItem());
        }
    }

    public void onRejectBtnClick() {
        if (selected(commentsPreDs)) {
            commentsPreDs.getItem().setCommentStatus(CommentStatus.rejected);
            dataManager.commit(commentsPreDs.getItem());
        }
    }

    public void onRemoveBtnClick() {
        if (selected(commentsPostDs)) {
            commentsPostDs.getItem().setCommentStatus(CommentStatus.deleted);
            dataManager.commit(commentsPostDs.getItem());
        }
    }

    public void onRestoreBtnClick() {
        if (selected(commentsPostDs)) {
            commentsPostDs.getItem().setCommentStatus(CommentStatus.approved);
            dataManager.commit(commentsPostDs.getItem());
        }
    }

    private boolean selected(GroupDatasource groupDatasource) {
        return groupDatasource.getItem() != null;
    }

    public void onModerationTypeBtnClick() {
        LoadContext<CommentPreference> cplc=LoadContext.create(CommentPreference.class);
        cplc.setQuery(LoadContext.createQuery("select e from discuss$CommentPreference e"));
        CommentPreference commentPreference=dataManager.load(cplc);
        if(commentPreference!=null) {
            if (commentPreference.getModerationType() == CommentPreferenceType.Postmoderation) {
                commentPreference.setModerationType(CommentPreferenceType.Premoderation);
                dataManager.commit(commentPreference);
                moderationTypeBtn.setCaption(messages.getMessage(CommentPreferenceType.class, "CommentPreferenceType.Premoderation"));
            } else {
                commentPreference.setModerationType(CommentPreferenceType.Postmoderation);
                dataManager.commit(commentPreference);
                moderationTypeBtn.setCaption(messages.getMessage(CommentPreferenceType.class, "CommentPreferenceType.Postmoderation"));
            }
        }
    }
}