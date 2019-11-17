package in.incourt.incourtnews.core.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bhavan on 12/26/16.
 */

public class ContactListModel extends IncourtModel {

    @SerializedName("app_user_id")
    @Expose
    public int app_user_id;

    public int getApp_user_id() {
        return app_user_id;
    }

    public void setApp_user_id(int app_user_id) {
        this.app_user_id = app_user_id;
    }
}
