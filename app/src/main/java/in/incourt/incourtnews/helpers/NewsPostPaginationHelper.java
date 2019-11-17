package in.incourt.incourtnews.helpers;

import java.util.List;

import in.incourt.incourtnews.core.models.PostsModel;
import in.incourt.incourtnews.core.request.PostRequestInterface;
import in.incourt.incourtnews.core.request.PostsRequest;
import in.incourt.incourtnews.core.sql.PostsSql;
import in.incourt.incourtnews.newslist.adapters.NewsListVerticalViewPagerAdapter;
import retrofit2.Call;

/**
 * Created by bhavan on 6/4/17.
 */

public class NewsPostPaginationHelper implements PostRequestInterface.PostModels{

    PostsRequest postsRequest;
    boolean post_enqueue = false;

    int current_position;
    int total_size;

    int page_no = 2;

    public boolean is_load_more = true;

    PostsRequest old_post_request;

    NewsListVerticalViewPagerAdapter newsListVerticalViewPagerAdapter;

    public NewsPostPaginationHelper(NewsListVerticalViewPagerAdapter newsListVerticalViewPagerAdapter, PostsRequest postsRequest) {
        this.newsListVerticalViewPagerAdapter = newsListVerticalViewPagerAdapter;
        this.postsRequest = new PostsRequest(this);
        old_post_request = postsRequest;
        getRequestWithType();
    }

    public void onNextPageSelected(int position, int total_size){
        if(!post_enqueue && is_load_more) {
            this.current_position = position;
            this.total_size = total_size;
            postsRequest.setPage(page_no);
            postsRequest.fetchNextPage();
            post_enqueue = true;
        }
    }

    @Override
    public void onFirstRunPosts(PostsModel postModel, PostsRequest postsRequest) {
        onNextPage(postModel);
    }

    @Override
    public void onPostRequestSuccess(PostsModel postsModel, PostsRequest postsRequest) {
        onNextPage(postsModel);
    }

    @Override
    public void onPostRequestNextPage(PostsModel postsModel, PostsRequest postsRequest) {
        onNextPage(postsModel);
    }

    public void onNextPage(PostsModel postsModel ){
        post_enqueue = false;
        List<PostsSql> postsSqls = PostsSql.parseData(postsModel, false);
        if(postsSqls.size() > 0){
            page_no++;
            getNewsListVerticalViewPagerAdapter().onNextPageAppends(postsSqls, current_position);
            if(postsSqls.size() < 25) is_load_more = false;
            return;
        }
        is_load_more = false;
        getNewsListVerticalViewPagerAdapter().setReadAllOnEnd(current_position);

    }

    @Override
    public void onPostRequestError(Call<PostsModel> postsModel, Throwable t) {
        post_enqueue = false;
        getNewsListVerticalViewPagerAdapter().setReadAllOnEnd(current_position);
    }

    public NewsListVerticalViewPagerAdapter getNewsListVerticalViewPagerAdapter() {
        return newsListVerticalViewPagerAdapter;
    }

    public PostsRequest getRequestWithType(){

        if(old_post_request == null) return null;

        switch (newsListVerticalViewPagerAdapter.getNEWS_LIST_TYPE()){
            case PostListTypeHelper.CATEGORY_LIST:
                int category_id = (old_post_request.getCategory_id() != null && old_post_request.getCategory_id().size() > 0)? old_post_request.getCategory_id().get(0):0;
                postsRequest.setCategory_id(category_id);
                break;

            case PostListTypeHelper.TOPIC_LIST:
                int topic_id = (old_post_request.getTopic_id() != null && old_post_request.getTopic_id().size() > 0)? old_post_request.getTopic_id().get(0):0;
                postsRequest.setTopic_id(topic_id);
                break;

            case PostListTypeHelper.STATE_SEARCH_LIST:
                postsRequest.setTitle(old_post_request.getTitle());
                page_no = old_post_request.getPage();
                break;
        }
        return postsRequest;
    }

}
