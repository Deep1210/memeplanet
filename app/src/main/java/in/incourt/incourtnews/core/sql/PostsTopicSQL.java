package in.incourt.incourtnews.core.sql;

import in.incourt.incourtnews.core.models.PostTopicModel;
import com.orm.dsl.Column;
import com.orm.dsl.Table;

import java.util.List;

/**
 * Created by bhavan on 2/22/17.
 */

@Table
public class PostsTopicSQL extends IncourtSQLModel {

    private Long id;

    @Column(name = "post_id")
    long post_id;

    @Column(name = "topic_id")
    long topic_id;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public long getPost_id() {
        return post_id;
    }

    public void setPost_id(long post_id) {
        this.post_id = post_id;
    }

    public long getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(long topic_id) {
        this.topic_id = topic_id;
    }

    public static void extractRequest(List<PostTopicModel> postTopicModels){
        if(postTopicModels != null && postTopicModels.size() > 0){
            for(int i =0 ; i < postTopicModels.size() ; i++){
                saveTopic(postTopicModels.get(i));
            }
        }

    }

    public static void saveTopic(PostTopicModel postTopicModel){
        if(checkExists(postTopicModel.getTopic_id(), postTopicModel.getPost_id()))return;
        PostsTopicSQL postsTopicSQL = merge(postTopicModel.getTopic_id(), postTopicModel.getPost_id());
        postsTopicSQL.save();
    }

    static PostsTopicSQL merge(long topic_id, long post_id){
        PostsTopicSQL postsTopicSQL= new PostsTopicSQL();
        postsTopicSQL.setTopic_id(topic_id);
        postsTopicSQL.setPost_id(post_id);
        return postsTopicSQL;
    }

    static boolean checkExists(long topic_id, long post_id){
        String[] strings = new String[2];
        strings[0] = String.valueOf(topic_id);
        strings[1] = String.valueOf(post_id);
        List<PostsTopicSQL> postsCategorySQL = PostsTopicSQL.find(PostsTopicSQL.class, "topic_id = ? AND post_id = ?", strings);
        if(postsCategorySQL != null && postsCategorySQL.size() > 0) return true;
        return false;
    }

    public static int removeRecord(long post_id) {
        return PostsTopicSQL.deleteAll(PostsTopicSQL.class, "post_id  = ?", new String[]{String.valueOf(post_id)});
    }
}
