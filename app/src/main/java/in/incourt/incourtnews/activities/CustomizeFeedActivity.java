package in.incourt.incourtnews.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import in.incourt.incourtnews.R;
import in.incourt.incourtnews.core.sql.CategoriesSQL;
import in.incourt.incourtnews.core.sql.TopicSQL;
import in.incourt.incourtnews.helpers.IncourtToastHelprer;
import in.incourt.incourtnews.helpers.MyInterestHelper;
import in.incourt.incourtnews.helpers.NoNetWorkStateHelper;
import in.incourt.incourtnews.helpers.PostListTypeHelper;
import in.incourt.incourtnews.helpers.interfaces.CategoryButtonHelperInterface;
import in.incourt.incourtnews.helpers.interfaces.NoNetworkHelperInterface;
import in.incourt.incourtnews.others.CategoryButtonHelper;

/**
 * Created by kashif on 2/14/19.
 */

public class CustomizeFeedActivity extends IncourtActivity implements NoNetworkHelperInterface, CategoryButtonHelperInterface {

    int SLIDE_COUNTS = 4;

    int MY_FEED_SELECT_COUNT = 0;

    ArrayList<Integer> my_feed_array = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_feed);
        new CategoryButtonHelper(getWindow().getDecorView().getRootView(), this, false,"feed");
        setupDoneButton(getWindow().getDecorView().getRootView());

        if(MyInterestHelper.getRaw()!=null) {
            try {

                //String value = MyInterestHelper.getRaw();
                JSONArray array = new JSONArray(MyInterestHelper.getRaw());
                for(int i=0;i<array.length();i++){
                    my_feed_array.add(Integer.parseInt(array.get(i).toString()));
                }

                MY_FEED_SELECT_COUNT = array.length();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e("Counter: ","Instart: "+MY_FEED_SELECT_COUNT+"\n"+my_feed_array);

    }


    @Override
    public void onClickCategoryButton(View itemView, CategoriesSQL categoriesSQL) {
        if(itemView.isActivated()){
            unCheckButton(itemView, categoriesSQL);
        }else {
            checkButton(itemView, categoriesSQL);
        }
    }

    @Override
    public void onClickCTopicButton(View itemView, TopicSQL topicSQL) {
        // do nothing
    }

    @Override
    public void setActivatedButtonForFalse(@Nullable View view, boolean flag) {
        // do nothing
    }

    @Override
    public void onClickTryAgain() {

    }

    @Override
    public void onClickSetting() {
        NoNetWorkStateHelper.openSettingActivity();
    }

    void unCheckButton(View itemView, CategoriesSQL categoriesSQL){
        itemView.setActivated(false);
        ((Button) itemView).setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.day_gray_text));
        for(int i = 0; i < my_feed_array.size(); i++){
            if(my_feed_array.get(i) == categoriesSQL.getCategory_id()){
                my_feed_array.remove(i);
            }
        }

        MY_FEED_SELECT_COUNT--;
    }

    void checkButton(View itemView, CategoriesSQL categoriesSQL){
        itemView.setActivated(true);
        ((Button) itemView).setTextColor(Color.WHITE);
        my_feed_array.add(categoriesSQL.getCategory_id());

        MY_FEED_SELECT_COUNT++;
    }

    void setupDoneButton(View itemView){
        if(itemView != null) {
            itemView.findViewById(R.id.my_feed_interest_done_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (MY_FEED_SELECT_COUNT < 3) {
                        IncourtToastHelprer.choseAtLeastThreeIntrest();
                    } else {
                        SLIDE_COUNTS = 5;
                        MyInterestHelper.save(my_feed_array);
                        finish();
                        Intent intent  = new Intent(CustomizeFeedActivity.this,IncourtLauncherActivity.class);
                        intent.putExtra(PostListTypeHelper.FROM_FEED,"feed");
                        startActivity(intent);
                    }
                }
            });
        }
    }

    public void setBackPressButton(View view) {
        onBackPressed();
    }
}
