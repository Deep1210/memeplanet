package in.incourt.incourtnews.threads;

import in.incourt.incourtnews.core.models.PostsModel;

/**
 * Created by bhavan on 5/18/17.
 */

public interface AutoUpdateInterface {

    void onUpdateSuccess(PostsModel postsModel);

}
