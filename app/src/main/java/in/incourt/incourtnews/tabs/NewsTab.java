package in.incourt.incourtnews.tabs;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

import in.incourt.incourtnews.R;
import in.incourt.incourtnews.activities.IncourtActivity;
import in.incourt.incourtnews.activities.SearchActivity;
import in.incourt.incourtnews.adapters.AdapterNews;
import in.incourt.incourtnews.core.models.PostsModel;
import in.incourt.incourtnews.core.request.PostRequestInterface;
import in.incourt.incourtnews.core.request.PostsRequest;
import in.incourt.incourtnews.core.sql.PostsSql;
import retrofit2.Call;

public class NewsTab extends Fragment implements PostRequestInterface.PostModels {

    Context context = getActivity();
    String get_cdn;
    List<PostsSql> postsSqlList;
    SearchActivity searchActivity;
    private int visibleItemCount;
    AdapterNews adapterNews;
    private int totalItemCount;
    private int pastVisiblesItems;
    private boolean loadingNextPage = false;
    PostsRequest postsRequest;
    public int current_page = 2;
    RelativeLayout loader;
    int hide = 0x00000008;
    int show = 0x00000000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_news, container, false);
        searchActivity = ((SearchActivity) getActivity());
        get_cdn = searchActivity.cdn;

        postsSqlList = searchActivity.postsSqlList;

        loader = (RelativeLayout) view.findViewById(R.id.loader);

        // Recycler view setup -->
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(false);

        final LinearLayoutManager llm = new GridLayoutManager(context, 1);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        adapterNews = new AdapterNews(searchActivity, this, postsSqlList);
        recyclerView.setAdapter(adapterNews);
        postsRequest = new PostsRequest(this).setTitle(searchActivity.getLastSearchTopic());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = llm.getChildCount();
                    totalItemCount = llm.getItemCount();
                    pastVisiblesItems = llm.findFirstVisibleItemPosition();

                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        if (!loadingNextPage) {
                            loadingNextPage = true;
                            postsRequest.setPage(current_page).fetchNextPage();
                            //Do pagination.. i.e. fetch new data
                           // loader.setVisibility(View.VISIBLE);
                            Progress(show);
                        }
                    }

                }
            }
        });
        // By Default Hide SoftKeyboard -->
        ((IncourtActivity) getActivity()).hideSoftKeyboard();
        return view;
    }

    @Override
    public void onFirstRunPosts(PostsModel postModel, PostsRequest postsRequest) {
        getNewPageToAppend(postModel, postsRequest);
    }

    @Override
    public void onPostRequestSuccess(PostsModel postsModel, PostsRequest postsRequest) {
        getNewPageToAppend(postsModel, postsRequest);
    }

    @Override
    public void onPostRequestNextPage(PostsModel postsModel, PostsRequest postsRequest) {
       getNewPageToAppend(postsModel, postsRequest);
    }

    public void getNewPageToAppend(PostsModel postsModel, PostsRequest postsRequest){
        List<PostsSql> postsSqls = PostsSql.parseData(postsModel, false);
        if (postsSqls != null && postsSqls.size() > 0) {
            adapterNews.getPostsSqlList().addAll(postsSqls);
            current_page++;
            adapterNews.notifyDataSetChanged();
        } else {
        }
        Progress(hide);
        loadingNextPage = false;
    }

    @Override
    public void onPostRequestError(Call<PostsModel> postsModel, Throwable t) {
        Progress(hide);
        loadingNextPage = false;
    }


    public void Progress(int visibility){
        loader.setVisibility(visibility);
    }


}