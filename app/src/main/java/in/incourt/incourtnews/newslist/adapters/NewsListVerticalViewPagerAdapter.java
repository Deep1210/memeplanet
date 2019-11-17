package in.incourt.incourtnews.newslist.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import in.incourt.incourtnews.FabricKpi.IncourtFabricNotificationClick;
import in.incourt.incourtnews.IncourtApplication;
import in.incourt.incourtnews.R;
import in.incourt.incourtnews.core.models.AdvertisementModel;
import in.incourt.incourtnews.core.models.PostsModel;
import in.incourt.incourtnews.core.request.PostsRequest;
import in.incourt.incourtnews.core.sql.PostsSql;
import in.incourt.incourtnews.helpers.AdvertisementHelper;
import in.incourt.incourtnews.helpers.ImageHelper;
import in.incourt.incourtnews.helpers.IncourtToastHelprer;
import in.incourt.incourtnews.helpers.MyInterestHelper;
import in.incourt.incourtnews.helpers.NetworkHelper;
import in.incourt.incourtnews.helpers.NewsPostPaginationHelper;
import in.incourt.incourtnews.helpers.NoNetWorkStateHelper;
import in.incourt.incourtnews.helpers.PostListTypeHelper;
import in.incourt.incourtnews.helpers.interfaces.NoNetworkHelperInterface;
import in.incourt.incourtnews.layouts.PostLayoutPage;
import in.incourt.incourtnews.newslist.pager.NewsListVerticalViewPager;

/**
 * Created by bhavan on 2/24/17.
 */

public class NewsListVerticalViewPagerAdapter extends PagerAdapter implements NoNetworkHelperInterface {

    LayoutInflater news_list_inflater;
    View itemView;
    List<PostsSql> postsSqlList;
    PostLayoutPage postLayoutPage;
    NewsListVerticalViewPager newsListVerticalViewPager;

    String NEWS_LIST_TYPE = PostListTypeHelper.ALL_NEWS;

    String NEWS_LIST_STATE = PostListTypeHelper.STATE_LOADING;

    AdvertisementHelper advertisementHelper;

    NewsPostPaginationHelper newsPostPaginationHelper;

    public NewsListVerticalViewPagerAdapter(NewsListVerticalViewPager newsListVerticalViewPager, Context context, @Nullable String NEWS_LIST_TYPE) {
        news_list_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.NEWS_LIST_TYPE = (NEWS_LIST_TYPE != null) ? NEWS_LIST_TYPE : PostListTypeHelper.getFilterStatus();

        this.newsListVerticalViewPager = newsListVerticalViewPager;

        setPostsSqlList(getPostWithType(0));

        postLayoutPage = new PostLayoutPage(this);
    }

    public NewsListVerticalViewPagerAdapter() {

    }


    public List<PostsSql> getPostWithType(int page_no) {

        if (NetworkHelper.state() && IncourtApplication.isFirstRunFetchFromServer() && checkForServerFetch(NEWS_LIST_TYPE)) {
            getNewsListVerticalViewPager().getFirstRunPosts();
            IncourtApplication.setFirstRunFetchFromServer(false);
            return new ArrayList<>();
        } else if (IncourtApplication.isFirstRunFetchFromServer() && checkForServerFetch(NEWS_LIST_TYPE)) {
            IncourtToastHelprer.showUnableToRefresh();
        }

        PostsSql notificationPostSql = checkIsCallFromNotification();

        List<PostsSql> postsSqls = null;

        if (NEWS_LIST_TYPE.equals(PostListTypeHelper.MY_BOOKMARKS))
            postsSqls = PostsSql.getBookmarksList(page_no, (int) notificationPostSql.getPost_id());

        else if (NEWS_LIST_TYPE.equals(PostListTypeHelper.MY_FEED)) {
            if (MyInterestHelper.getRaw() != null)
                postsSqls = PostsSql.getMyFeed(page_no, (int) notificationPostSql.getPost_id());
            else
                IncourtToastHelprer.showToast("Please customize your Feed first");
        }
        else if (NEWS_LIST_TYPE.equals(PostListTypeHelper.MY_WIRES))
            postsSqls = PostsSql.getMyWire(page_no, (int) notificationPostSql.getPost_id());

        else if (NEWS_LIST_TYPE.equals(PostListTypeHelper.MY_LIKES))
            postsSqls = PostsSql.getLikeList(page_no, (int) notificationPostSql.getPost_id());

        else if (NEWS_LIST_TYPE.equals(PostListTypeHelper.CATEGORY_LIST) || NEWS_LIST_TYPE.equals(PostListTypeHelper.TOPIC_LIST))
            return new ArrayList<>();

        else if (NEWS_LIST_TYPE.equals(PostListTypeHelper.TOP_STORIES))
            postsSqls = PostsSql.getTopStories(page_no, (int) notificationPostSql.getPost_id());

        else if (NEWS_LIST_TYPE.equals(PostListTypeHelper.TRENDING_NEWS))
            postsSqls = PostsSql.getTrending(page_no, (int) notificationPostSql.getPost_id());

        else if (NEWS_LIST_TYPE.equals(PostListTypeHelper.ALL_NEWS))
            postsSqls = PostsSql.getList(page_no, (int) notificationPostSql.getPost_id());

        if (postsSqls != null && notificationPostSql.getPost_id() > 0) {
            postsSqls.add(0, notificationPostSql);
        }

        if (checkForServerFetch(NEWS_LIST_TYPE) && postsSqls.size() <= 0) {
            noRecordFoundOnLocal();
        }

        return postsSqls;
    }

