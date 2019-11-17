package in.incourt.incourtnews.helpers;

import android.content.SharedPreferences;

import in.incourt.incourtnews.IncourtApplication;

/**
 * Created by bhavan on 2/23/17.
 */

public class PostListTypeHelper {

    static String FILTER_TYPE_KEY = "FILTER_TYPE_KEY";

    public static final String ALL_NEWS = "ALL_NEWS";

    public static final String FROM_FEED = "FROM_FEED";

    public static final String TRENDING_NEWS = "TRENDING_NEWS";

    public static final String TOP_STORIES = "TOP_STORIES";

    public static final String MY_BOOKMARKS = "BOOKMARKS";

    public static final String MY_FEED = "MY_FEED";

    public static final String MY_WIRES = "MY_WIRES";

    public static final String MY_LIKES = "MY_LIKES";

    public static final String CATEGORY_LIST = "CATEGORY_LIST";

    public static final String TOPIC_LIST = "TOPIC_LIST";

    public static final String STATE_LOADING = "STATE_LOADING";

    public static final String STATE_NO_RECORDS_FOUND = "STATE_NO_RECORDS_FOUND";

    public static final String STATE_NO_NETWORK = "STATE_NO_NETWORK";

    public static final String STATE_SEARCH_LIST = "STATE_SEARCH_LIST";

    public static final int GUIDE_LINE_SIX_TEEN_WORD_SCREEN = 0;

    public static final int GUIDE_LINE_SEARCH_SCREEN = 1;

    public static final int GUIDE_LINE_TYPIFY_SCREEN = 2;

    public static final int GUIDE_LINE_LOADING_NEWS = 3;

    public static final int FACEBOOK_LOGIN_FOR_POST_WIRED = 1000;

    public static final int FACEBOOK_LOGIN_FOR_POST_LIKE = 1001;

    public static void setFilterType(String type){
        SharedPreferences.Editor editor = IncourtApplication.getDefaultSharedPreferencesEditor();

        editor.putString(FILTER_TYPE_KEY, type);
        editor.commit();
    }

    public static String getFilterStatus(){
        return IncourtApplication.getDefaultSharedPreferences().getString(FILTER_TYPE_KEY, ALL_NEWS);
    }


    public static boolean getBookmarkStatus()
    {
        return getFilterStatus().equals(MY_BOOKMARKS);
    }

    public static boolean getWiredStatus(){
        return getFilterStatus().equals(MY_WIRES);
    }

    public static boolean getLikeStatus(){ return  getFilterStatus().equals( MY_LIKES); }

    public static boolean getMyFeedStatus(){
        return  getFilterStatus().equals(MY_FEED);
    }

    public static boolean getAllNewsStatus()
    {
        return  getFilterStatus().equals(ALL_NEWS);
    }

    public static boolean getTopStoriesStatus()
    {
        return getFilterStatus().equals(TOP_STORIES);
    }

    public static boolean getTrendingNewsStatus(){
        return  getFilterStatus().equals(TRENDING_NEWS);
    }

    public static String getTypeTitle(String type){
        String title = "All News";
        if(type.equals(MY_BOOKMARKS)) title = "Bookmarks";
        if(type.equals(MY_FEED)) title = "My Feed";
        if(type.equals(MY_LIKES)) title = "My Likes";
        if(type.equals(MY_WIRES)) title = "My Wires";
        if(type.equals(TRENDING_NEWS)) title = "Trending";
        if(type.equals(TOP_STORIES)) title = "Top Stories";
        return title;
    }

}
