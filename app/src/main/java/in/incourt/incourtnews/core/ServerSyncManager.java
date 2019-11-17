package in.incourt.incourtnews.core;

import android.content.Context;

import in.incourt.incourtnews.core.http.RestAdapter;
import in.incourt.incourtnews.core.models.SyncModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by bhavan on 1/22/17.
 */

public class ServerSyncManager {

    private Thread syncThread;

    private Context context;

    public ServerSyncManager(Context context) {
        this.context = context;
       // syncServer();
    }

    void syncServer() {
        RestAdapter.get().syncManager().enqueue(new Callback<SyncModel>() {
            @Override
            public void onResponse(Call<SyncModel> call, Response<SyncModel> response) {
                syncUpdateService((SyncModel) response.body());
            }

            @Override
            public void onFailure(Call<SyncModel> call, Throwable t) {

            }
        });
    }

    void syncUpdateService(SyncModel syncModel) {
        if (syncModel != null) {
            syncModel.updateRequest();
        }
    }

    public Thread getSyncThread() {
        return syncThread;
    }


}
