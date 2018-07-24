package ru.iovchinnikov.talks.listener;

import org.springframework.stereotype.Component;
import com.haulmont.cuba.core.listener.BeforeAttachEntityListener;
import ru.iovchinnikov.talks.entity.Comment;
import ru.iovchinnikov.talks.entity.CommentStatus;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import com.haulmont.cuba.core.EntityManager;

@Component("discuss_CommentListener")
public class CommentListener implements BeforeInsertEntityListener<Comment> {

    /*
    @Override
    public void onBeforeAttach(Comment entity) {
        entity.setCommentStatus(CommentStatus.notApproved);
    }
*/


    @Override
    public void onBeforeInsert(Comment entity, EntityManager entityManager) {
        entity.setCommentStatus(CommentStatus.notApproved);
    }


}