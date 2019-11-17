package in.incourt.incourtnews.core.request;

import in.incourt.incourtnews.core.models.PostWithTopicSearchModel;
import in.incourt.incourtnews.core.models.PostsModel;

import retrofit2.Call;

/**
 * Created by bhavan on 3/16/17.
 */

public interface PostRequestInterface {

    interface PostModels {

        void onFirstRunPosts(PostsModel postModel, PostsRequest postsRequest);

        void onPostRequestSuccess(PostsModel postsModel, PostsRequest postsRequest);

        void onPostRequestNextPage(PostsModel postsModel, PostsRequest postsRequest);

        void onPostRequestError(Call<PostsModel> postsModel, Throwable t);


    }
    interface PostModelsSearch {

        void onPostRequestErrorSearch(Call<PostWithTopicSearchModel> call, Throwable t);

        void onSuccessSearchNextPage(PostWithTopicSearchModel body);

        void onSuccessSearch(PostWithTopicSearchModel body);
    }
}
