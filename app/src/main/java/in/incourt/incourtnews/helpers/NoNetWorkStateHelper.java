package in.incourt.incourtnews.helpers;

import android.content.Intent;
import android.view.View;

import in.incourt.incourtnews.IncourtApplication;
import in.incourt.incourtnews.activities.IncourtActivity;
import in.incourt.incourtnews.helpers.interfaces.NoNetworkHelperInterface;

/**
 * Created by bhavan on 3/6/17.
 */


public class NoNetWorkStateHelper implements View.OnClickListener{

    NoNetworkHelperInterface noNetworkHelperInterface;

    public NoNetWorkStateHelper(View itemView, NoNetworkHelperInterface noNetworkHelperInterface) {
        itemView.findViewById(in.incourt.incourtnews.R.id.no_network_try_again).setOnClickListener(this);
        itemView.findViewById(in.incourt.incourtnews.R.id.no_network_setting_button).setOnClickListener(this);
        this.noNetworkHelperInterface = noNetworkHelperInterface;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case in.incourt.incourtnews.R.id.no_network_try_again:
                noNetworkHelperInterface.onClickTryAgain();
                break;

            case in.incourt.incourtnews.R.id.no_network_setting_button:
                noNetworkHelperInterface.onClickSetting();
                break;
        }
    }

    public static void openSettingActivity(){
        IncourtActivity incourtActivity = IncourtApplication.getIncourtLauncherActivity();
        incourtActivity.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
    }

}
