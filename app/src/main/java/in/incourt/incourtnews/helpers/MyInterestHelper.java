package in.incourt.incourtnews.helpers;

import android.content.SharedPreferences;

import java.util.ArrayList;

import in.incourt.incourtnews.IncourtApplication;

/**
 * Created by bhavan on 3/6/17.
 */

public class MyInterestHelper {

    static String MY_INTEREST_KEY = "MY_INTEREST_KEY";

    public static void save(ArrayList<Integer> interest){
        SharedPreferences.Editor editor = IncourtApplication.getDefaultSharedPreferencesEditor();
        editor.putString(MY_INTEREST_KEY, interest.toString());
        editor.commit();
    }

    public static String get() {
        String save_interest = getRaw();
        save_interest = save_interest.replace("[", "");
        save_interest = save_interest.replace("]", "");
        return save_interest;
    }

    public static String getRaw() {
        return IncourtApplication.getDefaultSharedPreferences().getString(MY_INTEREST_KEY, null);
    }
}
