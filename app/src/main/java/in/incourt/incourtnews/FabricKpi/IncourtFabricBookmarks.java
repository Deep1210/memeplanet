package in.incourt.incourtnews.FabricKpi;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import in.incourt.incourtnews.core.sql.PostsSql;

/**
 * Created by admin on 4/24/2017.
 */

public class IncourtFabricBookmarks extends IncourtFabricApplication {

    public static String LOG_POST_BOOKMARKS_KEY = "Post bookmarks";

    public static String LOG_POST_BOOKMARKS_REMOVE_KEY = "Post bookmarks remove";

    public static void logPostBookmarks(PostsSql postsSql){
        Answers.getInstance().logCustom(new CustomEvent(LOG_POST_BOOKMARKS_KEY)
                .putCustomAttribute(LOG_KEY_POST_TITLE_NAME, postsSql.getTitle()));
    }

    public static void logPostBookmarksRemove(PostsSql postsSql){
        Answers.getInstance().logCustom(new CustomEvent(LOG_POST_BOOKMARKS_REMOVE_KEY)
                .putCustomAttribute(LOG_KEY_POST_TITLE_NAME, postsSql.getTitle()));
    }
}
