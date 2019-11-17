package in.incourt.incourtnews.core.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bhavan on 1/31/17.
 */

public class ImageModel  extends  IncourtModel{

    @SerializedName("id")
    @Expose
    int id;

    @SerializedName("image_md")
    @Expose
    String image_md;

    @SerializedName("image_hq")
    @Expose
    String image_hq;

    @SerializedName("image_name")
    @Expose
    String image_name;

    @SerializedName("type")
    @Expose
    int type;

    @SerializedName("post_id")
    @Expose
    int post_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getImage_md() {
        return image_md;
    }

    public void setImage_md(String image_md) {
        this.image_md = image_md;
    }

    public String getImage_hq() {
        return image_hq;
    }

    public void setImage_hq(String image_hq) {
        this.image_hq = image_hq;
    }
}
