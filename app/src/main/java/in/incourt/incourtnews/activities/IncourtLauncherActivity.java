package in.incourt.incourtnews.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import in.incourt.incourtnews.R;

import com.crashlytics.android.Crashlytics;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.iid.FirebaseInstanceId;

import in.incourt.incourtnews.IncourtApplication;
import in.incourt.incourtnews.core.models.SyncModel;
import in.incourt.incourtnews.core.sql.PostsSql;
import in.incourt.incourtnews.helpers.FirebaseMessagingServiceHelper;
import in.incourt.incourtnews.helpers.PostListTypeHelper;
import in.incourt.incourtnews.helpers.UserHelper;
import in.incourt.incourtnews.helpers.ViewHelpers;
import in.incourt.incourtnews.layouts.PostLayoutPage;
import in.incourt.incourtnews.pagers.IncourtActivityViewPager;
import io.fabric.sdk.android.Fabric;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by bhavan on 2/24/17.
 */

  public class IncourtLauncherActivity extends IncourtActivity  {

    PostLayoutPage postLayoutPage;
    PostsSql postsSql;
    IncourtActivityViewPager incourtActivityViewPager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Only run first time app launch
        */
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent(AppEventsConstants.EVENT_NAME_VIEWED_CONTENT);
        isStoragePermissionGranted();

        if(ViewHelpers.getFirstRun()) SyncModel.syncFirstTime();
        if(IncourtApplication.getIncourtLauncherActivity() == null) IncourtApplication.setIncourtLauncherActivity(this);
        setContentView(R.layout.incourt_launcher_activity);
        incourtActivityViewPager = (IncourtActivityViewPager) findViewById(R.id.incourt_activity_view_pager);

        if(getIntent().hasExtra("posts")){
            setupFormPushNotification(FirebaseMessagingServiceHelper.modifyData(String.valueOf(getIntent().getExtras().get("posts"))));
        }

        if(getIntent().hasExtra(FirebaseMessagingServiceHelper.PUSH_NOTIFICATION_DATA)){
            setupFormPushNotification((String) getIntent().getSerializableExtra(FirebaseMessagingServiceHelper.PUSH_NOTIFICATION_DATA));
        }

        String token  = FirebaseInstanceId.getInstance().getToken();
        if(token!=null){
            Log.e("Token : ",token);
        }

        if(getIntent().getStringExtra(PostListTypeHelper.FROM_FEED)!=null) {

            PostListTypeHelper.setFilterType(PostListTypeHelper.MY_FEED);

        }else{

            PostListTypeHelper.setFilterType(PostListTypeHelper.ALL_NEWS);
        }

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Fabric.with(this, new Crashlytics());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(UserHelper.getState() && postLayoutPage != null){
            if(requestCode == PostListTypeHelper.FACEBOOK_LOGIN_FOR_POST_WIRED) postLayoutPage.postWiredStart(postsSql);
            if(requestCode == PostListTypeHelper.FACEBOOK_LOGIN_FOR_POST_LIKE) postLayoutPage.postLike(postsSql);
        }
    }

    public void setPostLayoutPage(PostLayoutPage postLayoutPage) {
        this.postLayoutPage = postLayoutPage;
    }

    public void setPostsSql(PostsSql postsSql) {
        this.postsSql = postsSql;
    }

    @Override
    public void onBackPressed() {
        if(incourtActivityViewPager.getCurrentItem() == 0){
            incourtActivityViewPager.setCurrentItem(1, true);
        }
        else if(incourtActivityViewPager.getNewsListHorizontalViewPager() != null && incourtActivityViewPager.getNewsListHorizontalViewPager().getCurrentItem() == 1 && !ViewHelpers.getFirstRun()){
            incourtActivityViewPager.getNewsListHorizontalViewPager().setCurrentItem(0, true);
        }
        else{
            super.onBackPressed();
        }
    }

    public IncourtActivityViewPager getIncourtActivityViewPager() {
        return incourtActivityViewPager;
    }

    public void setupFormPushNotification(String serialize){
        PostListTypeHelper.setFilterType(PostListTypeHelper.ALL_NEWS);
        IncourtApplication.setCallFromPushNotification(true);
        IncourtApplication.setCallFromPushNotificationString(serialize);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getIncourtActivityViewPager() != null) getIncourtActivityViewPager().onResume();
    }
}
