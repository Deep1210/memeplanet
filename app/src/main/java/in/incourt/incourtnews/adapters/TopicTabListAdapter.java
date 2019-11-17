package in.incourt.incourtnews.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import in.incourt.incourtnews.R;
import in.incourt.incourtnews.activities.SearchActivity;
import in.incourt.incourtnews.core.sql.TopicSQL;
import in.incourt.incourtnews.helpers.IncourtStringHelper;
import in.incourt.incourtnews.helpers.IncourtToastHelprer;
import in.incourt.incourtnews.helpers.NetworkHelper;
import in.incourt.incourtnews.helpers.NoNetWorkStateHelper;
import in.incourt.incourtnews.helpers.interfaces.NoNetworkHelperInterface;

import java.util.ArrayList;
import java.util.List;

public class TopicTabListAdapter extends RecyclerView.Adapter<TopicTabListAdapter.ViewHolder> implements NoNetworkHelperInterface {

    Context context;

    List<TopicSQL> topicSQLs;

    SearchActivity searchActivity;

    public TopicTabListAdapter(SearchActivity searchFragment, List<TopicSQL> topicSQLs) {
        super();
        this.topicSQLs = topicSQLs;
        if(this.topicSQLs == null){
            this.topicSQLs = new ArrayList<>();
        }
        this.searchActivity = searchFragment;
        this.context = searchFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {

        int layout_id = R.layout.topic_search_layout;

        if(topicSQLs.size() <= 0) layout_id = R.layout.no_record;

        if(!getNetworkState()) layout_id = R.layout.no_network_state;

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(layout_id, viewGroup, false);

        if(layout_id == R.layout.no_network_state) new NoNetWorkStateHelper(v, this);

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if(getNetworkState() && topicSQLs.size() > 0) {
            viewHolder.topic_button.setText(IncourtStringHelper.capitalize(topicSQLs.get(position).getName()));
            viewHolder.topic_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    searchActivity.topicSearchFilter(topicSQLs.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(!getNetworkState() || topicSQLs.size() <= 0) return 1;
        return topicSQLs.size();
    }

    @Override
    public void onClickTryAgain() {
        if(getNetworkState()) {
            searchActivity.startNewsSearch(searchActivity.getLastSearchTopic());
        }else{
            IncourtToastHelprer.showNoNetwork();
        }
    }

    @Override
    public void onClickSetting() {
        NoNetWorkStateHelper.openSettingActivity();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Button topic_button;

        public ViewHolder(View itemView) {
            super(itemView);
            topic_button = (Button) itemView.findViewById(in.incourt.incourtnews.R.id.search_topic_button);
        }
    }

    public boolean getNetworkState(){
        return NetworkHelper.state();
    }

    public List<TopicSQL> getTopicSQLs() {
        return topicSQLs;
    }

    public void setTopicSQLs(List<TopicSQL> topicSQLs) {
        this.topicSQLs = topicSQLs;
    }
}

