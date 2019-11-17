package in.incourt.incourtnews.core.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by bhavan on 1/23/17.
 */

public class CategoriesModel extends IncourtModel {

    @SerializedName("category")
    @Expose
    List<Category> categoryList;

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public Category getCategory(){
        return new Category();
    }

    public class Category{

        @SerializedName("id")
        @Expose
        int id;

        @SerializedName("name")
        @Expose
        String name;

        @SerializedName("category_image")
        @Expose
        String category_image;

        @SerializedName("position")
        @Expose
        int position;

        @SerializedName("is_active")
        @Expose
        int is_active;

        @SerializedName("updated_at")
        @Expose
        String updated_at;

        @SerializedName("type")
        @Expose
        int type;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCategory_image() {
            return category_image;
        }

        public void setCategory_image(String category_image) {
            this.category_image = category_image;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public int getIs_active() {
            return is_active;
        }

        public void setIs_active(int is_active) {
            this.is_active = is_active;
        }

        public String getUpdated_at() {
            return (updated_at != null)? updated_at.trim(): updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

    }

}
