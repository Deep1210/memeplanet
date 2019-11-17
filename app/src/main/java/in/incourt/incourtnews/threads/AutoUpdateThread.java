package in.incourt.incourtnews.threads;

import java.util.Date;

import in.incourt.incourtnews.core.http.RestAdapter;
import in.incourt.incourtnews.core.models.PostsModel;
import in.incourt.incourtnews.core.request.PostsRequest;
import in.incourt.incourtnews.core.sql.CategoriesSQL;
import in.incourt.incourtnews.core.sql.PostsSql;
import in.incourt.incourtnews.core.sql.TopicSQL;
import in.incourt.incourtnews.helpers.NetworkHelper;
import in.incourt.incourtnews.helpers.PostListTypeHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by bhavan on 5/18/17.
 */

public class AutoUpdateThread{

    Thread auto_main_thread;

    AutoUpdateInterface autoUpdateInterface;

    AutoUpdateNextPage autoUpdateNextPage;

    AutoUpdateSyncRecords autoUpdateSyncRecords;

    long update_next_run, sync_next_run, categories_next_sync, topics_next_sync;

    PostsRequest postsRequest;

    boolean first_run = true;

    public AutoUpdateThread() {

        if(getAuto_main_thread() != null) return;

        auto_main_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(getSleepTime());
                        if(NetworkHelper.state()) executingScript();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        auto_main_thread.setPriority(Thread.MIN_PRIORITY);
        auto_main_thread.start();
    }

    private long getSleepTime() {
        if(first_run){
            first_run = false;
            return 20000;
        }
        return 5000;
    }

    private void executingScript() {
        if(checkForCategoriesSync())
            runCategoriesSync();
        else if(checkForTopicSync())
            runTopicSync();
        else if(checkAutoSyncForRun())
            runAutoSync();
        else if(checkAutoUpdateForRun())
            runAutoUpdate();
    }

    private void runTopicSync() {
        TopicSQL.syncDown();
        topics_next_sync = getNow() + 60000;
    }

    private boolean checkForTopicSync() {
        return (getNow() > topics_next_sync)? true: false;
    }

    private void runCategoriesSync() {
        CategoriesSQL.syncDown();
        categories_next_sync = getNow() + 50000;
    }

    private boolean checkForCategoriesSync() {
        return (getNow() > categories_next_sync)? true: false;
    }

    private boolean checkAutoSyncForRun() {
        return (getNow() > getSync_next_run())? true: false;
    }

    private void runAutoSync() {
        setSync_next_run(getNow() + 40000);
        if(autoUpdateSyncRecords != null) getAutoSyncPostRecords();
    }

    private void getAutoSyncPostRecords() {
        PostsRequest postsRequest = getPostsRequest().clearAllParams().setUpdated_at(PostsSql.getMaxUpdateDate())
        .setId(PostsSql.getMaxPostId(PostListTypeHelper.ALL_NEWS)).setIs_updated(true);

        RestAdapter.get().postAll(postsRequest).enqueue(new Callback<PostsModel>() {
            @Override
            public void onResponse(Call<PostsModel> call, Response<PostsModel> response) {
                if(response != null) getAutoUpdateSyncRecords().onSyncSuccess(response.body());
            }

            @Override
            public void onFailure(Call<PostsModel> call, Throwable t) {

            }
        });
    }

    private long getNow() {
        return new Date().getTime();
    }

    public void runAutoUpdate() {
        setUpdate_next_run(getNow() + 15000);
        if(getAutoUpdateInterface() != null) getAutoUpdatePostRecords();
    }

    private void getAutoUpdatePostRecords() {

        PostsRequest postsRequest = getPostsRequest().clearAllParams().setPubDate(PostsSql.getMaxDateString())
                .setId(PostsSql.getMaxPostId(PostListTypeHelper.ALL_NEWS)).setIs_new(true);

        RestAdapter.get().postAll(postsRequest).enqueue(new Callback<PostsModel>() {
            @Override
            public void onResponse(Call<PostsModel> call, Response<PostsModel> response) {
                if(response != null) {
                    PostsSql.deleteOldRecords(response.body());
                    getAutoUpdateInterface().onUpdateSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<PostsModel> call, Throwable t) {

            }
        });
    }

    private boolean checkAutoUpdateForRun() {
        return (getNow() > getUpdate_next_run())? true: false;
    }

    public void registerAutoUpdate(AutoUpdateInterface autoUpdateInterface){
        this.autoUpdateInterface = autoUpdateInterface;
    }

    public void registerAutoUpdateNextPage(AutoUpdateNextPage autoUpdateNextPage){
        this.autoUpdateNextPage = autoUpdateNextPage;
    }

    public void registerAutoUpdateSyncRecords(AutoUpdateSyncRecords autoUpdateSyncRecords){
        if(this.autoUpdateSyncRecords == null) this.autoUpdateSyncRecords = autoUpdateSyncRecords;
    }

    public void forceExecuteingUpdate(){
        setUpdate_next_run(15000);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runAutoUpdate();
            }
        }).start();

    }

    public void deRegisterUpdate(){
        autoUpdateInterface = null;
    }

    public void deRegisterNextPage(){
        autoUpdateNextPage = null;
    }

    public void deRegisterSyncRecords(){
        autoUpdateSyncRecords = null;
    }

    public Thread getAuto_main_thread() {
        return auto_main_thread;
    }

    public AutoUpdateInterface getAutoUpdateInterface() {
        return autoUpdateInterface;
    }

    public AutoUpdateNextPage getAutoUpdateNextPage() {
        return autoUpdateNextPage;
    }

    public AutoUpdateSyncRecords getAutoUpdateSyncRecords() {
        return autoUpdateSyncRecords;
    }

    public long getUpdate_next_run() {
        return update_next_run;
    }

    public void setUpdate_next_run(long update_next_run) {
        this.update_next_run = update_next_run;
    }

    public long getSync_next_run() {
        return sync_next_run;
    }

    public void setSync_next_run(long sync_next_run) {
        this.sync_next_run = sync_next_run;
    }

    public PostsRequest getPostsRequest() {
        return new PostsRequest();
    }

    public void setPostsRequest(PostsRequest postsRequest) {
        this.postsRequest = postsRequest;
    }

}
