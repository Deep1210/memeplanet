package in.incourt.incourtnews.others;

import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import in.incourt.incourtnews.R;
import in.incourt.incourtnews.core.sql.TopicSQL;
import in.incourt.incourtnews.helpers.IncourtStringHelper;
import in.incourt.incourtnews.helpers.ViewHelpers;
import in.incourt.incourtnews.helpers.interfaces.CategoryButtonHelperInterface;
import in.incourt.incourtnews.layouts.CategoryPageLayout;

/**
 * Created by bhavan on 1/30/17.
 */

public class TopicButtonHelper implements View.OnClickListener{
    int row_count = 0;
    LinearLayout buttonfragmentcontainer;
    Button categoryButton;
    View view;
    ContextThemeWrapper categoryButtonThemeWrapper ;
    ContextThemeWrapper categoryRowThemeWrapper;
    LinearLayout  rowContainer;
    LinearLayout hiddenContainer;
    int row_char_count = 0;
    public TextView categoryViewMore;
    LinearLayout.LayoutParams layoutParams;

    int max_row_visible = 3;

    List<TopicSQL> topicSQLs;

    int current_index;

    boolean hiddenRowInsert = false;

    int row_element_count = 1;

    boolean read_more_status = false;

    CategoryButtonHelperInterface categoryButtonHelperInterface;

    public TopicButtonHelper(View view, CategoryButtonHelperInterface categoryButtonHelperInterface) {
        this.view = view;
        this.categoryButtonHelperInterface = categoryButtonHelperInterface;
        buttonfragmentcontainer = (LinearLayout) view.findViewById(R.id.topic_buttons_main_fragments);
        categoryButtonThemeWrapper = new ContextThemeWrapper(view.getContext(), R.style.category_button);
        categoryRowThemeWrapper = new ContextThemeWrapper(view.getContext(), R.style.category_button_container);

        topicSQLs = TopicSQL.getList();

        layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        int marginInDpHorizonlal = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 6, view.getResources()
                        .getDisplayMetrics());

        int marginInDpVertical = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 7, view.getResources()
                        .getDisplayMetrics());


        layoutParams.setMargins(marginInDpHorizonlal, marginInDpVertical, marginInDpHorizonlal, marginInDpVertical);

        init();
    }


    void init() {
        if(buttonfragmentcontainer != null && buttonfragmentcontainer.getChildCount() > 0){
            setTodefault();
        }

        rowContainer = createNewRowContainer();

        if(topicSQLs != null && topicSQLs.size() > 0){
            viewSetUp();return;
        }

        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                topicSQLs = TopicSQL.getList();
                if(topicSQLs != null && topicSQLs.size() >0 ) {
                    viewSetUp();
                }else{
                    init();
                }
            }
        }, 1000);
    }

    void viewSetUp(){

        topicSQLs = TopicSQL.getList();

        if (topicSQLs.size() > 0) {

            boolean last_insert = true;
            for (int i = 0; i < topicSQLs.size(); i++) {
                current_index = i;
                if (row_char_count >= 23 || (row_element_count % 3) == 0) {
                    rowInsert();
                } else {
                    last_insert = true;
                }
                buttonInsert();
            }
            if (last_insert) {
                lastInsert();
            }
        }
        if(read_more_status && categoryViewMore != null) categoryViewMore.callOnClick();
    }

    void buttonInsert(){
        categoryButton = new Button(categoryButtonThemeWrapper, null, 0);
        String button_text = topicSQLs.get(current_index).getName();
        categoryButton.setText(IncourtStringHelper.capitalize(button_text));
        categoryButton.setActivated(false);
        categoryButton.setTag(topicSQLs.get(current_index));
        categoryButton.setOnClickListener(this);
        if(CategoryPageLayout.getSeletedTagId().equals("T_" + topicSQLs.get(current_index).getTopic_id())){
            categoryButtonHelperInterface.setActivatedButtonForFalse(categoryButton, false);
        }

        categoryButton.setId(R.id.category_list_button_click);
        categoryButton.setLayoutParams(layoutParams);
        rowCountCounter();
        row_element_count++;
        rowContainer.addView(categoryButton);
    }

    void rowCountCounter(){
        row_char_count += topicSQLs.get(current_index).getName().length();
        if(current_index+1 < topicSQLs.size()){
            row_char_count += topicSQLs.get(current_index+1).getName().length();
        }
    }

    void rowInsert(){
        if(row_count >= max_row_visible && hiddenRowInsert == false){
            hidenContainerInsert();
        }
        if(hiddenRowInsert){
            if(rowContainer.getParent() != null) {
                ((ViewGroup)rowContainer.getParent()).removeView(rowContainer); // <- fix
            }
            hiddenContainer.addView(rowContainer);
        }
        else{
            if(rowContainer.getParent() != null) {
                ((ViewGroup)rowContainer.getParent()).removeView(rowContainer); // <- fix
            }
            buttonfragmentcontainer.addView(rowContainer);
        }

        rowContainer = createNewRowContainer();
        row_char_count = 0;
        row_element_count = 1;
        row_count++;
    }

    void hidenContainerInsert(){
        hiddenRowInsert = true;
        hiddenContainer = new LinearLayout(new ContextThemeWrapper(view.getContext(), R.style.category_hide_container));
        hiddenContainer.setId(R.id.category_button_row_container_hidden);
    }

    void lastInsert(){
        if(hiddenRowInsert){
            hiddenContainer.addView(rowContainer);
            buttonfragmentcontainer.addView(hiddenContainer);
            insertViewMore();
        }
        else{
            buttonfragmentcontainer.addView(rowContainer);
        }
    }

    void insertViewMore(){
        rowContainer = new LinearLayout(categoryRowThemeWrapper);
        ContextThemeWrapper viewMore = new ContextThemeWrapper(view.getContext(), R.style.category_view_more_button);
        categoryViewMore = new TextView(viewMore);
        categoryViewMore.setText("View More +");

        categoryViewMore.setOnClickListener(this);
        categoryViewMore.setId(R.id.category_view_more);
        rowContainer.addView(categoryViewMore);
        buttonfragmentcontainer.addView(rowContainer);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.category_view_more:
                read_more_status = ViewHelpers.visibilityChange(hiddenContainer, categoryViewMore);
                break;
            case R.id.category_list_button_click:
                TopicSQL topicSQL = (TopicSQL) view.getTag();
                categoryButtonHelperInterface.onClickCTopicButton(view, topicSQL);
                break;
        }
    }

    LinearLayout createNewRowContainer(){
        LinearLayout l = new LinearLayout(categoryRowThemeWrapper, null, 0);
        l.setId(R.id.category_button_row_container);
        return l;
    }

    public void reDrawAllView() {
        init();
    }

    private void setTodefault() {
        buttonfragmentcontainer.removeAllViewsInLayout();
        hiddenRowInsert = false;
        row_element_count = 1;
        row_count = 0;
        row_char_count = 0;
    }
}