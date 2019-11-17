package in.incourt.incourtnews.tabs;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.incourt.incourtnews.R;
import in.incourt.incourtnews.activities.SearchActivity;
import in.incourt.incourtnews.adapters.TopicTabListAdapter;
import in.incourt.incourtnews.core.http.RestAdapter;
import in.incourt.incourtnews.core.models.TopicsModel;
import in.incourt.incourtnews.core.sql.TopicSQL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class TopicsTab extends Fragment {

    //  SearchFragment searchFragment;
    Context context = getActivity();
    SearchActivity searchActivity;
    List<TopicSQL> topicSQLs;
    TopicTabListAdapter topicTabListAdapter;
    private int visibleItemCount;
    private int totalItemCount;
    private int pastVisiblesItems;
    private boolean loadingNextPage = false;
    int current_page = 2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(in.incourt.incourtnews.R.layout.tab_topics, container, false);
        searchActivity = ((SearchActivity) getActivity());
        topicSQLs = searchActivity.topicSQLs;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            topicSQLs.sort(new Comparator<TopicSQL>() {
                @Override
                public int compare(TopicSQL topicSQL, TopicSQL t1) {
                    int value;
                    if(topicSQL.getName().length()>t1.getName().length()){
                        value =1;
                    }else{
                        value =-1;
                    }
                    Log.e("Value : ",""+value);
                    return value;
                }
            });
        }
        setUpTopicButtons((RecyclerView) view.findViewById(R.id.topic_tab_container));
        return view;
    }

    void setUpTopicButtons(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);

        final GridLayoutManager llm = new GridLayoutManager(context,2);
        //llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        topicTabListAdapter = new TopicTabListAdapter(searchActivity, topicSQLs);
        recyclerView.setAdapter(topicTabListAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //if(newState == RecyclerView.SCROLL)
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = llm.getChildCount();
                    totalItemCount = llm.getItemCount();
                    pastVisiblesItems = llm.findFirstVisibleItemPosition();

                    if (pastVisiblesItems == totalItemCount - 1) {
                        if(!loadingNextPage) {
                            loadingNextPage = true;
                            getNextPage(searchActivity.getLastSearchTopic(), current_page);
                        }
                    }
                }
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
    }

    public void getNextPage(String title, int page_no){
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("name", title);
        stringStringHashMap.put("page", String.valueOf(page_no));
        RestAdapter.get().topicSearch(stringStringHashMap).enqueue(new Callback<TopicsModel>() {
            @Override
            public void onResponse(Call<TopicsModel> call, Response<TopicsModel> response) {
                List<TopicSQL> topicSQLs = TopicSQL.parseData(response.body());

                if(topicSQLs != null && topicSQLs.size() > 0){
                    topicTabListAdapter.getTopicSQLs().addAll(topicSQLs);
                    topicTabListAdapter.notifyDataSetChanged();
                    current_page++;
                    loadingNextPage = false;
                }
            }

            @Override
            public void onFailure(Call<TopicsModel> call, Throwable t) {

            }
        });
    }

}





