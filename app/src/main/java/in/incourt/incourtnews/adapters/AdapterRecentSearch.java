package in.incourt.incourtnews.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import in.incourt.incourtnews.R;
import in.incourt.incourtnews.activities.SearchActivity;
import in.incourt.incourtnews.helpers.IncourtStringHelper;
import in.incourt.incourtnews.helpers.NoNetWorkStateHelper;
import in.incourt.incourtnews.helpers.PostListTypeHelper;
import in.incourt.incourtnews.helpers.RecentSearchHelper;
import in.incourt.incourtnews.helpers.interfaces.NoNetworkHelperInterface;


public class AdapterRecentSearch extends RecyclerView.Adapter<AdapterRecentSearch.ViewHolder> implements View.OnClickListener, NoNetworkHelperInterface {

    SearchActivity searchFragment;
    List<String> results;
    String LIST_TYPE_STATE = PostListTypeHelper.STATE_LOADING;

    View itemView;


    public AdapterRecentSearch(SearchActivity searchFragment, Iterator<String> results) {
        super();
        this.searchFragment = searchFragment;
        parseData(results);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        int layout_id = in.incourt.incourtnews.R.layout.adapter_recent_search_items;

        if(LIST_TYPE_STATE.equals(PostListTypeHelper.STATE_NO_NETWORK)) layout_id = in.incourt.incourtnews.R.layout.no_network_state;

        itemView = LayoutInflater.from(viewGroup.getContext()).inflate(layout_id, viewGroup, false);

        if(layout_id == in.incourt.incourtnews.R.layout.no_network_state){
            new NoNetWorkStateHelper(itemView, this);
        }

        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(results == null)return;
        String title = results.get(position).toString();
        TextView recent_title = (TextView) itemView.findViewById(in.incourt.incourtnews.R.id.newstitle);
        recent_title.setText(IncourtStringHelper.capitalize(title));
    }



    @Override
    public int getItemCount()
    {
        return (LIST_TYPE_STATE.equals(PostListTypeHelper.STATE_NO_NETWORK))? 1: results.size();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case in.incourt.incourtnews.R.id.columnview:
                searchFragment.startNewsSearch(((TextView) view.findViewById(R.id.newstitle)).getText().toString());
            break;
        }
    }

    @Override
    public void onClickTryAgain() {

    }

    @Override
    public void onClickSetting() {
        NoNetWorkStateHelper.openSettingActivity();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout columnview;

        public ViewHolder(View itemView) {
            super(itemView);
            columnview = (LinearLayout) itemView.findViewById(in.incourt.incourtnews.R.id.columnview);
            if(columnview != null) columnview.setOnClickListener(AdapterRecentSearch.this);
        }
    }


    public void parseData(Iterator<String> results){
        List<String> recentSearch = new ArrayList<>();
        Iterator<String> iterator = results;
        if(iterator != null){
            for(;iterator.hasNext();) {
                recentSearch.add(iterator.next());
            }
        }
        this.results = recentSearch;
    }

    public void clearResult(){
        this.results = new ArrayList<>();
        RecentSearchHelper.clearRecentSearch();
        notifyDataSetChanged();
    }
}

