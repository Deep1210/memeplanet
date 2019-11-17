package in.incourt.incourtnews.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.incourt.incourtnews.R;
import in.incourt.incourtnews.helpers.IncourtStringHelper;
import in.incourt.incourtnews.helpers.IncourtToastHelprer;

public class RateIncourt extends IncourtActivity {

    @BindView(R.id.emoji1)
    ImageView emoji1;

    @BindView(R.id.emoji2)
    ImageView emoji2;

    @BindView(R.id.emoji3)
    ImageView emoji3;

    @BindView(R.id.emoji4)
    ImageView emoji4;

    @BindView(R.id.emoji5)
    ImageView emoji5;

    Context context = RateIncourt.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_incourt);
        ButterKnife.bind(this);
        rate();
    }

    void rate() {
        emoji1 = (ImageView) findViewById(R.id.emoji1);
        emoji2 = (ImageView) findViewById(R.id.emoji2);
        emoji3 = (ImageView) findViewById(R.id.emoji3);
        emoji4 = (ImageView) findViewById(R.id.emoji4);
        emoji5 = (ImageView) findViewById(R.id.emoji5);

        emoji1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                IncourtToastHelprer.showToast("Horrible! Please send us your Feedback");
                feedback();
            }
        });

        emoji2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                IncourtToastHelprer.showToast("Bad! Please send us your Feedback");
                feedback();
            }
        });

        emoji3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                IncourtToastHelprer.showToast("Okay!");
                //rateUs();
            }
        });

        emoji4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                IncourtToastHelprer.showToast("Good!");
                rateUs();
            }
        });

        emoji5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                IncourtToastHelprer.showToast("Great!");
                rateUs();
            }
        });
    }

    void rateUs(){
        Uri marketUri = Uri.parse("market://details?id=" + "in.incourt.incourtnews");
        Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
        startActivity(marketIntent);
    }

    void feedback() {
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
        return fields[Build.VERSION.SDK_INT].getName();
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

    public String getScreenHeightInInches() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        double density = dm.density * 160;
        double x = Math.pow(dm.widthPixels / density, 2);
        double y = Math.pow(dm.heightPixels / density, 2);
        return String.valueOf(Math.sqrt(x + y));

    }
}
