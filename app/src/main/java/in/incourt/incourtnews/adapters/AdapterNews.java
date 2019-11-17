package in.incourt.incourtnews.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.incourt.incourtnews.R;
import in.incourt.incourtnews.activities.SearchActivity;
import in.incourt.incourtnews.core.sql.PostImageSql;
import in.incourt.incourtnews.core.sql.PostsSql;
import in.incourt.incourtnews.helpers.ImageHelper;
import in.incourt.incourtnews.helpers.IncourtDateHelper;
import in.incourt.incourtnews.helpers.IncourtToastHelprer;
import in.incourt.incourtnews.helpers.NetworkHelper;
import in.incourt.incourtnews.helpers.NoNetWorkStateHelper;
import in.incourt.incourtnews.helpers.interfaces.NoNetworkHelperInterface;
import in.incourt.incourtnews.tabs.NewsTab;

public class AdapterNews extends RecyclerView.Adapter<AdapterNews.ViewHolder> implements NoNetworkHelperInterface {

    Context context;
    List<PostsSql> postsSqlList;
    SearchActivity searchActivity;
    NewsTab newsTab;

    public AdapterNews(SearchActivity searchActivity, NewsTab newsTab, List<PostsSql> postsSqlList) {
        super();
        this.postsSqlList = postsSqlList;
        if(this.postsSqlList == null){
            this.postsSqlList = new ArrayList<>();
        }
        this.searchActivity = searchActivity;
        this.context = searchActivity;
        this.newsTab = newsTab;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {

        int layout_id = in.incourt.incourtnews.R.layout.adapter_news_items;

        if(postsSqlList.size() <= 0) layout_id = in.incourt.incourtnews.R.layout.no_record;

        if(!getNetworkState()) layout_id = in.incourt.incourtnews.R.layout.no_network_state;

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(layout_id, viewGroup, false);

        if(layout_id == in.incourt.incourtnews.R.layout.no_network_state) new NoNetWorkStateHelper(v, this);

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        if(getNetworkState() && postsSqlList.size() > 0){
            viewHolder.newstitle.setText(postsSqlList.get(position).getTitle());
            List<PostImageSql> postImageSqls = postsSqlList.get(position).getPostImages();

            viewHolder.columnview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    searchActivity.openDrawer(position, newsTab.current_page);
                }
            });

            viewHolder.recent_search_news_time.setText(IncourtDateHelper.getTimeAgo(postsSqlList.get(position).getPubDate()));

            if (PostsSql.hasVideo(postsSqlList.get(position))) {
                viewHolder.video_play_icon.setVisibility(View.VISIBLE);
                viewHolder.multiple_image_icon.setVisibility(View.GONE);
                ImageHelper.loadImage("https://img.youtube.com/vi/" + postImageSqls.get(0).getImage_name() + "/0.jpg", viewHolder.newsimg);
            }else{
                viewHolder.video_play_icon.setVisibility(View.INVISIBLE);
                viewHolder.multiple_image_icon.setVisibility(View.GONE);
                String image_name = "";
                if(postImageSqls.size() > 0){
                    image_name = (ImageHelper.getHdImageStatus()) ? postImageSqls.get(0).getImage_name_hd() : postImageSqls.get(0).getImage_name();
                    if(postImageSqls.size() > 1){
                        viewHolder.multiple_image_icon.setVisibility(View.VISIBLE);
                    }
                }
                ImageHelper.loadImage( image_name, viewHolder.newsimg);
            }
        }
    }

    @Override
    public int getItemCount() {
        if(postsSqlList.size() <= 0 || !NetworkHelper.state()){
            return 1;
        }
        return postsSqlList.size();
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
        ImageView newsimg;
        TextView newstitle, recent_search_news_time;
        LinearLayout columnview;
        ImageView video_play_icon, multiple_image_icon;

        public ViewHolder(View itemView) {
            super(itemView);
            if(getNetworkState()) {
                columnview = (LinearLayout) itemView.findViewById(R.id.columnview);
                newsimg = (ImageView) itemView.findViewById(R.id.newsimg);
                newstitle = (TextView) itemView.findViewById(R.id.newstitle);
                recent_search_news_time = (TextView) itemView.findViewById(R.id.recent_search_news_time);
                video_play_icon = (ImageView) itemView.findViewById(R.id.recent_serach_view_video_icon);
                multiple_image_icon = (ImageView) itemView.findViewById(R.id.multiple_image_icon);
            }
        }
    }

    public boolean getNetworkState(){
        return NetworkHelper.state();
    }

    public List<PostsSql> getPostsSqlList() {
        return postsSqlList;
    }

    public void setPostsSqlList(List<PostsSql> postsSqlList) {
        this.postsSqlList = postsSqlList;
    }
}

