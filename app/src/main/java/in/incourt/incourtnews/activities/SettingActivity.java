package in.incourt.incourtnews.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.SwitchCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import in.incourt.incourtnews.FabricKpi.IncourtFabricInviteFreinds;
import in.incourt.incourtnews.IncourtApplication;
import in.incourt.incourtnews.R;
import in.incourt.incourtnews.helpers.FacebookHelper;
import in.incourt.incourtnews.helpers.ImageHelper;
import in.incourt.incourtnews.helpers.IncourtNotificationHelper;
import in.incourt.incourtnews.helpers.IncourtShareNewsHelper;
import in.incourt.incourtnews.helpers.IncourtStringHelper;
import in.incourt.incourtnews.helpers.ThemeHelper;
import in.incourt.incourtnews.helpers.UserHelper;


public class SettingActivity extends IncourtActivity {

    @BindView(R.id.user_profile_image)
    CircleImageView user_profile_image_view;

    @BindView(R.id.setting_facebook_connect)
    LinearLayout setting_facebook_connect;

    @BindView(R.id.synccontact)
    LinearLayout synccontact;

    @BindView(R.id.invitefriends)
    LinearLayout invitefriends;

    @BindView(R.id.feedback)
    LinearLayout feedback;

    @BindView(R.id.termsconditions)
    LinearLayout termsconditions;

    @BindView(R.id.privacypolicy)
    LinearLayout privacypolicy;

    @BindView(R.id.backarrow)
    ImageView backpressbtn;

    @BindView(R.id.toggle_notif)
    SwitchCompat toggle_notif;

    @BindView(R.id.theme_change_toggle_button)
    SwitchCompat toggleButton;

    @BindView(R.id.media_bloger)
    LinearLayout media_bloger;

    @BindView(R.id.toggle_hd_image)
    SwitchCompat toggle_hd_image;

    LinearLayout contact_sync_row;

