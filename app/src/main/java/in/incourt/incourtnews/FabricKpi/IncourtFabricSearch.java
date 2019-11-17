package in.incourt.incourtnews.FabricKpi;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

/**
 * Created by bhavan on 5/15/17.
 */

public class IncourtFabricSearch extends IncourtFabricApplication {

    public static String LOG_POST_SEARCH_KEY = "Search";
    public static String LOG_POST_SEARCH_STRING = "Search Keywords";

    public static void logPostSearch(String serach){
        Answers.getInstance().logCustom(new CustomEvent(LOG_POST_SEARCH_KEY)
                .putCustomAttribute(LOG_POST_SEARCH_STRING, serach));
    }

}
