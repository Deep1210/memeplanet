package in.incourt.incourtnews.FabricKpi;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;

import in.incourt.incourtnews.core.sql.PostsCategorySQL;
import in.incourt.incourtnews.core.sql.PostsSql;

/**
 * Created by admin on 4/22/2017.
 */

public class IncourtFabricContentView extends IncourtFabricApplication {


    public static void postView(PostsSql postsSql){
        if(postsSql != null && !postsSql.hasAdvertisementModel()) {
            Answers.getInstance().logContentView(new ContentViewEvent()
                    .putContentName(postsSql.getTitle())
                    .putContentType(PostsCategorySQL.getPostCategoryNames(postsSql))
                    .putContentId(postsSql.getTiny_url())
            );
        }
    }

}
