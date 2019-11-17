package in.incourt.incourtnews.helpers;

import android.content.SharedPreferences;

import in.incourt.incourtnews.IncourtApplication;

/**
 * Created by bhavan on 2/19/17.
 */

public class IncourtNotificationHelper {

    static String NOTIFICATION_STATUS_KEY = "NOTIFICATION_STATUS_KEY";

    public static boolean getStatus(){
        return IncourtApplication.getDefaultSharedPreferences().getBoolean(NOTIFICATION_STATUS_KEY, true);
    }

    public static void enable(){
        changeStatus(true);
    }

    public static void disable(){
        changeStatus(false);
    }

    static void changeStatus(boolean status){
        SharedPreferences.Editor editor = IncourtApplication.getDefaultSharedPreferencesEditor();
        editor.putBoolean(NOTIFICATION_STATUS_KEY, status);
        editor.commit();
    }






}
