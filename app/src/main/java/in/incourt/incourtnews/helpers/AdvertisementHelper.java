package in.incourt.incourtnews.helpers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import in.incourt.incourtnews.IncourtApplication;

import in.incourt.incourtnews.R;
import in.incourt.incourtnews.activities.IncourtActivity;
import in.incourt.incourtnews.core.models.AdvertisementModel;
import in.incourt.incourtnews.core.models.PostsModel;

/**
 * Created by bhavan on 3/22/17.
 */

public class AdvertisementHelper {

    ImageView imageView;
    AdvertisementModel.Advertisement advertisementModel;
    View itemView;
    TextView advertisement_button;
    TextView share_layer;
    static String ADVERTISEMENT_KEY = "ADVERTISEMENT_KEY";

    static int ADVERTISEMENT_TYPE_FULL_IMAGE = 1;

    static int ADVERTISEMENT_TYPE_IMAGE_BUTTON = 2;

    static int ADVERTISEMENT_TYPE_SHARE_CARD = 3;

    public static AdvertisementHelper init(View itemView, AdvertisementModel.Advertisement advertisementModel){

        final AdvertisementHelper advertisementHelper = getInstance();

        advertisementHelper.setAdvertisementModel(advertisementModel).setImageView((ImageView) itemView.findViewById(R.id.advertisement_image));

        advertisementHelper.setItemView(itemView);

        advertisementHelper.setAdvertisement_button((TextView) advertisementHelper.getItemView().findViewById(R.id.advertisement_button));

        if(advertisementHelper.getAdvertisementModel().getTitle() != null && advertisementModel.getType() == ADVERTISEMENT_TYPE_IMAGE_BUTTON){
            advertisementHelper.getAdvertisement_button().setVisibility(View.VISIBLE);
            advertisementHelper.getItemView().findViewById(R.id.share_layer).setVisibility(View.VISIBLE);
            advertisementHelper.getAdvertisement_button().setText(advertisementHelper.getAdvertisementModel().getTitle());
            AdvertisementModel.Advertisement.AdvertisementButtonModel advertisementButtonModel = advertisementHelper.getAdvertisementModel().getButtonSetting();
            if(advertisementButtonModel != null) {
                advertisementHelper.getAdvertisement_button().setBackgroundColor(Color.parseColor(advertisementButtonModel.getBackground_color()));
                advertisementHelper.getAdvertisement_button().setTextColor(Color.parseColor(advertisementButtonModel.getForeground_color()));
            }
        }else if(advertisementModel.getType() == ADVERTISEMENT_TYPE_SHARE_CARD){
            advertisementHelper.getAdvertisement_button().setVisibility(View.VISIBLE);
            advertisementHelper.getItemView().findViewById(R.id.share_layer).setVisibility(View.VISIBLE);
            advertisementHelper.getAdvertisement_button().setText("Share this Card");
            advertisementHelper.getAdvertisement_button().setBackgroundColor(Color.TRANSPARENT);
        }

        return advertisementHelper;
    }

    public static AdvertisementHelper getInstance(){
        return new AdvertisementHelper();
    }

    public RelativeLayout getMainContainer(){
        return (RelativeLayout) getItemView().findViewById(R.id.advertisement_layout_container);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public AdvertisementHelper setImageView(ImageView imageView) {
        this.imageView = imageView;
        return this;
    }

    public AdvertisementModel.Advertisement getAdvertisementModel() {
        return advertisementModel;
    }

    public AdvertisementHelper setAdvertisementModel(AdvertisementModel.Advertisement advertisementModel) {
        this.advertisementModel = advertisementModel;
        return this;
    }

    public View getItemView() {
        return itemView;
    }

    public AdvertisementHelper setItemView(View itemView) {
        this.itemView = itemView;
        return this;
    }

    public IncourtActivity getIncourtActivity(){
        if(getItemView().getContext() != null){
            return (IncourtActivity) getItemView().getContext();
        }
        return null;
    }

    public TextView getAdvertisement_button() {
        return advertisement_button;
    }

    public AdvertisementHelper setAdvertisement_button(TextView advertisement_button) {
        this.advertisement_button = advertisement_button;
        return this;
    }

    public void onClickAdvertisement(AdvertisementModel.Advertisement advertisementModel){
        if(advertisementModel.getType() == ADVERTISEMENT_TYPE_SHARE_CARD){
            IncourtShareNewsHelper.shareAdvertisementCard(getItemView(), advertisementModel);
        }else {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(advertisementModel.getLink_url()));
            getIncourtActivity().startActivity(browserIntent);
        }
    }

    public static boolean saveAdvertisement(String advertisement){
        if(advertisement != null && !advertisement.equals("null") && advertisement.length() > 0) {
            SharedPreferences.Editor editor = IncourtApplication.getDefaultSharedPreferencesEditor();
            editor.putString(AdvertisementHelper.ADVERTISEMENT_KEY, "{data:" + advertisement + "}");
            return editor.commit();
        }
        return false;
    }

    public static List<AdvertisementModel.Advertisement> getAdvertisementList(){
        String advertisementString = getAdvertisementString();
        if(advertisementString != "") {
            AdvertisementModel advertisementModel = new Gson().fromJson(advertisementString, AdvertisementModel.class);
            if(advertisementModel != null){
                return advertisementModel.getAdvertisements();
            }else return null;
        }
        return null;
    }

    public static String getAdvertisementString(){
        return IncourtApplication.getDefaultSharedPreferences().getString(ADVERTISEMENT_KEY, "");
    }

    public static void update(PostsModel postsModel) {
        List<AdvertisementModel.Advertisement> advertisements =  postsModel.getAdvertisements();
        if(advertisements != null && advertisements.size() > 0){
            AdvertisementHelper.saveAdvertisement(new Gson().toJson(advertisements));
        }
    }
}
