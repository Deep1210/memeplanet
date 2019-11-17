package in.incourt.incourtnews;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.orm.SugarApp;

import java.util.Locale;

import in.incourt.incourtnews.activities.IncourtLauncherActivity;
import in.incourt.incourtnews.core.sql.PostsSql;
import in.incourt.incourtnews.helpers.TypefaceUtil;
import in.incourt.incourtnews.helpers.UserDeviceToken;
import in.incourt.incourtnews.layouts.CategoryPageLayout;
import in.incourt.incourtnews.others.AdBlocker;
import in.incourt.incourtnews.pagers.IncourtActivityViewPager;
import in.incourt.incourtnews.threads.AutoUpdateThread;

import static com.facebook.FacebookSdk.setAdvertiserIDCollectionEnabled;
import static com.facebook.FacebookSdk.setAutoLogAppEventsEnabled;

/**
 * Created by bhavan on 1/24/17.
 */

public class IncourtApplication extends SugarApp {

    public static boolean topicSyncStatus = false;

    public static boolean categorySyncStatus = false;

    public static boolean postSyncStatus = false;

    public static String FACEBOOK_LOGIN_FROM_SETTINGS = "FACEBOOK_LOGIN_FROM_SETTING";

    public static int FACEBOOK_LOGIN_ACITVITY_CODE = 321;
    public static int FACEBOOK_LOGIN_CONFIRM_CODE = 221;

    private static Application incourtApplcation;

    private static boolean CALL_FROM_PUSH_NOTIFICATION = false;

    private static String CALL_FROM_PUSH_NOTIFICATION_STRING;

    public static TextToSpeech textToSpeech;

    static SharedPreferences sharedPreferences;

    static PostsSql postsSql;

    static boolean FIRST_RUN_FETCH_FROM_SERVER = true;

    public static Context getIncourtContext(){
        return incourtApplcation.getApplicationContext();
    }

    static AutoUpdateThread autoUpdateThread;

    @Override
    public void onCreate() {
        super.onCreate();
        incourtApplcation = this;
        AdBlocker.init(this);
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/HelveticaNeue_1.ttf");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getIncourtContext());
        adjustFontScale(getApplicationContext(), getApplicationContext().getResources().getConfiguration());
        CategoryPageLayout.setSeletedTagId("");
        setAutoLogAppEventsEnabled(true);
        setAdvertiserIDCollectionEnabled(true);
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent(AppEventsConstants.EVENT_NAME_COMPLETED_REGISTRATION);
    }

    public static void onPostCreate(){

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(IncourtApplication.incourtApplcation).build();
        ImageLoader.getInstance().init(config);

        textToSpeech = new TextToSpeech(IncourtApplication.getIncourtContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                textToSpeech.setLanguage(Locale.ENGLISH);
            }
        });

        if(!UserDeviceToken.getRegisterState()) new UserDeviceToken();

        setupAutoUpdateThread();

    }


    public static TextToSpeech getTextToSpeech() {
        return textToSpeech;
    }

    public static SharedPreferences getDefaultSharedPreferences(){
        return  sharedPreferences;
    }

    public static SharedPreferences.Editor getDefaultSharedPreferencesEditor(){
        return getDefaultSharedPreferences().edit();
    }

    static IncourtActivityViewPager incourtActivityViewPager;

    public static IncourtActivityViewPager getIncourtActivityViewPager() {
        return incourtActivityViewPager;
    }

    public static void setIncourtActivityViewPager(IncourtActivityViewPager incourtActivityViewPager) {
        IncourtApplication.incourtActivityViewPager = incourtActivityViewPager;
    }

    public static IncourtLauncherActivity incourtLauncherActivity;

    public static IncourtLauncherActivity getIncourtLauncherActivity() {
        return incourtLauncherActivity;
    }

    public static void setIncourtLauncherActivity(IncourtLauncherActivity incourtLauncherActivity) {
        IncourtApplication.incourtLauncherActivity = incourtLauncherActivity;
    }

    public static boolean isCallFromPushNotification() {
        return CALL_FROM_PUSH_NOTIFICATION;
    }

    public static void setCallFromPushNotification(boolean callFromPushNotification) {
        CALL_FROM_PUSH_NOTIFICATION = callFromPushNotification;
    }

    public static String getCallFromPushNotificationString() {
        return CALL_FROM_PUSH_NOTIFICATION_STRING;
    }

    public static void setCallFromPushNotificationString(String callFromPushNotificationString) {
        CALL_FROM_PUSH_NOTIFICATION_STRING = callFromPushNotificationString;
    }

    public static PostsSql getPostsSql() {
        return postsSql;
    }

    public static void setPostsSql(PostsSql postsSql) {
        IncourtApplication.postsSql = postsSql;
    }

    public static boolean isFirstRunFetchFromServer() {
        return FIRST_RUN_FETCH_FROM_SERVER;
    }

    public static void setFirstRunFetchFromServer(boolean firstRunFetchFromServer) {
        FIRST_RUN_FETCH_FROM_SERVER = firstRunFetchFromServer;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // In some cases modifying newConfig leads to unexpected behavior,
        // so it's better to edit new instance.
        Configuration configuration = new Configuration(newConfig);
        adjustFontScale(getApplicationContext(), configuration);
    }

    public static void adjustFontScale(Context context, Configuration configuration) {
        if (configuration.fontScale != 1) {
            configuration.fontScale = 1;
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metrics);
            metrics.scaledDensity = configuration.fontScale * metrics.density;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                context.createConfigurationContext(configuration);
            } else {
                context.getResources().updateConfiguration(configuration, metrics);
            }

        }
    }

    public static void setupAutoUpdateThread() {
        if(autoUpdateThread == null) autoUpdateThread = new AutoUpdateThread();
    }

    public static AutoUpdateThread getAutoUpdateThread() {
        if(autoUpdateThread == null) autoUpdateThread = new AutoUpdateThread();
        return autoUpdateThread;
    }
}
