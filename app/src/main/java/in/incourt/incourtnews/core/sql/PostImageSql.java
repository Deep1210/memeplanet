package in.incourt.incourtnews.core.sql;

import com.orm.dsl.Column;
import com.orm.dsl.Table;

import java.util.ArrayList;
import java.util.List;

import in.incourt.incourtnews.core.models.ImageModel;
import in.incourt.incourtnews.helpers.ImageHelper;

/**
 * Created by bhavan on 1/31/17.
 */

@Table
public class PostImageSql extends IncourtSQLModel {

    private Long id;

    @Column(name = "image_id")
    int image_id;

    @Column(name = "image_name")
    String image_name;

    @Column(name = "image_name_hd")
    String image_name_hd;

    @Column(name = "type")
    String type;

    @Column(name = "post_id")
    int post_id;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getImage_name_hd() {
        return image_name_hd;
    }

    public void setImage_name_hd(String image_name_hd) {
        this.image_name_hd = image_name_hd;
    }

    public static void postImages(List<ImageModel> imageModelList){
        if(imageModelList != null && imageModelList.size() > 0){
            for (int i = 0; i < imageModelList.size() ; i++){
                savePostImage(imageModelList.get(i));
            }
        }
    }

    static void savePostImage(ImageModel imageModel){
        PostImageSql postImageSql = mergeRequest(imageModel, true);
        postImageSql.save();
    }

    static PostImageSql mergeRequest(ImageModel imageModel, boolean flag){
        PostImageSql postImageSql = null;
        if(flag) postImageSql = getByImageId(imageModel.getId());
        if(postImageSql == null) postImageSql = new PostImageSql();
        postImageSql.setImage_id(imageModel.getId());
        postImageSql.setImage_name(imageModel.getImage_md());
        if(imageModel.getType() == 1) {
            postImageSql.setImage_name_hd(imageModel.getImage_hq());
        }
        postImageSql.setPost_id(imageModel.getPost_id());
        postImageSql.setType(String.valueOf(imageModel.getType()));
        return postImageSql;
    }


    public static PostImageSql getByImageId(int image_id){
        List<PostImageSql> postImageSqls =  PostImageSql.find(PostImageSql.class, "image_id = " + image_id, null);
        if(postImageSqls != null && postImageSqls.size() > 0) return postImageSqls.get(0);
        return null;
    }

    public String getPostImage(){
        if(ImageHelper.getHdImageStatus()) return this.getImage_name_hd();
        return this.getImage_name();
    }

    public static List<PostImageSql> parseData(List<ImageModel> imageModelList, boolean flag){
        List<PostImageSql> postImageSqls = new ArrayList<>();
        if(imageModelList != null && imageModelList.size() > 0){
            for(int i = 0; i < imageModelList.size() ; i++){
                postImageSqls.add(i, mergeRequest(imageModelList.get(i), flag));
            }
        }
        return postImageSqls;
    }

    public static void savePostImageFromSql(List<PostImageSql> postImageSqls){
        if(postImageSqls!= null && postImageSqls.size() > 0){
            for (int i = 0; i < postImageSqls.size() ; i++){
                postImageSqls.get(i).save();
            }
        }
    }

    public static void updatePostImages(List<ImageModel> imageModelList, int post_id) {
        String image_post_ids = "";
        if(imageModelList != null && imageModelList.size() > 0){
            for (int i =0 ; i < imageModelList.size() ; i++){
                savePostImage(imageModelList.get(i));
                image_post_ids += ((image_post_ids.length() > 0)? ",":"") + imageModelList.get(i).getId();
            }
        }
        if(image_post_ids.length() > 0) deleteOldImageRecords(image_post_ids, post_id);
    }

    private static void deleteOldImageRecords(String image_post_ids, int post_id) {
        PostImageSql.deleteAll(PostImageSql.class, "image_id not in("+ image_post_ids +") AND post_id = ?", new String[]{String.valueOf(post_id)});
    }
}
