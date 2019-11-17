package in.incourt.incourtnews.core.http;

import in.incourt.incourtnews.core.models.AdvertisementModel;
import in.incourt.incourtnews.core.models.CategoriesModel;
import in.incourt.incourtnews.core.models.ContactSyncModel;
import in.incourt.incourtnews.core.models.DeviceIdRegistrationModel;
import in.incourt.incourtnews.core.models.PostBookmarksModel;
import in.incourt.incourtnews.core.models.PostLikeModel;
import in.incourt.incourtnews.core.models.PostWithTopicSearchModel;
import in.incourt.incourtnews.core.models.PostsModel;
import in.incourt.incourtnews.core.models.SyncModel;
import in.incourt.incourtnews.core.models.TopicsModel;
import in.incourt.incourtnews.core.models.UserLoginModel;
import in.incourt.incourtnews.core.request.PostsRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by bhavan on 12/26/16.
 */

public interface IncourtWebServices {


    @GET("sync")
    Call<SyncModel> syncManager();

    @GET("category/list")
    Call<CategoriesModel> categoryList();

    @POST("contactsync")
    Call<ContactSyncModel> contactSync(@Body Map<String, List<HashMap<String, String>>> contacts);

    @POST("post/listAll")
    Call<PostsModel> postAll(@Body PostsRequest postsRequest);

    @POST("topic/search")
    Call<PostsModel> searchPost(@Body PostsRequest postsRequest);


    /**
     * APP USER LOGIN SIGN UP REQUESTS STARTS
     *
     */

    @POST("user/facebooksignup")
    Call<UserLoginModel> facebookSignup(@Body HashMap<String, String> request);

    @POST("user/facebookExits")
    Call<UserLoginModel> facebookExists(@Body HashMap<String, String> request);

    @POST("user/contactsignup")
    Call<UserLoginModel> contactsignup(@Body HashMap<String, String> request);


    @POST("topic/search")
    Call<PostWithTopicSearchModel> searchPostWIthTopic(@Body PostsRequest postsRequest);

    /**
    ** APP USER LOGIN SIGN UP REQUESTS END
    */


    /**
     * POST LIKE AND BOOKMARKS API
     */

    @POST("bookmarks/add")
    Call<PostBookmarksModel> postBookmarks(@Body HashMap<String, long[]> request);

    @POST("bookmarks/remove")
    Call<PostBookmarksModel> postBookmarksRemove(@Body HashMap<String, long[]> request);

    @POST("post/like")
    Call<PostLikeModel> postLike(@Body HashMap<String, long[]> request);

    @POST("post/unlike")
    Call<PostLikeModel> postUnLike(@Body HashMap<String, long[]> request);

    @POST("post/wired")
    Call<PostLikeModel> postWired(@Body HashMap<String, Long> request);


    /**
     * DEVICE TOKEN REGISTARION
     */

    @POST("user/android")
    Call<DeviceIdRegistrationModel> deviceIdRegistration(@Body HashMap<String, String> request);


    /**
     * TOPIC LIST CALLS
     */

    @GET("topic/listAll")
    Call<TopicsModel> topicList();

    @POST("topic/searchTopics")
    Call<TopicsModel> topicSearch(@Body HashMap<String, String> request);

    @GET("advertisement")
    Call<AdvertisementModel> getAdvertisement();


}
