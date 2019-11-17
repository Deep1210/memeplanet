package in.incourt.incourtnews.core.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 2/7/2017.
 */

public class UserLoginModel extends IncourtModel {

    @SerializedName("app_user")
    @Expose
    AppUser appUser;

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public class AppUser {

        @SerializedName("id")
        @Expose
        int app_user_id;

        @SerializedName("phone")
        @Expose
        String phone;

        @SerializedName("email")
        @Expose
        String email;

        @SerializedName("name")
        @Expose
        String name;

        @SerializedName("registerid")
        @Expose
        String registerid;

        @SerializedName("registerby")
        @Expose
        int registerby;

        @SerializedName("access_token")
        @Expose
        String login_access_token;

        @SerializedName("status")
        @Expose
        int status;

        @SerializedName("created_at")
        @Expose
        String created_at;

        @SerializedName("updated_at")
        @Expose
        String updated_at;


        public int getApp_user_id() {
            return app_user_id;
        }

        public void setApp_user_id(int app_user_id) {
            this.app_user_id = app_user_id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRegisterid() {
            return registerid;
        }

        public void setRegisterid(String registerid) {
            this.registerid = registerid;
        }

        public int getRegisterby() {
            return registerby;
        }

        public void setRegisterby(int registerby) {
            this.registerby = registerby;
        }

        public String getLogin_access_token() {
            return login_access_token;
        }

        public void setLogin_access_token(String login_access_token) {
            this.login_access_token = login_access_token;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public boolean contact_sync = false;
    }

}
