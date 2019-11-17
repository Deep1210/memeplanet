package in.incourt.incourtnews.FabricKpi;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import in.incourt.incourtnews.core.sql.PostsSql;

/**
 * Created by bhavan on 6/5/17.
 */

public class IncourtFabricNotificationClick extends IncourtFabricApplication {

    public static String NOTIFICATION_CLICK_KEY = "Notification Click";

    public static void logNotificatinClick(PostsSql postsSql){
        Answers.getInstance().logCustom(
                new CustomEvent(NOTIFICATION_CLICK_KEY)
                        .putCustomAttribute(LOG_KEY_POST_TITLE_NAME, postsSql.getTitle())
        );
    }

}
