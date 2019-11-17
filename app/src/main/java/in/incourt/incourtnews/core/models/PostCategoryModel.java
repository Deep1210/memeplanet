package in.incourt.incourtnews.core.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bhavan on 2/22/17.
 */

public class PostCategoryModel extends IncourtModel {

    @SerializedName("category_id")
    @Expose
    long category_id;

    @SerializedName("post_id")
    @Expose
    long post_id;

    public long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
    }

    public long getPost_id() {
        return post_id;
    }

    public void setPost_id(long post_id) {
        this.post_id = post_id;
    }
}
