package ru.iovchinnikov.talks.listener;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import org.springframework.stereotype.Component;
import com.haulmont.cuba.core.listener.BeforeAttachEntityListener;
import ru.iovchinnikov.talks.entity.Comment;
import ru.iovchinnikov.talks.entity.CommentPreference;
import ru.iovchinnikov.talks.entity.CommentPreferenceType;
import ru.iovchinnikov.talks.entity.CommentStatus;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import com.haulmont.cuba.core.EntityManager;

@Component("discuss_CommentListener")
public class CommentListener implements BeforeInsertEntityListener<Comment> {

    @Override
    public void onBeforeInsert(Comment entity, EntityManager entityManager) {
        DataManager dataManager= AppBeans.get(DataManager.class);
        LoadContext<CommentPreference> cplc=LoadContext.create(CommentPreference.class);
        cplc.setQuery(LoadContext.createQuery("select e from discuss$CommentPreference e"));
        CommentPreference commentPreference=dataManager.load(cplc);
        if(commentPreference!=null) {
            if (commentPreference.getModerationType() == CommentPreferenceType.Premoderation) {
                entity.setCommentStatus(CommentStatus.notApproved);
            } else {
                entity.setCommentStatus(CommentStatus.approved);
            }
        }
    }


}