package in.incourt.incourtnews.core.models;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

import in.incourt.incourtnews.core.http.RestAdapter;
import in.incourt.incourtnews.helpers.UserHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by bhavan on 1/31/17.
 */

public class PostsModel extends IncourtModel {

    @SerializedName("posts")
    @Expose
    List<PostModel> postModelList;

    @SerializedName("advertisement")
    @Expose
    List<AdvertisementModel.Advertisement> advertisements;

    public List<PostModel> getPostModelList() {
        return postModelList;
    }

    public void setPostModelList(List<PostModel> postModelList) {
        this.postModelList = postModelList;
    }

    public List<AdvertisementModel.Advertisement> getAdvertisements() {
        return advertisements;
    }

    public void setAdvertisements(List<AdvertisementModel.Advertisement> advertisements) {
        this.advertisements = advertisements;
    }

    public class PostModel
    {
        @SerializedName("id")
        @Expose
        int id;

        @SerializedName("title")
        @Expose
        String title;

        @SerializedName("description")
        @Expose
        String description;

        @SerializedName("link")
        @Expose
        String link;

        @SerializedName("url")
        @Expose
        String url;

        @SerializedName("is_approved")
        @Expose
        int is_approved;

        @SerializedName("title_tag")
        @Expose
        String title_tag;

        @SerializedName("meta_des")
        @Expose
        String meta_des;

        @SerializedName("share_cat")
        //@Expose
        int share_cat;

        @SerializedName("author_id")
        //@Expose
        int author_id;

        @SerializedName("type")
        @Expose
        String type;

        @SerializedName("publisher_id")
        @Expose
        int publisher_id;

        @SerializedName("channel_id")
        @Expose
        int channel_id;

        @SerializedName("share_title")
        @Expose
        String share_title;

        @SerializedName("rss_url")
        @Expose
        String rss_url;

        @SerializedName("pubDate")
        @Expose
        String pubDate;

        @SerializedName("created_at")
        @Expose
        String created_at;

        @SerializedName("updated_at")
        @Expose
        String updated_at;

        @SerializedName("tiny_url")
        @Expose
        String tiny_url;

        @SerializedName("publish_type")
        @Expose
        int publish_type;

        @SerializedName("images")
        @Expose
        List<ImageModel> images;

        @SerializedName("publisher")
        @Expose
        PublisherModel publisher;

        @SerializedName("publisher_name")
        @Expose
        String publisher_name;

        @SerializedName("categories")
        @Expose
        List<PostCategoryModel> postCategoryModels;

        @SerializedName("topics")
        @Expose
        List<PostTopicModel> postTopicModels;

        @SerializedName("wired_count")
        @Expose
        int wired_count;

        @SerializedName("contributor")
        @Expose
        PostContributor postContributor;

        @SerializedName("is_like_count")
        @Expose
        int is_like = 0;

        @SerializedName("like_count")
        @Expose
        int likes_count = 0;

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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getIs_approved() {
            return is_approved;
        }

        public void setIs_approved(int is_approved) {
            this.is_approved = is_approved;
        }

        public String getTitle_tag() {
            return title_tag;
        }

        public void setTitle_tag(String title_tag) {
            this.title_tag = title_tag;
        }

        public String getMeta_des() {
            return meta_des;
        }

        public void setMeta_des(String meta_des) {
            this.meta_des = meta_des;
        }

        public int getShare_cat() {
            return share_cat;
        }

        public void setShare_cat(int share_cat) {
            this.share_cat = share_cat;
        }

        public int getAuthor_id() {
            return author_id;
        }

