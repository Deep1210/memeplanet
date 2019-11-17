package in.incourt.incourtnews.core.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by bhavan on 12/26/16.
 */

public class IncourtModel {

    @SerializedName("is_error")
    @Expose
    public boolean is_error;


    @SerializedName("is_success")
    @Expose
    public boolean is_success;

    public boolean is_success() {
        return is_success;
    }


    @SerializedName("cdn")
    @Expose
    public String cdn_url;



    public void setIs_success(boolean is_success) {
        this.is_success = is_success;
    }

    public boolean is_error() {
        return is_error;
    }

    public void setIs_error(boolean is_error) {
        this.is_error = is_error;
    }

    public String getCdn_url() {
        return cdn_url;
    }

    public void setCdn_url(String cdn_url) {
        this.cdn_url = cdn_url;
    }
}
