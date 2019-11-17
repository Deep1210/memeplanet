package in.incourt.incourtnews.core.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 2/8/2017.
 */

public class ContactSyncModel extends IncourtModel {

    @SerializedName("status")
    @Expose
    int status;

    public int getApp_status() {
        return status;
    }

    public void setApp_status(int app_user_id) {
        this.status = app_user_id;
    }


}