        public void setAuthor_id(int author_id) {
            this.author_id = author_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getPublisher_id() {
            return publisher_id;
        }

        public void setPublisher_id(int publisher_id) {
            this.publisher_id = publisher_id;
        }

        public int getChannel_id() {
            return channel_id;
        }

        public void setChannel_id(int channel_id) {
            this.channel_id = channel_id;
        }

        public String getShare_title() {
            return share_title;
        }

        public void setShare_title(String share_title) {
            this.share_title = share_title;
        }

        public String getRss_url() {
            return rss_url;
        }

        public void setRss_url(String rss_url) {
            this.rss_url = rss_url;
        }

        public String getPubDate() {
            return pubDate;
        }

        public void setPubDate(String pubDate) {
            this.pubDate = pubDate;
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

        public String getTiny_url() {
            return tiny_url;
        }

        public void setTiny_url(String tiny_url) {
            this.tiny_url = tiny_url;
        }

        public List<ImageModel> getImages() {
            return images;
        }

        public void setImages(List<ImageModel> images) {
            this.images = images;
        }

        public PublisherModel getPublisher() {
            return publisher;
        }

        public void setPublisher(PublisherModel publisher) {
        this.publisher = publisher;
    }

        public List<PostCategoryModel> getPostCategoryModels() {
            return postCategoryModels;
        }

        public void setPostCategoryModels(List<PostCategoryModel> postCategoryModels) {
            this.postCategoryModels = postCategoryModels;
        }

        public List<PostTopicModel> getPostTopicModels() {
            return postTopicModels;
        }

        public void setPostTopicModels(List<PostTopicModel> postTopicModels) {
            this.postTopicModels = postTopicModels;
        }

        public int getWired_count() {
            return wired_count;
        }

        public void setWired_count(int wired_count) {
            this.wired_count = wired_count;
        }

        public PostContributor getPostContributor() {
            return postContributor;
        }

        public void setPostContributor(PostContributor postContributor) {
            this.postContributor = postContributor;
        }

        public int getLikes_count() {
            return likes_count;
        }

        public void setLikes_count(int likes_count) {
            this.likes_count = likes_count;
        }

        public int getIs_like() {
            return is_like;
        }

        public void setIs_like(int is_like) {
            this.is_like = is_like;
        }

        public int getPublish_type() {
            return publish_type;
        }

        public void setPublish_type(int publish_type) {
            this.publish_type = publish_type;
        }

        public String getPublisher_name() {
            return publisher_name;
        }

        public void setPublisher_name(String publisher_name) {
            this.publisher_name = publisher_name;
        }

    }


    public static void getLikeChange(long post_id, int status){
        /*
        * If user not login return false
        */
        if(!UserHelper.getState())return;

        HashMap<String, long[]> request = new HashMap<>();
        long post_ids[] = new long[1];
        post_ids[0] = post_id;
        request.put("post_ids", post_ids);
        if(status == 1) getLike(request);
        else if (status == 0) getUnLike(request);

    }

    public static void getLike(HashMap<String, long[]> request){
        RestAdapter.get().postLike(request).enqueue(new Callback<PostLikeModel>() {
            @Override
            public void onResponse(Call<PostLikeModel> call, Response<PostLikeModel> response) {
                Log.d("ASD", "ASD");
            }

            @Override
            public void onFailure(Call<PostLikeModel> call, Throwable t) {
                Log.d("ASD", "ASD");
            }
        });
    }

    public static void getUnLike(HashMap<String, long[]> request){
        RestAdapter.get().postUnLike(request).enqueue(new Callback<PostLikeModel>() {
            @Override
            public void onResponse(Call<PostLikeModel> call, Response<PostLikeModel> response) {
                Log.d("ASD", "ASD");
            }

            @Override
            public void onFailure(Call<PostLikeModel> call, Throwable t) {
                Log.d("ASD", "ASD");
            }
        });
    }


    public static void bookmarkChnage(long post_id, int status){
         /*
        * If user not login return false
        */
        if(!UserHelper.getState())return;

        HashMap<String, long[]> request = new HashMap<>();
        long post_ids[] = new long[1];
        post_ids[0] = post_id;
        request.put("post_ids", post_ids);
        if(status == 1) getBookmarks(request);
        else if (status == 0) getBookmarksRemove(request);
    }

    public static void getBookmarks(HashMap<String, long[]> request){
        RestAdapter.get().postBookmarks(request).enqueue(new Callback<PostBookmarksModel>() {
            @Override
            public void onResponse(Call<PostBookmarksModel> call, Response<PostBookmarksModel> response) {
            }

            @Override
            public void onFailure(Call<PostBookmarksModel> call, Throwable t) {

            }
        });
    }

    public static void getBookmarksRemove(HashMap<String, long[]> request){
        RestAdapter.get().postBookmarksRemove(request).enqueue(new Callback<PostBookmarksModel>() {
            @Override
            public void onResponse(Call<PostBookmarksModel> call, Response<PostBookmarksModel> response) {
            }

            @Override
            public void onFailure(Call<PostBookmarksModel> call, Throwable t) {

            }
        });
    }


    public static void getWired(long post_id){
        HashMap<String, Long> request = new HashMap<>();
        request.put("post_id", post_id);
        RestAdapter.get().postWired(request).enqueue(new Callback<PostLikeModel>() {
            @Override
            public void onResponse(Call<PostLikeModel> call, Response<PostLikeModel> response) {
            }

            @Override
            public void onFailure(Call<PostLikeModel> call, Throwable t) {
                Log.d("ASD", "ASD");
            }
        });
    }

}
