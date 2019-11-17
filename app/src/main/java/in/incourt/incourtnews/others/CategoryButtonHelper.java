package in.incourt.incourtnews.others;

import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import in.incourt.incourtnews.R;
import in.incourt.incourtnews.core.sql.CategoriesSQL;
import in.incourt.incourtnews.helpers.IncourtColorHelper;
import in.incourt.incourtnews.helpers.IncourtStringHelper;
import in.incourt.incourtnews.helpers.MyInterestHelper;
import in.incourt.incourtnews.helpers.ViewHelpers;
import in.incourt.incourtnews.helpers.interfaces.CategoryButtonHelperInterface;
import in.incourt.incourtnews.layouts.CategoryPageLayout;

/**
 * Created by bhavan on 1/24/17.
 */

public class CategoryButtonHelper implements View.OnClickListener{

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

    List<CategoriesSQL> categoryList;

    int current_index;

    boolean hiddenRowInsert = false;

    boolean read_more = true;

    int row_element_count = 1;

    boolean read_more_status = false;

    String from;

    CategoryButtonHelperInterface categoryButtonHelperInterface;

    public CategoryButtonHelper(View itemView, CategoryButtonHelperInterface categoryButtonHelperInterface, boolean read_more,String from) {
        this.view = itemView;
        this.categoryButtonHelperInterface  = categoryButtonHelperInterface;
        this.read_more = read_more;
        this.from = from;
        preInit();
    }

    public CategoryButtonHelper(View itemView) {
        this.view = itemView;
    }



    public CategoryButtonHelper(final View itemView, CategoryButtonHelperInterface categoryButtonHelperInterface,String from) {
        this.view = itemView;
        this.categoryButtonHelperInterface  = categoryButtonHelperInterface;
        this.from = from;
        preInit();
    }



    public void preInit(){
        categoryButtonThemeWrapper = new ContextThemeWrapper(view.getContext(), R.style.category_button);
        categoryRowThemeWrapper = new ContextThemeWrapper(view.getContext(), R.style.category_button_container);
        init();
    }



    void init() {
        if(buttonfragmentcontainer == null)
            buttonfragmentcontainer = (LinearLayout) view.findViewById(R.id.category_buttons_main_fragments);

        if(buttonfragmentcontainer != null){
            setTodefault();
        }

        rowContainer = createNewRowContainer();
        categoryList = CategoriesSQL.getList();

        if(categoryList != null && categoryList.size() > 0){
            viewSetUp();return;
        }
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                categoryList = CategoriesSQL.getList();
                if(categoryList != null && categoryList.size() >0 ) {
                    viewSetUp();
                }else{
                    init();
                }
            }
        }, 1000);


        if(MyInterestHelper.getRaw()!=null) {
            Log.e("Query: ", MyInterestHelper.getRaw());
            try {

                JSONArray array = new JSONArray(MyInterestHelper.getRaw());
                Log.e("Array: ", array.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    private void setTodefault() {
        buttonfragmentcontainer.removeAllViewsInLayout();
        hiddenRowInsert = false;
        row_element_count = 1;
        row_count = 0;
        row_char_count = 0;
    }

    void viewSetUp(){
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

        if (categoryList.size() > 0) {

            boolean last_insert = true;
            for (int i = 0; i < categoryList.size(); i++) {
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
        String button_text = categoryList.get(current_index).getName();
        categoryButton.setText(IncourtStringHelper.capitalize(button_text));
        if(CategoryPageLayout.getSeletedTagId().equals("C_" + categoryList.get(current_index).getCategory_id())){
            categoryButtonHelperInterface.setActivatedButtonForFalse(categoryButton, false);
        }

        categoryButton.setTag(categoryList.get(current_index));
        categoryButton.setOnClickListener(this);

        if(from=="feed") {
            if (MyInterestHelper.getRaw() != null) {
                try {

                    //String value = MyInterestHelper.getRaw();
                    JSONArray array = new JSONArray(MyInterestHelper.getRaw());

                    for (int j = 0; j < categoryList.size(); j++) {
                        for (int i = 0; i < array.length(); i++) {
                            int id = Integer.parseInt(array.get(i).toString());
                            if (categoryList.get(j).getCategory_id() == id) {

                                if (categoryButton.getTag() == categoryList.get(j)) {
                                    categoryButton.setActivated(true);
                                    categoryButton.setTextColor(Color.WHITE);
                                } else {
                                    // categoryButton.setActivated(false);
                                }
                            }
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                categoryButton.setActivated(false);
            }
        }else{
            categoryButton.setActivated(false);
        }


        categoryButton.setId(R.id.category_list_button_click);
        categoryButton.setLayoutParams(layoutParams);
        rowCountCounter();
        row_element_count++;
        rowContainer.addView(categoryButton);
    }

    void rowCountCounter(){
        row_char_count += categoryList.get(current_index).getName().length();
        if(current_index+1 < categoryList.size()){
            row_char_count += categoryList.get(current_index+1).getName().length();
        }
    }

    void rowInsert(){
        if(row_count >= max_row_visible && hiddenRowInsert == false && this.read_more){
            hidenContainerInsert();
        }
        if(hiddenRowInsert && this.read_more){
            hiddenContainer.addView(rowContainer);
        }
        else{
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
                categoryButtonClick(view, (CategoriesSQL) view.getTag());
                break;

        }
    }

    private void categoryButtonClick(View view, CategoriesSQL tag) {
        categoryButtonHelperInterface.onClickCategoryButton(view, (CategoriesSQL) view.getTag());
    }

    public static void changeStateToDeActive(LinearLayout l) {
        for(int i = 0; i < l.getChildCount(); i++){

            if(l.getChildAt(i).getId() == R.id.category_list_button_click){
                l.getChildAt(i).setActivated(false);
                ((Button) l.getChildAt(i)).setTextColor(IncourtColorHelper.fetchColor(l.getContext(), R.attr.category_button_text_color));
            }

            if(l.getChildAt(i).getId() == R.id.category_button_row_container || l.getChildAt(i).getId() == R.id.category_button_row_container_hidden){
                changeStateToDeActive((LinearLayout) l.getChildAt(i));
            }
        }
    }

    LinearLayout createNewRowContainer(){
        LinearLayout l = new LinearLayout(categoryRowThemeWrapper, null, 0);
        l.setId(in.incourt.incourtnews.R.id.category_button_row_container);
        return l;
    }

    public void reDrawAllView(){
        init();
    }

}