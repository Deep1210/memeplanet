package in.incourt.incourtnews.helpers;

import android.content.SharedPreferences;

import in.incourt.incourtnews.IncourtApplication;
import in.incourt.incourtnews.core.http.RestAdapter;
import in.incourt.incourtnews.core.models.DeviceIdRegistrationModel;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by bhavan on 12/27/16.
 */

public class UserDeviceToken extends FirebaseInstanceIdService{


    static String APP_USER_DEVICE_TOKEN_STATE_KEY = "APP_USER_DEVICE_TOKEN_STATE_KEY";


    @Override
    public void onTokenRefresh() {
        registerDeviceId();
    }

    public static void registerDeviceId(){
        HashMap<String, String> request = new HashMap<>();
        request.put("device_token", FirebaseInstanceId.getInstance().getToken());
        RestAdapter.get().deviceIdRegistration(request).enqueue(new Callback<DeviceIdRegistrationModel>() {
            @Override
            public void onResponse(Call<DeviceIdRegistrationModel> call, Response<DeviceIdRegistrationModel> response) {
                DeviceIdRegistrationModel deviceIdRegistrationModel = response.body();
                if(deviceIdRegistrationModel != null && deviceIdRegistrationModel.is_success) {
                    setRegisterState();
                }
            }

            @Override
            public void onFailure(Call<DeviceIdRegistrationModel> call, Throwable t) {
                registerDeviceId();
            }
        });
    }

    static void setRegisterState(){
        SharedPreferences.Editor editor = IncourtApplication.getDefaultSharedPreferencesEditor();
        editor.putBoolean(APP_USER_DEVICE_TOKEN_STATE_KEY, true);
        editor.commit();
    }

    public static boolean getRegisterState(){
            return IncourtApplication.getDefaultSharedPreferences().getBoolean(APP_USER_DEVICE_TOKEN_STATE_KEY, false);
    }


}
