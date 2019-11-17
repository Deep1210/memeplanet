package in.incourt.incourtnews.helpers.interfaces;

import android.support.annotation.Nullable;
import android.view.View;

import in.incourt.incourtnews.core.sql.CategoriesSQL;
import in.incourt.incourtnews.core.sql.TopicSQL;

/**
 * Created by bhavan on 3/6/17.
 */

public interface CategoryButtonHelperInterface {


    void onClickCategoryButton(View itemView, CategoriesSQL categoriesSQL);


    void onClickCTopicButton(View itemView, TopicSQL topicSQL);

    void setActivatedButtonForFalse(@Nullable View view, boolean flag);
}
