package in.incourt.incourtnews.activities;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.util.Locale;

import in.incourt.incourtnews.IncourtApplication;
import in.incourt.incourtnews.helpers.ThemeHelper;
import in.incourt.incourtnews.helpers.interfaces.IncourtShareNewsHelperInterface;

import butterknife.ButterKnife;


public class IncourtActivity extends AppCompatActivity{


    IncourtShareNewsHelperInterface incourtShareNewsHelperInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        ButterKnife.bind(this);
        IncourtApplication.onPostCreate();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        final Configuration override = new Configuration();
        override.fontScale = (float) 1.0;
        applyOverrideConfiguration(override);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

    @Override
    protected void onApplyThemeResource(Resources.Theme theme, int resid, boolean first) {
        super.onApplyThemeResource(theme, ThemeHelper.active(getApplicationContext()), first);
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public IncourtShareNewsHelperInterface getIncourtShareNewsHelperInterface() {
        return incourtShareNewsHelperInterface;
    }

    public void setIncourtShareNewsHelperInterface(IncourtShareNewsHelperInterface incourtShareNewsHelperInterface) {
        this.incourtShareNewsHelperInterface = incourtShareNewsHelperInterface;
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){
        switch(permsRequestCode){
            case 1:
                if(incourtShareNewsHelperInterface != null)
                    incourtShareNewsHelperInterface.getPermission();
                break;
        }
    }
}