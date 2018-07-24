package ru.iovchinnikov.talks.web.comment;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.data.GroupDatasource;
import ru.iovchinnikov.talks.entity.Comment;
import ru.iovchinnikov.talks.entity.CommentStatus;

import javax.inject.Inject;
import java.util.UUID;

public class CommentModeratorBrowse extends AbstractLookup {

    @Inject
    private GroupDatasource<Comment, UUID> commentsPreDs;

    @Inject
    private GroupDatasource<Comment, UUID> commentsPostDs;

    @Inject
    private DataManager dataManager;

    public void onApproveBtnClick() {
        if(selected(commentsPreDs)) {
            commentsPreDs.getItem().setCommentStatus(CommentStatus.approved);
            dataManager.commit(commentsPreDs.getItem());
        }
    }
    
    public void onRejectBtnClick() {
        if(selected(commentsPreDs)) {
            commentsPreDs.getItem().setCommentStatus(CommentStatus.rejected);
            dataManager.commit(commentsPreDs.getItem());
        }
    }

    public void onRemoveBtnClick() {
        if(selected(commentsPostDs)) {
            commentsPostDs.getItem().setCommentStatus(CommentStatus.deleted);
            dataManager.commit(commentsPostDs.getItem());
        }
    }

    public void onRestoreBtnClick() {
        if(selected(commentsPostDs)) {
            commentsPostDs.getItem().setCommentStatus(CommentStatus.approved);
            dataManager.commit(commentsPostDs.getItem());
        }
    }

    private boolean selected(GroupDatasource groupDatasource){
        if(groupDatasource.getItem()!=null){
            return true;
        }
        else{
            return false;
        }
    }
}