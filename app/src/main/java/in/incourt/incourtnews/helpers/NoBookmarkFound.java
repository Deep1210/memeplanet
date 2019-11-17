package in.incourt.incourtnews.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by admin on 1/31/2017.
 */

public class NoBookmarkFound {

    public static void renderNoBookmark(Context context, ViewGroup viewGroup) {
        LayoutInflater no_network = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewGroup.addView(no_network.inflate(in.incourt.incourtnews.R.layout.no_bookmark, viewGroup, false));
    }


}
