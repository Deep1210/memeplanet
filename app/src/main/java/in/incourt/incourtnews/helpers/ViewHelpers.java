package in.incourt.incourtnews.helpers;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.incourt.incourtnews.IncourtApplication;

/**
 * Created by bhavan on 1/25/17.
 */

public class ViewHelpers {

    static String KEY_FIRST_RUN = "FIRST_RUN_APP";

    public static boolean visibilityChange(LinearLayout linearLayout, TextView textView) {
        if (linearLayout.getVisibility() == View.VISIBLE) {
            linearLayout.setVisibility(View.GONE);
            textView.setText("View more +");
            return false;
        } else if (linearLayout.getVisibility() == View.GONE) {
            linearLayout.setVisibility(View.VISIBLE);
            textView.setText("Show less -");
            return true;
        }
        return false;
    }

    public static boolean getFirstRun(){
        SharedPreferences sharedPreferences = IncourtApplication.getDefaultSharedPreferences();
        return sharedPreferences.getBoolean(KEY_FIRST_RUN, true);
    }

    public static void setFirstRun(boolean status){
        SharedPreferences.Editor editor = IncourtApplication.getDefaultSharedPreferencesEditor();
        editor.putBoolean(KEY_FIRST_RUN, status);
        editor.commit();
    }

}
