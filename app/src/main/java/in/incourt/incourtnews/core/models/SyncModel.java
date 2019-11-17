package in.incourt.incourtnews.core.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import in.incourt.incourtnews.core.sql.CategoriesSQL;
import in.incourt.incourtnews.core.sql.TopicSQL;

/**
 * Created by bhavan on 1/22/17.
 */

public class SyncModel extends IncourtModel{

    @SerializedName("category")
    @Expose
    String last_update_category_time;

    @SerializedName("topics")
    @Expose
    String last_update_topic_time;

    public long getLast_update_category_time() {
        return (long) Long.parseLong(last_update_category_time)*1000;
    }

    public void setLast_update_category_time(String last_update_category_time) {
        this.last_update_category_time = last_update_category_time;
    }

    public long getLast_update_topic_time() {
        return (long) Long.parseLong(last_update_topic_time)*1000;
    }

    public void setLast_update_topic_time(String last_update_topic_time) {
        this.last_update_topic_time = last_update_topic_time;
    }

    public boolean updateRequest(){
        if( getLast_update_category_time() > CategoriesSQL.getMaxDate()){
            CategoriesSQL.syncDown();
        }
        if(getLast_update_topic_time() > TopicSQL.getMaxDate()){
            TopicSQL.syncDown();
        }
        return false;
    }

    public static void syncFirstTime(){
        CategoriesSQL.syncDown();
        TopicSQL.syncDown();
    }

    public static boolean getFirstSyncStatus(){
        return (CategoriesSQL.getList().size() > 0 && TopicSQL.getList().size() > 0);
    }

    public static void afterLoginSync() {
        syncLikes();
        syncBookmarks();
        syncWired();
    }

    private static void syncWired() {

    }

    private static void syncBookmarks() {

    }

    private static void syncLikes() {
        
    }

}
