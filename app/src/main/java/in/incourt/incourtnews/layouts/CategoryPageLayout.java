package in.incourt.incourtnews.layouts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import in.incourt.incourtnews.IncourtApplication;
import in.incourt.incourtnews.R;
import in.incourt.incourtnews.activities.CustomizeFeedActivity;
import in.incourt.incourtnews.activities.IncourtLauncherActivity;
import in.incourt.incourtnews.activities.SearchActivity;
import in.incourt.incourtnews.activities.SettingActivity;
import in.incourt.incourtnews.core.sql.CategoriesSQL;
import in.incourt.incourtnews.core.sql.TopicSQL;
import in.incourt.incourtnews.helpers.IncourtColorHelper;
import in.incourt.incourtnews.helpers.PostListTypeHelper;
import in.incourt.incourtnews.helpers.interfaces.CategoryButtonHelperInterface;
import in.incourt.incourtnews.newslist.adapters.NewsListHorizontalViewPagerAdapter;
import in.incourt.incourtnews.others.CategoryButtonHelper;
import in.incourt.incourtnews.others.TopicButtonHelper;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by bhavan on 2/24/17.
 */

public class CategoryPageLayout implements View.OnClickListener, CategoryButtonHelperInterface {

    IncourtLauncherActivity incourtLauncherActivity;

    TopicSQL topicSQL;
    CategoriesSQL categoriesSQL;

    CategoryButtonHelper categoryButtonHelper;
    TopicButtonHelper topicButtonHelper;
    View itemView;

    static String SELECTED_BUTTON_STRING_KEY = "SELECTED_BUTTON_STRING_KEY";

    public CategoryPageLayout(IncourtLauncherActivity incourtLauncherActivity, View itemView) {

        this.incourtLauncherActivity = incourtLauncherActivity;
        this.itemView = itemView;


        /**
         * Category AND Topic Button SetUp
         */

        categoryButtonHelper = new CategoryButtonHelper(itemView, this,"category");
       // topicButtonHelper = new TopicButtonHelper(itemView, this);

//        itemView.findViewById(R.id.img_setting).setOnClickListener(this);

//        itemView.findViewById(R.id.searchtext1).setOnClickListener(this);
//
//        itemView.findViewById(R.id.bookmarkbtn).setOnClickListener(this);
//        itemView.findViewById(R.id.bookmarkbtn).setActivated(PostListTypeHelper.getBookmarkStatus());
//
//        itemView.findViewById(R.id.mylikesbtn).setOnClickListener(this);
//        itemView.findViewById(R.id.mylikesbtn).setActivated(PostListTypeHelper.getLikeStatus());
//
//        itemView.findViewById(R.id.mywiresbtn).setOnClickListener(this);
//        itemView.findViewById(R.id.mywiresbtn).setActivated(PostListTypeHelper.getWiredStatus());
//
//
//        itemView.findViewById(R.id.customizefeedbtn).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            /**
             * Setting Image Click
             */

//            case in.incourt.incourtnews.R.id.img_setting:
//                Intent intent = new Intent(incourtLauncherActivity, SettingActivity.class);
//                incourtLauncherActivity.startActivity(intent);
//                break;

            /**
             * Search Bar Click
             */

//            case in.incourt.incourtnews.R.id.searchtext1:
//                openSearchActivity();
//                break;
//
//            case R.id.bookmarkbtn:
//                changeListType(PostListTypeHelper.MY_BOOKMARKS, view);
//                break;
//
//            case R.id.mylikesbtn:
//                changeListType(PostListTypeHelper.MY_LIKES, view);
//                break;
//
//            case R.id.mywiresbtn:
//                changeListType(PostListTypeHelper.MY_WIRES, view);
//                break;
//
//
//            case R.id.customizefeedbtn:
//                Intent intent1 = new Intent(incourtLauncherActivity, CustomizeFeedActivity.class);
//                incourtLauncherActivity.startActivity(intent1);
//                break;

        }
    }



    public void changeListType(String type, View view) {
        setActivatedButtonForFalse(view, true);
        setSeletedTagId("");
        getNewsListHorizontalViewPagerAdapter().setNewsListAdapter(type);
    }

    @Override
    public void setActivatedButtonForFalse(@Nullable View view, boolean flag) {

//        itemView.findViewById(R.id.bookmarkbtn).setActivated(false);
//        itemView.findViewById(R.id.mylikesbtn).setActivated(false);
//        itemView.findViewById(R.id.mywiresbtn).setActivated(false);
//        itemView.findViewById(R.id.customizefeedbtn).setActivated(false);

        CategoryButtonHelper.changeStateToDeActive((LinearLayout) itemView.findViewById(R.id.category_buttons_main_fragments));
        CategoryButtonHelper.changeStateToDeActive((LinearLayout) itemView.findViewById(R.id.topic_buttons_main_fragments));

        if (view != null){
            view.setActivated(true);
            try {
                ((Button) view).setTextColor(IncourtColorHelper.fetchColor(itemView.getContext(), R.attr.category_button_text_color_selected));
            }catch (Exception e){

            }
        }

        if (flag)
            getNewsListHorizontalViewPagerAdapter().getTopbarLayout().setActivatedButtonToFalse(null, false);
    }

    void openSearchActivity() {
        Intent intent1 = new Intent(incourtLauncherActivity, SearchActivity.class);
        incourtLauncherActivity.startActivity(intent1);
    }

    @Override
    public void onClickCategoryButton(View itemView, CategoriesSQL categoriesSQL) {
        setActivatedButtonForFalse(itemView, true);
        setSeletedTagId("C_" + categoriesSQL.getCategory_id());
        getNewsListHorizontalViewPagerAdapter().setCategoryFilterAdapter(categoriesSQL, (categoriesSQL.equals(getCategoriesSQL())));
        setCategoriesSQL(categoriesSQL);
    }

    @Override
    public void onClickCTopicButton(View itemView, TopicSQL topicSQL) {
        setActivatedButtonForFalse(itemView, true);
        setSeletedTagId("T_" + topicSQL.getTopic_id());
        getNewsListHorizontalViewPagerAdapter().setTopicFilterAdapter(topicSQL, (topicSQL.equals(getTopicSQL())));
        setTopicSQL(topicSQL);
    }

    NewsListHorizontalViewPagerAdapter getNewsListHorizontalViewPagerAdapter() {
        return ((NewsListHorizontalViewPagerAdapter) incourtLauncherActivity.getIncourtActivityViewPager().getNewsListHorizontalViewPager().getAdapter());
    }

    public TopicSQL getTopicSQL() {
        return topicSQL;
    }

    public void setTopicSQL(TopicSQL topicSQL) {
        this.topicSQL = topicSQL;
    }

    public CategoriesSQL getCategoriesSQL() {
        return categoriesSQL;
    }

    public void setCategoriesSQL(CategoriesSQL categoriesSQL) {
        this.categoriesSQL = categoriesSQL;
    }

    public View getItemView() {
        return itemView;
    }

    public void reRedraw(){
        categoryButtonHelper.reDrawAllView();
        //topicButtonHelper.reDrawAllView();
    }

    public static void setSeletedTagId(String tag){
        IncourtApplication.getDefaultSharedPreferencesEditor().putString(SELECTED_BUTTON_STRING_KEY, tag).commit();
    }

    public static String getSeletedTagId(){
        return IncourtApplication.getDefaultSharedPreferences().getString(SELECTED_BUTTON_STRING_KEY, "");
    }



}
