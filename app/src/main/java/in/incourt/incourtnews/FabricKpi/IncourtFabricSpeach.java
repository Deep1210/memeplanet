package in.incourt.incourtnews.FabricKpi;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import in.incourt.incourtnews.core.sql.PostsSql;

/**
 * Created by admin on 4/24/2017.
 */

public class IncourtFabricSpeach extends IncourtFabricApplication {

    public static String LOG_POST_SPEACH_KEY = "Post speech";

    public static String LOG_POST_SPEECH_STOP_KEY = "Post speech stop";

    public static void logPostSpeech(PostsSql postsSql){
        Answers.getInstance().logCustom(new CustomEvent(LOG_POST_SPEACH_KEY)
                .putCustomAttribute(LOG_KEY_POST_TITLE_NAME, postsSql.getTitle()));
    }

    public static void logPostSpeechStop(PostsSql postsSql){
        Answers.getInstance().logCustom(new CustomEvent(LOG_POST_SPEECH_STOP_KEY)
                .putCustomAttribute(LOG_KEY_POST_TITLE_NAME, postsSql.getTitle()));
    }

}
