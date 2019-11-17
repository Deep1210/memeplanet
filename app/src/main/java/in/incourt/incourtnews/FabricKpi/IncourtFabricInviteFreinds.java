package in.incourt.incourtnews.FabricKpi;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

/**
 * Created by bhavan on 5/16/17.
 */

public class IncourtFabricInviteFreinds extends IncourtFabricApplication {

    public static String LOG_POST_INVITE_FRIENDS_KEY = "Invite Friends";

    public static void logInviteFriends(){
        Answers.getInstance().logCustom(new CustomEvent(LOG_POST_INVITE_FRIENDS_KEY));
    }
}
