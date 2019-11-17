package in.incourt.incourtnews.core.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by bhavan on 3/2/17.
 */

public class PostWithTopicSearchModel extends IncourtModel {

    @SerializedName("topics")
    @Expose
    List<TopicsModel.Topic> topicList;

    @SerializedName("posts")
    @Expose
    List<PostsModel.PostModel> postModelList;

    public List<TopicsModel.Topic> getTopicList() {
        return topicList;
    }

    public void setTopicList(List<TopicsModel.Topic> topicList) {
        this.topicList = topicList;
    }

    public List<PostsModel.PostModel> getPostModelList() {
        return postModelList;
    }

    public void setPostModelList(List<PostsModel.PostModel> postModelList) {
        this.postModelList = postModelList;
    }

    public PostsModel getPostModel(){
        PostsModel postsModel = new PostsModel();
        postsModel.setPostModelList(getPostModelList());
        return postsModel;
    }

    public TopicsModel getTopicsModel(){
        TopicsModel topicsModel = new TopicsModel();
        topicsModel.setTopicList(getTopicList());
        return topicsModel;
    }
}
