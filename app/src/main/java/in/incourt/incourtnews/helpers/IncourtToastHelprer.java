package in.incourt.incourtnews.helpers;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import in.incourt.incourtnews.IncourtApplication;
import in.incourt.incourtnews.R;
import in.incourt.incourtnews.core.sql.PostsSql;

/**
 * Created by bhavan on 3/10/17.
 */

public class IncourtToastHelprer {
    public static final int LENGTH_SHORTe = 0;
    public static void showRefreshComplete() {
        showToast("Refreshed Successfully");
    }

    public static void showAlreadyUptodate() {
        showToast("Already Updated");
    }

    public static void showNoNetwork() {
        showToast("No Network please try again!");
    }

    public static void showTakingBitLong() {
        showToast("Taking a bit long!");
    }

    public static void showToast(String message) {
        LayoutInflater inflater = IncourtApplication.getIncourtLauncherActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.mytoast, (ViewGroup) IncourtApplication.getIncourtLauncherActivity().findViewById(R.id.toast_layout_root));
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(message);
        final Toast toast = new Toast(IncourtApplication.getIncourtContext());
//        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 900);

        toast.show();
    }

    public static void showUnreadToast(int count){
        showToast(((count > 99)? "99+": String.valueOf(count)) + " Unread Stories");
    }

    public static void showUnableToRefresh() {
        showToast("Unable to refresh!");
    }

    public static void choseAtLeastThreeIntrest() { showToast("Please Select at least 3 interest..."); }

    public static void showBookmarksToast(int is_bookmarks) {
        if(is_bookmarks == PostsSql.POST_BOOKMARKS) showToast("News Bookmarked");
        else if(is_bookmarks == PostsSql.POST_BOOKMARKS_REMOVE) showToast("Bookmark Removed");
    }



}
