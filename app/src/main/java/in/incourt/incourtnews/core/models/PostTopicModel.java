package in.incourt.incourtnews.core.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bhavan on 2/22/17.
 */

public class PostTopicModel extends IncourtModel {

    @SerializedName("topic_id")
    @Expose
    long topic_id;

    @SerializedName("post_id")
    @Expose
    long post_id;

    public long getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(long topic_id) {
        this.topic_id = topic_id;
    }

    public long getPost_id() {
        return post_id;
    }

    public void setPost_id(long post_id) {
        this.post_id = post_id;
    }
}
