package in.incourt.incourtnews.core.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import in.incourt.incourtnews.core.http.RestAdapter;
import in.incourt.incourtnews.core.models.PostWithTopicSearchModel;
import in.incourt.incourtnews.core.models.PostsModel;
import in.incourt.incourtnews.core.sql.PostsSql;
import in.incourt.incourtnews.helpers.NetworkHelper;
import in.incourt.incourtnews.helpers.PostListTypeHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by bhavan on 2/27/17.
 */

public class PostsRequest extends IncourtRequest implements Cloneable {


    PostRequestInterface.PostModels postRequestInterfacePostModels;
    PostRequestInterface.PostModelsSearch postRequestInterfacePostModelsSearch;
    int TYPE_GET_FIRST_RUN = 6;
    int TYPE_FIRST_PAGE = 1;
    int TYPE_NEW_UPDATE = 2;
    int TYPE_NEXT_PAGE = 3;
    int TYPE_SEARCH = 4;
    int TYPE_SERACH_NEXT_PAGE = 5;

    boolean flag = false;
    int TYPE = 1;

    boolean current_status = false;

    Call<PostWithTopicSearchModel> postWithTopicSearchModelCall;
    Call<PostsModel> postsModelCall;


    @SerializedName("id")
    @Expose
    int id;

    @SerializedName("post_id")
    @Expose
    int post_id;

    @SerializedName("category_id")
    @Expose
    ArrayList<Integer> category_id;

    @SerializedName("topic_id")
    @Expose
    ArrayList<Integer> topic_id;

    @SerializedName("title")
    @Expose
    String title;

    @SerializedName("pubDate")
    @Expose
    String pubDate;

    @SerializedName("is_new")
    @Expose
    int is_new = 0;


    @SerializedName("is_old")
    @Expose
    int is_old = 0;

    @SerializedName("is_updated")
    @Expose
    int is_updated;

    @SerializedName("updated_at")
    @Expose
    String updated_at;

    @SerializedName("page")
    @Expose
    int page = 1;

    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("is_wire")
    @Expose
    int is_wire = 0;

    @SerializedName("is_like")
    @Expose
    int is_like = 0;

    @SerializedName("is_bookmarks")
    @Expose
    int is_bookmarks = 0;


    public PostsRequest(PostRequestInterface.PostModels postRequestInterfacePostModels) {
        this.postRequestInterfacePostModels = postRequestInterfacePostModels;
    }

    public PostsRequest(PostRequestInterface.PostModelsSearch postRequestInterfacePostModelsSearch) {
        this.postRequestInterfacePostModelsSearch = postRequestInterfacePostModelsSearch;
    }

    public PostsRequest() {
    }

    public int getId() {
        return id;
    }

    public PostsRequest setId(int id) {
        if (id > 0) this.id = id;
        return this;
    }

    public int getPost_id() {
        return post_id;
    }

    public PostsRequest setPost_id(int post_id) {
        this.post_id = post_id;
        return this;
    }

    public ArrayList<Integer> getCategory_id() {
        return category_id;
    }

    public PostsRequest setCategory_id(int category_id) {
        if (this.category_id == null) this.category_id = new ArrayList<>();
        this.category_id.add(category_id);
        return this;
    }

    public ArrayList<Integer> getTopic_id() {
        return topic_id;
    }

    public PostsRequest setTopic_id(int topic_id) {
        if (this.topic_id == null) this.topic_id = new ArrayList<>();
        this.topic_id.add(topic_id);
        return this;
    }

    public String getTitle() {
        return title;
    }

    public PostsRequest setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getPubDate() {
        return pubDate;
    }

    public PostsRequest setPubDate(String pubDate) {
        if (pubDate != null && pubDate.length() > 0) this.pubDate = pubDate;
        return this;
    }

    public int is_new() {
        return is_new;
    }

    public PostsRequest setIs_new(boolean is_new) {
        this.is_new = (is_new) ? 1 : 0;
        return this;
    }

    public int is_old() {
        return is_old;
    }

    public PostsRequest setIs_old(boolean is_old) {
        this.is_old = (is_old) ? 1 : 0;
        return this;
    }

    public int getPage() {
        return page;
    }

    public PostsRequest setPage(int page) {
        this.page = page;
        return this;
    }


    public int is_updated() {
        return is_updated;
    }

