
package in.incourt.incourtnews.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.incourt.incourtnews.IncourtApplication;
import in.incourt.incourtnews.helpers.interfaces.NoNetworkHelperInterface;

/**
 * Created by bhavan on 1/4/17.
 */

public class NetworkHelper {

    public static boolean state() {
        ConnectivityManager cm = (ConnectivityManager) IncourtApplication.getIncourtContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            if (Build.VERSION.SDK_INT < 23) {
                final NetworkInfo ni = cm.getActiveNetworkInfo();

                if (ni != null) {
                    return (ni.isConnected() && (ni.getType() == ConnectivityManager.TYPE_WIFI || ni.getType() == ConnectivityManager.TYPE_MOBILE));
                }
            } else {
                final Network n = cm.getActiveNetwork();

                if (n != null) {
                    final NetworkCapabilities nc = cm.getNetworkCapabilities(n);

                    return (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
                }
            }
        }
        return false;
    }

    public static void renderNoNetwork(Context context, ViewGroup viewGroup, final NoNetworkHelperInterface noNetworkHelperInterface) {
        LayoutInflater no_network = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = no_network.inflate(in.incourt.incourtnews.R.layout.no_network_state, viewGroup, false);
        viewGroup.addView(view);
        new NoNetWorkStateHelper(view, noNetworkHelperInterface);
    }
}
