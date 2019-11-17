package in.incourt.incourtnews.FabricKpi;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import in.incourt.incourtnews.core.sql.PostsSql;

/**
 * Created by admin on 4/24/2017.
 */

public class IncourtFabricWired extends IncourtFabricApplication {

    public static String LOG_POST_WIRED_KEY = "Post wired";

    public static String LOG_POST_WIRED_REMOVE_KEY = "Post wired remove";

    public static void logPostWired(PostsSql postsSql){
        Answers.getInstance().logCustom(new CustomEvent(LOG_POST_WIRED_KEY)
                .putCustomAttribute(LOG_KEY_POST_TITLE_NAME, postsSql.getTitle()));
    }

    public static void logPostWiredRemove(PostsSql postsSql){
        Answers.getInstance().logCustom(new CustomEvent(LOG_POST_WIRED_REMOVE_KEY)
                .putCustomAttribute(LOG_KEY_POST_TITLE_NAME, postsSql.getTitle()));
    }
}