    private void noRecordFoundOnLocal() {
        getNewsListVerticalViewPager().noRecordFoundOnLocal(NEWS_LIST_TYPE);
    }

    private boolean checkForServerFetch(String type) {
        if (type.equals(PostListTypeHelper.ALL_NEWS)) return true;
        if (type.equals(PostListTypeHelper.MY_FEED)) return true;
        if (type.equals(PostListTypeHelper.TOP_STORIES)) return true;
        if (type.equals(PostListTypeHelper.TRENDING_NEWS)) return true;
        return false;
    }

    private PostsSql checkIsCallFromNotification() {
        if (IncourtApplication.isCallFromPushNotification()) {
            setNEWS_LIST_TYPE(PostListTypeHelper.ALL_NEWS);
            IncourtApplication.setCallFromPushNotification(false);
            PostsSql postsSql = PostsSql.getPostsSqlFormSerialize(IncourtApplication.getCallFromPushNotificationString());
            IncourtFabricNotificationClick.logNotificatinClick(postsSql);
            return postsSql;
        }
        return new PostsSql();
    }


    @Override
    public int getCount() {
        if (postsSqlList == null || postsSqlList.size() <= 0) return 1;
        return (postsSqlList.size() + 1);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        if (postsSqlList == null || postsSqlList.size() <= 0) {
            itemView = getNewsLoadingState(container, position);
        } else if ((getPostsSqlList().size()) == position) {
            itemView = getLastLoadingState(container, position);
        } else {
            PostsSql postsSql = postsSqlList.get(position);
            if (postsSql.hasAdvertisementModel()) {
                itemView = news_list_inflater.inflate(in.incourt.incourtnews.R.layout.advertisement_layout, container, false);
                ImageHelper.loadImage(postsSql.getAdvertisementModel().getImage_url(), (ImageView) itemView.findViewById(in.incourt.incourtnews.R.id.advertisement_image));
                setAdvertisementHelper(AdvertisementHelper.init(itemView, postsSql.getAdvertisementModel()));
            } else {
                itemView = news_list_inflater.inflate(in.incourt.incourtnews.R.layout.content_main, container, false);
                postLayoutPage.setUpLayout(itemView, postsSql);
            }
        }

        itemView.setTag(position);

        container.addView(itemView);

        if (position == 0 && postsSqlList.size() > 0) {
            getNewsListVerticalViewPager().onPageChangeEvent();
        }

        return itemView;
    }

    private View getLastLoadingState(ViewGroup container, int position) {

        int layout_id = R.layout.no_morestories;

        if (!getNewsListVerticalViewPager().checkIfAutoUpdateImplement() && NetworkHelper.state()) {
            if (getNewsPostPaginationHelper() != null && getNewsPostPaginationHelper().is_load_more) {
                layout_id = R.layout.loading_news;
                getNewsPostPaginationHelper().onNextPageSelected(position, getCount());
            }
        }
        View view = news_list_inflater.inflate(layout_id, container, false);
        return view;
    }

