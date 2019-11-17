package in.incourt.incourtnews.core.models;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import in.incourt.incourtnews.core.request.AdvertisementRequestInterface;

/**
 * Created by bhavan on 3/17/17.
 */

public class AdvertisementModel extends IncourtModel {

    AdvertisementRequestInterface advertisementRequestInterface;

    @SerializedName("data")
    @Expose
    List<Advertisement> advertisements;

    public class Advertisement {

        @SerializedName("id")
        @Expose
        int id;

        @SerializedName("title")
        @Expose
        String title;

        @SerializedName("link_url")
        @Expose
        String link_url;

        @SerializedName("image_url")
        @Expose
        String image_url;

        @SerializedName("additional_data")
        @Expose
        String additional_data;

        @SerializedName("type")
        @Expose
        int type;

        @SerializedName("start_date")
        @Expose
        String start_date;

        @SerializedName("end_date")
        @Expose
        String end_date;

        @SerializedName("status")
        @Expose
        int status;

        @SerializedName("created_at")
        @Expose
        String created_at;

        @SerializedName("updated_at")
        @Expose
        String updated_at;

        @SerializedName("position")
        @Expose
        String position;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLink_url() {
            return link_url;
        }

        public void setLink_url(String link_url) {
            this.link_url = link_url;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public String getAdditional_data() {
            return additional_data;
        }

        public void setAdditional_data(String additional_data) {
            this.additional_data = additional_data;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getStart_date() {
            return start_date;
        }

        public void setStart_date(String start_date) {
            this.start_date = start_date;
        }

        public String getEnd_date() {
            return end_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
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

        public String getPosition() {
            return position;
        }

        public String[] getPositionList(){
            if(getPosition() != null) return getPosition().split(",");
            return new String[]{};
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public AdvertisementButtonModel getButtonSetting(){
            if(getAdditional_data() != null) {
                return new Gson().fromJson(getAdditional_data(), AdvertisementButtonModel.class);
            }
            return new AdvertisementButtonModel();
        }

        public class AdvertisementButtonModel{

            @SerializedName("background_color")
            @Expose
            String background_color;

            @SerializedName("foreground_color")
            @Expose
            String foreground_color;

            public String getBackground_color() {
                return background_color;
            }

            public void setBackground_color(String background_color) {
                this.background_color = background_color;
            }

            public String getForeground_color() {
                return foreground_color;
            }

            public void setForeground_color(String foreground_color) {
                this.foreground_color = foreground_color;
            }
        }

    }

    public List<Advertisement> getAdvertisements() {
        return advertisements;
    }

}
