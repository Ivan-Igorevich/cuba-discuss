package ru.iovchinnikov.talks.web.comment;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.ViewRepository;
import com.haulmont.cuba.gui.data.impl.CustomCollectionDatasource;
import com.haulmont.cuba.gui.data.impl.CustomGroupDatasource;
import ru.iovchinnikov.talks.entity.Comment;
import ru.iovchinnikov.talks.entity.CommentStatus;
import ru.iovchinnikov.talks.service.ParamsService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Stepanov_ME on 23.07.2018.
 */
public class commentDatasource extends CustomGroupDatasource<Comment,UUID> {

    private DataManager dataManager=AppBeans.get(DataManager.class);

    private ParamsService paramsService= AppBeans.get(ParamsService.class);

    private ViewRepository viewRepository=AppBeans.get(ViewRepository.class);

    @Override
    protected Collection<Comment> getEntities(Map<String, Object> params) {
        Map<String,Object> dataParams=paramsService.getParams();
        if(dataParams!=null) {
            LoadContext<Comment> loadContext = LoadContext.create(Comment.class);
            loadContext.setQuery(LoadContext.createQuery("SELECT e FROM discuss$Comment e WHERE e.entity IN (SELECT n.id " +
                    "FROM " + dataParams.get("eName") + " n WHERE n.id =:entity) ORDER BY e.date DESC")
                    .setParameter("entity", dataParams.get("entity")));
            loadContext.setView(viewRepository.getView(Comment.class,"comment-view"));
            ArrayList<Comment> comments=new ArrayList<>(dataManager.loadList(loadContext));
            for (Comment comment:comments){
                if(comment.getCommentStatus().equals(CommentStatus.deleted)){
                    comment.setContents("Comment deleted");
                }
            }
            return comments;
        }
        else {
            return new ArrayList<>();
        }
    }
}
