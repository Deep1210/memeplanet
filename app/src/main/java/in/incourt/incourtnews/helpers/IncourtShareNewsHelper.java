package in.incourt.incourtnews.helpers;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.view.MenuItem;
import android.view.View;

import com.cocosw.bottomsheet.BottomSheet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import in.incourt.incourtnews.BuildConfig;
import in.incourt.incourtnews.R;
import in.incourt.incourtnews.activities.IncourtActivity;
import in.incourt.incourtnews.core.models.AdvertisementModel;
import in.incourt.incourtnews.core.sql.PostsSql;
import in.incourt.incourtnews.helpers.interfaces.IncourtShareNewsHelperInterface;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by bhavan on 3/23/17.
 */

public class IncourtShareNewsHelper implements IncourtShareNewsHelperInterface {

    ResolveInfo resolveInfo;

    private static final String[] PERMISSIONS_STORAGE = {
            READ_EXTERNAL_STORAGE,
            WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    public static View view;
    PostsSql postsSql;
    AdvertisementModel.Advertisement advertisement;

    boolean INVITE_FRIENDS = false;

    public static IncourtShareNewsHelper getInstance() {
        return new IncourtShareNewsHelper();
    }

    public static void shareNews(View view, PostsSql postsSql) {
        getInstance().setView(view).setPostsSql(postsSql).shareSubOpen();
    }

    public static void inviteFriends(View view) {
        getInstance().setView(view).setINVITE_FRIENDS(true).shareSubOpen();
    }

    public void shareSubOpen() {

        BottomSheet.Builder bottomSheet = new BottomSheet.Builder(getIncourtActivity()).title("Complete Action using...").sheet(in.incourt.incourtnews.R.menu.list);

        final Intent i = getIntent();

        final List<ResolveInfo> activities = getResolveInfos(i);

        if (activities != null && activities.size() > 0) {

            for (int j = 0; j < activities.size(); j++) {
                final int finalJ = j;
                bottomSheet.build().getMenu().add(getAppName(activities.get(j))).setIcon(getAppIcon(activities.get(j).activityInfo.packageName))
                        .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                startWhatsApp(activities.get(finalJ));
                                return false;
                            }
                        });
            }
        }

        bottomSheet.show();
    }


    private void startWhatsApp(ResolveInfo resolveInfo) {
        Intent launch = getActivityIntent(resolveInfo.activityInfo.packageName).setAction(Intent.ACTION_SEND);
        if (isINVITE_FRIENDS()) {
            launch.setType("text/plain");
            launch.putExtra(Intent.EXTRA_TEXT, getShareText());
        } else {
            launch.setType("image/jpeg");
            launch.putExtra(Intent.EXTRA_STREAM, getNewsImage());
            launch.putExtra(Intent.EXTRA_TEXT, getSimpleText());
        }
        startIntent(launch);
    }

    public void startIntent(Intent launch) {
        getIncourtActivity().startActivity(launch);
    }

    private static String getAppName(ResolveInfo resolveInfo) {
        return resolveInfo.loadLabel(getIncourtActivity().getPackageManager()).toString();
    }

    private static Drawable getAppIcon(String packageName) {
        try {
            return getIncourtActivity().getPackageManager().getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static View getView() {
        return view;
    }

    public IncourtShareNewsHelper setView(View view) {
        this.view = view;
        return this;
    }

    public static IncourtActivity getIncourtActivity() {
        return (IncourtActivity) (getView().getContext());
    }

    public PostsSql getPostsSql() {
        return postsSql;
    }

    public IncourtShareNewsHelper setPostsSql(PostsSql postsSql) {
        this.postsSql = postsSql;
        return this;
    }

    public static Intent getIntent() {
        return new Intent(Intent.ACTION_SEND).setType("text/plain");
    }

    public static List<ResolveInfo> getResolveInfos(Intent i) {
        return getIncourtActivity().getPackageManager().queryIntentActivities(i, 0);
    }

    public String getSimpleText() {
        if (getPostsSql() != null) {
            return "Hey! Download InCourt and read all the important legal news in just 60 words. I have shared one with you.\n" + "https://goo.gl/Ktw7Rk";
        } else if (getAdvertisement() != null) {
            return getAdvertisement().getTitle() + " \n " + getAdvertisement().getLink_url();
        } else {
            return "";
        }
    }

    public String getShareText() {
        return "Hey! Download InCourt and read all the important legal news in just 60 words. I have shared one with you.\n from invite friends " + "https://goo.gl/Ktw7Rk";
    }

    public Intent getActivityIntent(String packageName) {
        return new Intent().setPackage(packageName).setAction(Intent.ACTION_SEND);
    }

    public Uri getNewsImage() {

        getView().setDrawingCacheEnabled(true);
        getView().buildDrawingCache(true);

        if (getView().findViewById(in.incourt.incourtnews.R.id.buttonslayer) != null) {
            getView().findViewById(in.incourt.incourtnews.R.id.buttonslayer).setVisibility(View.INVISIBLE);
        }

        if (getView().findViewById(R.id.addview_layer) != null) {
            getView().findViewById(R.id.addview_layer).setVisibility(View.VISIBLE);
        }

        Bitmap bitmap = Bitmap.createScaledBitmap(getView().getDrawingCache(), 720, 1280, false);

        if (getView().findViewById(R.id.addview_layer) != null) {
            getView().findViewById(R.id.addview_layer).setVisibility(View.INVISIBLE);
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "352511083461145.jpg");
        try {
            f.createNewFile();
            new FileOutputStream(f).write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(getIncourtActivity().getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", f);
        } else {
            return Uri.fromFile(f);
        }
    }

    @Override
    public void getPermission() {
        startWhatsApp(getResolveInfo());
    }

    public ResolveInfo getResolveInfo() {
        return resolveInfo;
    }

    public void setResolveInfo(ResolveInfo resolveInfo) {
        this.resolveInfo = resolveInfo;
    }

    public static void shareAdvertisementCard(View itemView, AdvertisementModel.Advertisement advertisementModel) {
        getInstance().setView(itemView).setAdvertisement(advertisementModel).shareSubOpen();
    }

    public AdvertisementModel.Advertisement getAdvertisement() {
        return advertisement;
    }

    public IncourtShareNewsHelper setAdvertisement(AdvertisementModel.Advertisement advertisement) {
        this.advertisement = advertisement;
        return this;
    }

    public boolean isINVITE_FRIENDS() {
        return INVITE_FRIENDS;
    }

    public IncourtShareNewsHelper setINVITE_FRIENDS(boolean INVITE_FRIENDS) {
        this.INVITE_FRIENDS = INVITE_FRIENDS;
        return this;
    }
}
