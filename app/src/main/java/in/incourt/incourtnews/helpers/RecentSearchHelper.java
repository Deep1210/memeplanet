package in.incourt.incourtnews.helpers;

import android.content.SharedPreferences;

import in.incourt.incourtnews.IncourtApplication;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by bhavan on 2/21/17.
 */

public class RecentSearchHelper {

    static String RECENT_SEARCH_STRINGS = "RECENT_SEARCH_STRINGS";

    public static void updateString(String recentSearch) {
        SharedPreferences.Editor editor = IncourtApplication.getDefaultSharedPreferencesEditor();

        Set<String> recent =  getRecentSearch();
        if(recent == null) recent = new HashSet<>();

        recent.add(modifyString(recentSearch));

        editor.putStringSet(RECENT_SEARCH_STRINGS, recent);
        editor.commit();
    }

    public static Set<String> getRecentSearch(){
        return IncourtApplication.getDefaultSharedPreferences().getStringSet(RECENT_SEARCH_STRINGS, null);
    }

    public static Iterator getRecentSearchIterator(){
        Set<String> recent = getRecentSearch();
        if(recent != null) return recent.iterator();
        return null;
    }


    public static int getSize(){
        Set<String> recentSearch = getRecentSearch();
        if(recentSearch != null) return recentSearch.size();
        return 0;
    }



    static String modifyString(String string){
        return string.toLowerCase().trim();
    }

    public static void clearRecentSearch(){
        SharedPreferences.Editor editor = IncourtApplication.getDefaultSharedPreferencesEditor();
        editor.remove(RECENT_SEARCH_STRINGS);
        editor.commit();
    }

}
