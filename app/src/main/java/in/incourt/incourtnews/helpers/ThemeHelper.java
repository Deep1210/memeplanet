package in.incourt.incourtnews.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.TaskStackBuilder;

import in.incourt.incourtnews.activities.IncourtActivity;
import in.incourt.incourtnews.activities.IncourtLauncherActivity;

/**
 * Created by bhavan on 12/29/16.
 */

public class ThemeHelper {

    public static String theme_key = "CURRENT_THEME";
    public static SharedPreferences sharedPreferences;
    public static int active(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getBoolean(theme_key, false)? in.incourt.incourtnews.R.style.NightTheme: in.incourt.incourtnews.R.style.DayTheme;

    }

    public static boolean switchTheme(Context context, IncourtActivity activity, int theme_id){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spEdit =  sharedPreferences.edit();
        spEdit.putBoolean(theme_key, (theme_id == in.incourt.incourtnews.R.style.NightTheme)? true: false);
        changeTheme(activity);
        context.setTheme(ThemeHelper.active(context));
        return spEdit.commit();
    }

    public static boolean toggleStatus(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(theme_key, false);
    }

    public static void changeTheme(IncourtActivity activity){
        Intent intent = activity.getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.finish();
        activity.overridePendingTransition(in.incourt.incourtnews.R.anim.fade_in, in.incourt.incourtnews.R.anim.fade_out);
        activity.startActivity(intent);

        TaskStackBuilder.create(activity.getApplicationContext())
                .addNextIntent(new Intent(activity, IncourtLauncherActivity.class))
                .addNextIntent(activity.getIntent())
                .startActivities();

    }
}