    public void onNextPageAppends(List<PostsSql> postsSqls, int position) {
        getPostsSqlList().addAll(postsSqls);
        notifyDataSetChanged();
        getNewsListVerticalViewPager().invalidateCurrentView();
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    View getNewsLoadingState(ViewGroup container, int position) {

        int layout_id = R.layout.loading_news;

        if (NEWS_LIST_STATE.equals(PostListTypeHelper.STATE_NO_RECORDS_FOUND)) {
            layout_id = R.layout.no_record;
        }


        if (NEWS_LIST_STATE.equals(PostListTypeHelper.STATE_NO_NETWORK)) {
            layout_id = R.layout.no_network_state;
        }

        if (NEWS_LIST_TYPE.equals(PostListTypeHelper.MY_BOOKMARKS)) {
            layout_id = R.layout.no_bookmark;
        }

        if (NEWS_LIST_TYPE.equals(PostListTypeHelper.MY_LIKES)) {
            layout_id = R.layout.no_likes;
        }

        if (NEWS_LIST_TYPE == PostListTypeHelper.MY_WIRES) {
            layout_id = R.layout.no_wire;
        }

        if ((NEWS_LIST_TYPE.equals(PostListTypeHelper.TRENDING_NEWS) || NEWS_LIST_TYPE.equals(PostListTypeHelper.TOP_STORIES)) && NEWS_LIST_STATE.equals(PostListTypeHelper.STATE_NO_RECORDS_FOUND)) {
            layout_id = R.layout.no_record;
        }


        View view = news_list_inflater.inflate(layout_id, container, false);

        if (R.layout.no_network_state == layout_id) {
            new NoNetWorkStateHelper(view, this);
        }

        getNewsListHorizontalViewPagerAdapter().setWeb_view_available(false);

        return view;

    }

    public void setPostsSqlList(List<PostsSql> postsSqlList) {
        this.postsSqlList = postsSqlList;
        if (postsSqlList != null) {
            List<AdvertisementModel.Advertisement> advertisementList = AdvertisementHelper.getAdvertisementList();
            if (advertisementList != null) {
                implementAdvertisementInPostSql(advertisementList);
            }
        }
        notifyDataSetChanged();
    }

    public void setNEWS_LIST_TYPE(String NEWS_LIST_TYPE) {
        this.NEWS_LIST_TYPE = NEWS_LIST_TYPE;
    }

    public void setNEWS_LIST_STATE(String NEWS_LIST_STATE) {
        this.NEWS_LIST_STATE = NEWS_LIST_STATE;
    }

    public NewsListVerticalViewPager getNewsListVerticalViewPager() {
        return newsListVerticalViewPager;
    }

    public NewsListHorizontalViewPagerAdapter getNewsListHorizontalViewPagerAdapter() {
        return getNewsListVerticalViewPager().getNewsListHorizontalViewPagerAdapter();
    }

    public String getNEWS_LIST_TYPE() {
        return NEWS_LIST_TYPE;
    }

    public String getNEWS_LIST_STATE() {
        return NEWS_LIST_STATE;
    }

    public List<PostsSql> getPostsSqlList() {
        return postsSqlList;
    }

    public PostLayoutPage getPostLayoutPage() {
        return postLayoutPage;
    }

    @Override
    public void onClickTryAgain() {
        if (NetworkHelper.state()) {
            getNewsListVerticalViewPager().setAdapter(new NewsListVerticalViewPagerAdapter(getNewsListVerticalViewPager(), getNewsListVerticalViewPager().getContext(), getNEWS_LIST_TYPE()));
            getNewsListVerticalViewPager().setupRestCall(getNewsListVerticalViewPager().getPostsRequest());
        } else {
            IncourtToastHelprer.showNoNetwork();
        }
    }

    @Override
    public void onClickSetting() {
        NoNetWorkStateHelper.openSettingActivity();
    }

    public AdvertisementHelper getAdvertisementHelper() {
        return advertisementHelper;
    }

    public void setAdvertisementHelper(AdvertisementHelper advertisementHelper) {
        this.advertisementHelper = advertisementHelper;
    }

    public void implementAdvertisementInPostSql(List<AdvertisementModel.Advertisement> advertisements) {
        if (getPostsSqlList() == null) return;
        if (getPostsSqlList().size() <= 0) return;
        if (advertisements != null && advertisements.size() > 0) {
            for (int i = 0; i < advertisements.size(); i++) {
                String[] advertisementList = advertisements.get(i).getPositionList();
                if (advertisementList.length > 0) {
                    for (int y = 0; y < advertisementList.length; y++) {
                        if (advertisementList[y].length() > 0 && getPostsSqlList().size() >= Integer.parseInt(advertisementList[y]))
                            getPostsSqlList().add(Integer.parseInt(advertisementList[y]), getAdvertisementPostSql(advertisements.get(i)));
                    }
                }
            }
        }
    }

    public PostsSql getAdvertisementPostSql(AdvertisementModel.Advertisement advertisement) {
        PostsSql postsSql = new PostsSql();
        postsSql.setAdvertisementModel(advertisement);
        return postsSql;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void getFirstRunApp(PostsModel postModel) {
        PostsSql.extractRequest(postModel, true);
        AdvertisementHelper.update(postModel);
        setPostsSqlList(getPostWithType(0));
        notifyDataSetChanged();
    }

    public void updatedPostsList(PostsModel postsModel) {
        setNEWS_LIST_STATE(PostListTypeHelper.STATE_NO_RECORDS_FOUND);
        setPostsSqlList(PostsSql.parseData(postsModel, true));
        notifyDataSetChanged();
    }


    public void appendNextPage(PostsModel postsModel) {
        getPostsSqlList().addAll(PostsSql.parseData(postsModel, true));
        notifyDataSetChanged();
        getNewsListVerticalViewPager().invalidateCurrentView();
    }

    public NewsPostPaginationHelper getNewsPostPaginationHelper() {
        return newsPostPaginationHelper;
    }


    public void adapterChange(PostsRequest postsRequest) {
        newsPostPaginationHelper = new NewsPostPaginationHelper(this, postsRequest);
    }

    public void setReadAllOnEnd(int current_position) {
        notifyDataSetChanged();
        getNewsListVerticalViewPager().invalidateCurrentView();
    }
}