    Context context = SettingActivity.this;
    private static NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_setting);
        setting_facebook_connect = (LinearLayout) findViewById(R.id.setting_facebook_connect);
        contact_sync_row = (LinearLayout) findViewById(R.id.contact_sync_row);

        toggleButton = (SwitchCompat) findViewById(R.id.theme_change_toggle_button);
        toggleButton.setChecked(ThemeHelper.toggleStatus(this));

        toggle_hd_image = (SwitchCompat) findViewById(R.id.toggle_hd_image);
        toggle_hd_image.setChecked(ImageHelper.getHdImageStatus());
        toggle_notif = (SwitchCompat) findViewById(R.id.toggle_notif);
        toggle_notif.setChecked(IncourtNotificationHelper.getStatus());
        userProfileSetUp();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }



    void userProfileSetUp() {
        if (UserHelper.getFacebookState()) {
            setting_facebook_connect.setVisibility(View.GONE);
            user_profile_image_view = (CircleImageView) findViewById(R.id.user_profile_image);
            ImageHelper.loadImage(FacebookHelper.profilePictureSrc(UserHelper.getAppUserRegisterId()), user_profile_image_view);
        }
    }


    @OnClick(R.id.user_profile_image)
    void userbtnCLick() {
        Intent intent = new Intent(getApplicationContext(), UserAccountActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.backarrow)
    void setBackpressbtn() {
        onBackPressed();
    }

    @OnClick(R.id.setting_facebook_connect)
    void facebookConnect() {
        Intent intent = new Intent(getApplicationContext(), FacebookLoginActivity.class);
        intent.putExtra(IncourtApplication.FACEBOOK_LOGIN_FROM_SETTINGS, true);
        startActivityForResult(intent, IncourtApplication.FACEBOOK_LOGIN_ACITVITY_CODE);
    }

    @OnCheckedChanged(R.id.toggle_hd_image)
    void hd_image_change() {

        if (toggle_hd_image.isChecked()) {
            ImageHelper.hdImageOn();
        } else {
            ImageHelper.hdImageOff();
        }
    }

    @OnClick(R.id.synccontact)
    void synccontactClick() {
        Intent intent = new Intent(getApplicationContext(), ConfirmDetailActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.invitefriends)
    void invitefriendsClick() {
        IncourtFabricInviteFreinds.logInviteFriends();
        IncourtShareNewsHelper.inviteFriends(findViewById(R.id.activity_setting));
    }

    @OnClick(R.id.rateincourt)
    void rateincourtClick() {
        Intent intent1 = new Intent(getApplicationContext(), RateIncourt.class);
        startActivity(intent1);
        //rateUs();
    }

    @OnClick(R.id.feedback)
    void feedbackClick() {
        feedback();
    }

    @OnClick(R.id.termsconditions)
    void termsconditionsClick() {
        BitmapFactory.decodeResource(context.getResources(), R.drawable.girlimage);
    }

    @OnClick(R.id.privacypolicy)
    void privacypolicyClick() {
        Toast.makeText(getApplicationContext(), "Privacy policy", Toast.LENGTH_SHORT).show();
    }

    @OnCheckedChanged(R.id.toggle_notif)
    void setToggle_notif() {
        if (toggle_notif.isChecked()) {
            IncourtNotificationHelper.enable();
        } else {
            IncourtNotificationHelper.disable();
        }
    }

    @OnClick(R.id.media_bloger)
    void media_blogerClick() {
        Intent intent = new Intent(getApplicationContext(), Media_BloggersActivity.class);
        startActivity(intent);
    }

    @OnCheckedChanged(R.id.theme_change_toggle_button)
    void themeChange() {
        boolean theme_id = false;
        if (toggleButton.isChecked()) {
            theme_id = true;
        }
        ThemeHelper.switchTheme(getApplicationContext(), this, (theme_id) ? R.style.NightTheme : R.style.DayTheme);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IncourtApplication.FACEBOOK_LOGIN_ACITVITY_CODE) {
            recreate();
        }
    }

    /*void rateUs() {
        Uri marketUri = Uri.parse("market://details?id=" + "in.incourt.incourtnews");
        Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
        startActivity(marketIntent);
    }*/

    @Override
    protected void onResume() {
        userProfileSetUp();
        super.onResume();
    }


    private void feedback() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String[] emails = {"feedback@incourt.in"};
        intent.putExtra(Intent.EXTRA_EMAIL, emails);
        Uri data = Uri.parse("mailto:?subject=" + "&body=" + getFeedBackBody());
        intent.setData(data);
        startActivity(intent);
    }

    public String getFeedBackBody(){
        //TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        return "DeviceId: " + Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID) + "\n" +
                "Device Name: " + IncourtStringHelper.capitalize(Build.MANUFACTURER + " " + android.os.Build.MODEL) + "\n" +
                "Android Version Name: " + getOsName() + "\n" +
                "App Version :" + getAppVersion() + "\n" +
                "App version Code: " + getAppversionCode() + "\n" +
                "Device Width (in DP): " + getDeviceWidthInDP() + "\n" +
                "Device Height (in DP): " + getDeviceHeightInDP() + "\n" +
                "Device Height (in Inches) : " + getScreenHeightInInches() + "\n" +
                "-- Please dont't edit anything above this, to help us serve you better";
    }

    public String getOsName(){
        Field[] fields = Build.VERSION_CODES.class.getFields();
        return fields[Build.VERSION.SDK_INT ].getName();

    }

    public String getAppVersion(){
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getAppversionCode(){
        try {
            return String.valueOf(getPackageManager().getPackageInfo(getPackageName(), 0).versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getDeviceWidthInDP(){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        //float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        return String.valueOf(displayMetrics.widthPixels / displayMetrics.density);
    }


    public String getDeviceHeightInDP(){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return String.valueOf(displayMetrics.heightPixels / displayMetrics.density);
    }

    public String getScreenHeightInInches(){
        DisplayMetrics dm = getResources().getDisplayMetrics();
        double density = dm.density * 160;
        double x = Math.pow(dm.widthPixels / density, 2);
        double y = Math.pow(dm.heightPixels / density, 2);
        return String.valueOf(Math.sqrt(x + y));
    }

    private void inviteFriend() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:?subject=" + getString(R.string.feedback_content_title) + "&body=" + getString(R.string.feedback_content));
        intent.setData(data);
    }


}