    public PostsRequest setIs_updated(boolean is_updated) {
        this.is_updated = (is_updated) ? 1 : 0;
        return this;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public PostsRequest setUpdated_at(String updated_at) {
        if (updated_at != null && updated_at.length() > 0) this.updated_at = updated_at;
        return this;
    }

    public int is_wire() {
        return is_wire;
    }

    public PostsRequest setIs_wire(boolean is_wire) {
        this.is_wire = (is_wire) ? 1 : 0;
        return this;
    }

    public int is_like() {
        return is_like;
    }

    public PostsRequest setIs_like(boolean is_like) {
        this.is_like = (is_like) ? 1 : 0;
        return this;
    }

    public int is_bookmarks() {
        return is_bookmarks;
    }

    public PostsRequest setIs_bookmarks(boolean is_bookmarks) {
        this.is_bookmarks = (is_bookmarks) ? 1 : 0;
        return this;
    }

    public void sendPostModel(final PostsRequest postsRequest) {
        setExecuting(true);
        clearPreviousCall();
        RestAdapter.get().postAll(postsRequest).enqueue(new Callback<PostsModel>() {
            @Override
            public void onResponse(retrofit2.Call<PostsModel> call, Response<PostsModel> response) {
                if (response.body() != null) {
                    onSuccess(response.body());
                }
                setExecuting(false);
            }

            @Override
            public void onFailure(retrofit2.Call<PostsModel> call, Throwable t) {
                getPostRequestInterfacePostModel().onPostRequestError(call, t);
                setExecuting(false);
            }
        });
    }

    public void fetchFirstPage() {
        setTYPE(TYPE_FIRST_PAGE);
        setIs_new(true);
        sendPostModel(this);
    }

    public void fetchNextPage() {
        if (NetworkHelper.state()) {
            PostsRequest postsRequest = null;
            try {
                postsRequest = (PostsRequest) clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            if (postsRequest == null) return;

            postsRequest.setIs_updated(false)
                    .setIs_new(false)
                    .setPubDate(null)
                    .setUpdated_at(null)
                    .setIs_old(true)
                    .setTYPE(TYPE_NEXT_PAGE);

            sendPostModel(postsRequest);
        }
    }

    public void fetchSearch() {
        setTYPE(TYPE_SEARCH).setIs_new(true).setFlag(flag);
        sendPostWithTopicSearchModel();
    }

    public void sendPostWithTopicSearchModel() {
        setExecuting(true);
        //clearPreviousCall();
        getPostWithTopicSearchModelCall().enqueue(new Callback<PostWithTopicSearchModel>() {
            @Override
            public void onResponse(Call<PostWithTopicSearchModel> call, Response<PostWithTopicSearchModel> response) {
                if (getTYPE() == TYPE_SEARCH) {
                    getPostRequestInterfacePostModelsSearch().onSuccessSearch(response.body());
                } else if (getTYPE() == TYPE_SERACH_NEXT_PAGE) {
                    getPostRequestInterfacePostModelsSearch().onSuccessSearchNextPage(response.body());
                }
                setExecuting(false);
            }

            @Override
            public void onFailure(Call<PostWithTopicSearchModel> call, Throwable t) {
                getPostRequestInterfacePostModelsSearch().onPostRequestErrorSearch(call, t);
                setExecuting(false);
            }
        });
    }

    private void clearPreviousCall() {
        if (getPostWithTopicSearchModelCall() != null) getPostWithTopicSearchModelCall().cancel();
        if (getPostsModelCall() != null) getPostsModelCall().cancel();
    }

    public PostRequestInterface.PostModels getPostRequestInterfacePostModel() {
        return postRequestInterfacePostModels;
    }

    public String getName() {
        return name;
    }

    public PostsRequest setName(String name) {
        this.name = name;
        return this;
    }

    public int getTYPE() {
        return TYPE;
    }

    public PostsRequest setTYPE(int TYPE) {
        this.TYPE = TYPE;
        return this;
    }

    public void onSuccess(PostsModel postsModel) {

        if (getTYPE() == TYPE_GET_FIRST_RUN) {
            getPostRequestInterfacePostModel().onFirstRunPosts(postsModel, this);
        } else if (getTYPE() == TYPE_FIRST_PAGE) {
            getPostRequestInterfacePostModel().onPostRequestSuccess(postsModel, this);
        } else if (getTYPE() == TYPE_NEXT_PAGE) {
            getPostRequestInterfacePostModel().onPostRequestNextPage(postsModel, this);
        }
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public PostRequestInterface.PostModelsSearch getPostRequestInterfacePostModelsSearch() {
        return postRequestInterfacePostModelsSearch;
    }

    public boolean isExecuting() {
        return current_status;
    }

    public void setExecuting(boolean current_status) {
        this.current_status = current_status;
    }

    public void getFirstRunPosts() {
        sendPostModel(makeFirstRequest());
    }

    public PostsRequest makeFirstRequest() {
        setTYPE(TYPE_GET_FIRST_RUN);
        return new PostsRequest(getPostRequestInterfacePostModelsSearch())
                .setIs_new(true)
                .setId(PostsSql.getMaxPostId(PostListTypeHelper.ALL_NEWS))
                .setPubDate(PostsSql.getMaxDateString());
    }

    public Call<PostWithTopicSearchModel> getPostWithTopicSearchModelCall() {
        if (postWithTopicSearchModelCall == null)
            postWithTopicSearchModelCall = RestAdapter.get().searchPostWIthTopic(this);
        return postWithTopicSearchModelCall;
    }

    public Call<PostsModel> getPostsModelCall() {
        if (postsModelCall == null) postsModelCall = RestAdapter.get().postAll(this);
        return postsModelCall;
    }

    public void getPostsWithCategoryIds(String[] categoriesIdsType) {
        PostsRequest postsRequest = new PostsRequest(getPostRequestInterfacePostModel());
        if (categoriesIdsType != null && categoriesIdsType.length > 0) {
            for (int i = 0; i < categoriesIdsType.length; i++) {
                if (categoriesIdsType[i] != null)
                    postsRequest.setCategory_id(Integer.parseInt(categoriesIdsType[i]));
            }
        }
        postsRequest.fetchFirstPage();
    }

    public PostsRequest clearAllParams() {
        setIs_like(false).setIs_new(false).setIs_updated(false).setName(null)
                .setPage(1).setId(0).setIs_bookmarks(false).setIs_wire(false).setTitle(null)
                .setIs_old(false);
        updated_at = null;
        topic_id = null;
        category_id = null;
        pubDate = null;
        return this;
    }


}
