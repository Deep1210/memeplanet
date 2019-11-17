package in.incourt.incourtnews.core.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bhavan on 2/14/17.
 */

public class PostLikeModel extends IncourtModel {

    @SerializedName("records")
    @Expose
    boolean records;

    public boolean isRecords() {
        return records;
    }

    public void setRecords(boolean records) {
        this.records = records;
    }

}
