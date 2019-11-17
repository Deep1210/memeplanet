package in.incourt.incourtnews.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;
import in.incourt.incourtnews.FabricKpi.IncourtFabricSearch;
import in.incourt.incourtnews.R;
import in.incourt.incourtnews.adapters.AdapterRecentSearch;
import in.incourt.incourtnews.core.http.RestAdapter;
import in.incourt.incourtnews.core.models.PostWithTopicSearchModel;
import in.incourt.incourtnews.core.models.PostsModel;
import in.incourt.incourtnews.core.models.TopicsModel;
import in.incourt.incourtnews.core.request.PostRequestInterface;
import in.incourt.incourtnews.core.request.PostsRequest;
import in.incourt.incourtnews.core.sql.PostsSql;
import in.incourt.incourtnews.core.sql.TopicSQL;
import in.incourt.incourtnews.helpers.IncourtLoader;
import in.incourt.incourtnews.helpers.NetworkHelper;
import in.incourt.incourtnews.helpers.RecentSearchHelper;
import in.incourt.incourtnews.newslist.pager.NewsListHorizontalViewPager;
import in.incourt.incourtnews.pagers.SearchPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gurpreetsingh on 08/03/17.
 */

public class SearchActivity extends IncourtActivity implements PostRequestInterface.PostModelsSearch {

    @BindView(R.id.backarrow)
    ImageView backpressbtn;

    @BindView(R.id.searchtext)
    TextView searchtext;


    @BindView(R.id.recent_search_clear_text_view)
    TextView clearall;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LinearLayout tabsview, search_recent_view;
    public String cdn;
    public DrawerLayout drawer;
    String lastSearchTopic = "";
    RecyclerView recyclerView;
    public List<PostsSql> postsSqlList;
    public List<TopicSQL> topicSQLs;

