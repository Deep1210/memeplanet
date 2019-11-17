package in.incourt.incourtnews.FabricKpi;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import in.incourt.incourtnews.core.sql.PostsSql;

/**
 * Created by admin on 4/22/2017.
 */

public class IncourtFabricLike extends IncourtFabricApplication {

    public static String LOG_POST_LIKE_KEY = "Post like";

    public static String LOG_POST_DIS_LIKE_KEY = "Post dislike";

    public static void logPostLike(PostsSql postsSql){
        Answers.getInstance().logCustom(new CustomEvent(LOG_POST_LIKE_KEY)
                .putCustomAttribute(LOG_KEY_POST_TITLE_NAME, postsSql.getTitle()));
    }

    public static void logPostDisLike(PostsSql postsSql){
        Answers.getInstance().logCustom(new CustomEvent(LOG_POST_DIS_LIKE_KEY)
                .putCustomAttribute(LOG_KEY_POST_TITLE_NAME, postsSql.getTitle()));
    }

}
