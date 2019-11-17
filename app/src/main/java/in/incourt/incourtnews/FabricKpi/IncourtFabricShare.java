package in.incourt.incourtnews.FabricKpi;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import in.incourt.incourtnews.core.sql.PostsSql;

/**
 * Created by bhavan on 5/15/17.
 */

public class IncourtFabricShare extends IncourtFabricApplication {

    public static String LOG_POST_SHARE_KEY = "Post Share";

    public static void logPostShare(PostsSql postsSql){

        Answers.getInstance().logCustom(new CustomEvent(LOG_POST_SHARE_KEY)
                .putCustomAttribute(LOG_KEY_POST_TITLE_NAME, postsSql.getTitle()));

    }

}