    SearchPager adapter;
    NewsListHorizontalViewPager newsListHorizontalViewPager;
    Call<TopicsModel> topicRequest;
    IncourtLoader incourtLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.search_activity);

        viewPager = (ViewPager) findViewById(R.id.searchpager);
        tabsview = (LinearLayout) findViewById(R.id.tabsview);
        clearall = (TextView) findViewById(R.id.recent_search_clear_text_view);
        search_recent_view = (LinearLayout) findViewById(R.id.search_recent_view);

        // Recent search -->
        recyclerView = (RecyclerView) findViewById(R.id.recent_search_recycler_view);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager llm = new GridLayoutManager(SearchActivity.this, 1);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        recyclerView.setAdapter(new AdapterRecentSearch(SearchActivity.this, RecentSearchHelper.getRecentSearchIterator()));
        viewPager.clearOnPageChangeListeners();

        setFocusOn();
        drawerSetup();

        newsListHorizontalViewPager = (NewsListHorizontalViewPager) drawer.findViewById(R.id.serach_news_list_vertical_pager);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
            }
        });


        findViewById(R.id.searchtext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchtext = (EditText) findViewById(R.id.searchtext);
                searchtext.selectAll();
            }
        });
        findViewById(R.id.serch_layer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchtext = (EditText) findViewById(R.id.searchtext);
                searchtext.selectAll();
            }
        });

    }


    void drawerSetup() {

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    void setFocusOn() {
        findViewById(R.id.searchtext).requestFocus();
    }

    public void showNewsTabs() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new SearchPager(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabsview.setVisibility(View.VISIBLE);
        search_recent_view.setVisibility(View.GONE);
    }

    public void showSearchRecent() {
        tabsview.setVisibility(View.GONE);
        search_recent_view.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.backarrow)
    void setBackpressbtn() {
        onBackPressed();
    }

    @OnEditorAction(R.id.searchtext)
    protected boolean editorSubmit(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            startNewsSearch(searchtext.getText().toString());
            return true;
        }
        return false;
    }

    @OnTextChanged(R.id.searchtext)
    public void editorTextChange() {
        if (searchtext.getText().length() > 0) {
            listTopicsHints(searchtext.getText().toString());
        }
        if (search_recent_view.getVisibility() == View.GONE) {
            showSearchRecent();
        }

    }

    public void openDrawer(int position, int page_no) {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        newsListHorizontalViewPager.setNewsSearchAdapter(postsSqlList, lastSearchTopic, position, drawer, page_no);
        drawer.openDrawer(Gravity.RIGHT);
    }

    void listTopicsHints(String title) {
        HashMap<String, String> request = new HashMap<>();
        request.put("name", title);
        if (topicRequest != null && topicRequest.isExecuted()) {
            topicRequest.cancel();
        }
        topicRequest = RestAdapter.get().topicSearch(request);
        topicRequest.enqueue(new Callback<TopicsModel>() {
            @Override
            public void onResponse(Call<TopicsModel> call, Response<TopicsModel> response) {
                TopicsModel topicsModel = response.body();
                if (topicsModel != null) {
                    recyclerView.setAdapter(new AdapterRecentSearch(SearchActivity.this, topicsModel.getRecentSearchIterator()));
                }
            }

            @Override
            public void onFailure(Call<TopicsModel> call, Throwable t) {

            }
        });
    }

    public void startNewsSearch(String search_string) {
        this.lastSearchTopic = search_string;

        IncourtFabricSearch.logPostSearch(search_string);

        if (!NetworkHelper.state()) {
           // showNewsTabs();
        }

        if (search_string != null && search_string.length() <= 0) return;
        hideSoftKeyboard();
        getIncourtLoader().start();
        new PostsRequest(this).setName(search_string).fetchSearch();
        RecentSearchHelper.updateString(search_string);
        searchtext.setText(search_string);
    }

    void parseSearchResponce(PostWithTopicSearchModel postWithTopicSearchModel) {
        PostsModel postsModel = postWithTopicSearchModel.getPostModel();
        postsModel.setCdn_url(postWithTopicSearchModel.getCdn_url());
        postsSqlList = PostsSql.parseData(postsModel, false);
        topicSQLs = TopicSQL.parseData(postWithTopicSearchModel.getTopicsModel());
        getIncourtLoader().stop();
       // showNewsTabs();
    }


    public void topicSearchFilter(TopicSQL topicSQL) {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        newsListHorizontalViewPager.setNewsSearchTopicAdapter(topicSQL, drawer);
        drawer.openDrawer(Gravity.RIGHT);
    }

    @OnClick(R.id.recent_search_clear_text_view)
    void clearRecentSearch() {
        ((AdapterRecentSearch) recyclerView.getAdapter()).clearResult();
    }

    @Override
    public void onBackPressed() {
        if (getDrawer().isDrawerOpen(Gravity.RIGHT)) {
            if (getNewsListHorizontalViewPager().getCurrentItem() == 0) {
                getDrawer().closeDrawer(Gravity.RIGHT);
            } else {
                getNewsListHorizontalViewPager().setCurrentItem(0);
            }
        } else {
            finish();
        }
    }

    public DrawerLayout getDrawer() {
        return drawer;
    }

    public NewsListHorizontalViewPager getNewsListHorizontalViewPager() {
        return newsListHorizontalViewPager;
    }

    public String getLastSearchTopic() {
        return lastSearchTopic;
    }

    @Override
    public void onPostRequestErrorSearch(Call<PostWithTopicSearchModel> call, Throwable t) {
        parseSearchResponce(new PostWithTopicSearchModel());
    }

    @Override
    public void onSuccessSearchNextPage(PostWithTopicSearchModel postWithTopicSearchModel) {
        parseSearchResponce(postWithTopicSearchModel);
    }

    @Override
    public void onSuccessSearch(PostWithTopicSearchModel postWithTopicSearchModel) {
        parseSearchResponce(postWithTopicSearchModel);
    }

    public IncourtLoader getIncourtLoader(){
        if(incourtLoader == null) incourtLoader = IncourtLoader.init(this);
        return incourtLoader;
    }
}
