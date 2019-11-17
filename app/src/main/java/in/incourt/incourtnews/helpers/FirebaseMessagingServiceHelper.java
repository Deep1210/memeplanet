package in.incourt.incourtnews.helpers;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import in.incourt.incourtnews.R;
import in.incourt.incourtnews.activities.IncourtLauncherActivity;
import in.incourt.incourtnews.core.models.PostCategoryModel;
import in.incourt.incourtnews.core.models.PostsModel;
import in.incourt.incourtnews.core.sql.PostImageSql;
import in.incourt.incourtnews.core.sql.PostsSql;

/**
 * Created by bhavan on 2/20/17.
 */

public class FirebaseMessagingServiceHelper extends FirebaseMessagingService {


    public static String PUSH_NOTIFICATION_DATA = "PUSH_NOTIFICATION_DATA";

    public static String PUSH_NOTIFICATION_STRING_DATA = "";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(IncourtNotificationHelper.getStatus() /*&& checkMyFeedStatus(remoteMessage)*/) {
            setPushNotificationStringData(remoteMessage.getData().get("posts"));
            show(remoteMessage);
        }
    }

    private boolean checkMyFeedStatus(RemoteMessage remoteMessage) {
        boolean status = false;
        try {
            List<PostCategoryModel> postCategoryModels = getPostsModel(getPushNotificationStringData()).getPostModelList().get(0).getPostCategoryModels();
            if(postCategoryModels != null && postCategoryModels.size() > 0){
                for (int i = 0; i < postCategoryModels.size(); i++){
                    if(myFeedExisting(postCategoryModels.get(i).getCategory_id())){
                        status = true;
                        break;
                    }
                }
            }
            return status;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;



    }

    private boolean myFeedExisting(long category_id) {
        return MyInterestHelper.get().contains(String.valueOf(category_id));
    }

    void show(RemoteMessage remoteMessage){
        // Create Notification

        String CHANNEL_ID = "my_channel_01";
        CharSequence name = getString(R.string.channel_name);// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        }
        Notification notification;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder noti = new Notification.Builder(getApplicationContext());
            noti.setSmallIcon(in.incourt.incourtnews.R.drawable.incourt_logo)
                    .setAutoCancel(true)
                    .setChannelId(CHANNEL_ID)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setCustomBigContentView(setCustomViewNotification())
                    .setContentIntent(getTaskStackBuilder().getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT));
            notification = noti.build();
        }else{
            notification =  setCustomViewNotificationOld();
        }
        if(notification == null) return;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.createNotificationChannel(mChannel);
        }
        mNotificationManager.notify(0, notification);

    }

    private Notification setCustomViewNotificationOld(){

        PostsSql postsSql = PostsSql.getPostsSqlFormSerialize(getPushNotificationStringData());
        if(postsSql == null) return null;
        PendingIntent resultPendingIntent = getTaskStackBuilder().getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(in.incourt.incourtnews.R.drawable.incourt_logo)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(postsSql.getTitle()).build();

        notification.bigContentView = setCustomViewNotification();

        notification.bigContentView.setOnClickPendingIntent(in.incourt.incourtnews.R.id.sharelayer, getPendingIntentForShare());


        return notification;

    }

    private PendingIntent getPendingIntentForShare() {
        return PendingIntent.getActivity(this, 0, getIncourtActivityIntent(true), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private TaskStackBuilder getTaskStackBuilder(){
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(IncourtLauncherActivity.class);
        stackBuilder.addNextIntent(getIncourtActivityIntent(false));
        return stackBuilder;
    }

    private Intent getIncourtActivityIntent(boolean flag){
        Intent resultIntent = new Intent(this, IncourtLauncherActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PUSH_NOTIFICATION_DATA, getPushNotificationStringData());
        if(flag){
            bundle.putString("from_share123", "ASD");
        }
        resultIntent.putExtras(bundle);
        return resultIntent;
    }

    private RemoteViews setCustomViewNotification() {
        PostsSql postsSql = PostsSql.getPostsSqlFormSerialize(getPushNotificationStringData());
        RemoteViews expandedView = new RemoteViews(this.getPackageName(), R.layout.notification_layout);
        if(postsSql == null) return expandedView;

        expandedView.setTextViewText(R.id.post_news_title, postsSql.getTitle());
        expandedView.setTextViewText(R.id.timetext, new SimpleDateFormat("hh:mm a").format(new Date().getTime()));

        if(postsSql.getPostImages(postsSql.getPost_id()) != null && postsSql.getPostImages(postsSql.getPost_id()).size() > 0) {
            PostImageSql postImageSql = postsSql.getPostImages(postsSql.getPost_id()).get(0);
            Uri imageUrl = null;

            if (postImageSql.getType() == ImageHelper.IMAGE_TYPE_VIDEO) {
                imageUrl = Uri.parse("https://img.youtube.com/vi/" + postImageSql.getPostImage() + "/0.jpg");
            } else {
                imageUrl = Uri.parse(postImageSql.getPostImage());
            }

            try {
                URL url = new URL(imageUrl.toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap notifimgbtmp = BitmapFactory.decodeStream(input);
                expandedView.setImageViewBitmap(R.id.largeimg, notifimgbtmp);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return expandedView;

    }

    public PostsModel getPostsModel(String remoteMessage){
        return new Gson().fromJson(remoteMessage, PostsModel.class);
    }

    public static String getPushNotificationStringData() {
        return PUSH_NOTIFICATION_STRING_DATA;
    }

    public static void setPushNotificationStringData(String pushNotificationStringData) {
        PUSH_NOTIFICATION_STRING_DATA = modifyData(pushNotificationStringData);
    }

    public static String modifyData(String pushNotificationStringData){
        String out = null;
        try {
            out = IncourtCompressHelper.decompress(IncourtCompressHelper.decode64(pushNotificationStringData));
            out = "{\"posts\":["+ out +"]}";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }
}
