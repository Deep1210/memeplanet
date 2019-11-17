package in.incourt.incourtnews.helpers;

import android.os.Bundle;
import android.util.Log;
import in.incourt.incourtnews.activities.FacebookLoginActivity;
import in.incourt.incourtnews.core.http.RestAdapter;
import in.incourt.incourtnews.core.models.UserLoginModel;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by bhavan on 12/28/16.
 */

        public class FacebookHelper {

            private static final String facebook_graph_url = "http://graph.facebook.com/v2.8/";

            public static void setupFaceBookInit(final FacebookLoginActivity activity, CallbackManager callbackManager){


                FacebookSdk.sdkInitialize(activity.getApplicationContext());


                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                FacebookHelper.signUp(activity, loginResult);
                            }

                            @Override
                            public void onCancel() {
                                Log.d("login_token", "CANCEL");
                            }

                            @Override
                            public void onError(FacebookException error) {
                                Log.d("login_token", error.getMessage());
                            }
                        }
                );
            }

            private static boolean facebookLogin(LoginResult loginResult) {
                String facebook_user_id = loginResult.getAccessToken().getUserId();
                String facebook_access_token = loginResult.getAccessToken().getToken();
                String facebook_profile_pic = profilePictureSrc(facebook_user_id);
                return true;
            }

            public static String profilePictureSrc(String facebook_user_id) {
                return facebook_graph_url + facebook_user_id + "/" + "picture?type=normal";
            }

            public static void signUp(final FacebookLoginActivity activity, final LoginResult loginResult){
                final IncourtLoader incourtLoader = IncourtLoader.init(activity).start();
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback(){
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                RestAdapter.get().facebookSignup(mergeRequet(loginResult, response)).enqueue(new Callback<UserLoginModel>() {
                                    @Override
                                    public void onResponse(Call<UserLoginModel> call, Response<UserLoginModel> response) {
                                        UserHelper.login(response.body());
                                        incourtLoader.stop();
                                        activity.finish();
                                    }
                                    @Override
                                    public void onFailure(Call<UserLoginModel> call, Throwable t) {
                                        Log.d("AD", "qwe");
                                    }
                                });
                            }
                        }
                    );
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,email,first_name,last_name,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            static HashMap<String, String> mergeRequet(LoginResult loginResult, GraphResponse response){
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("registerid", loginResult.getAccessToken().getUserId());
                hashMap.put("access_token", loginResult.getAccessToken().getToken());
                try {
                    hashMap.put("name", response.getJSONObject().getString("first_name") + " " + response.getJSONObject().getString("last_name"));
                    hashMap.put("email", response.getJSONObject().getString("email"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return  hashMap;
            }

        }
