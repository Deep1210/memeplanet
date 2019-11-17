package in.incourt.incourtnews.core.sql;

import in.incourt.incourtnews.core.models.PostCategoryModel;
import com.orm.dsl.Column;
import com.orm.dsl.Table;

import java.util.List;

/**
 * Created by bhavan on 2/22/17.
 */

@Table
public class PostsCategorySQL extends IncourtSQLModel {

    private Long id;

    @Column(name = "post_id")
    long post_id;

    @Column(name = "category_id")
    long category_id;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public long getPost_id() {
        return post_id;
    }

    public void setPost_id(long post_id) {
        this.post_id = post_id;
    }

    public long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
    }

    public static void extractRequest(List<PostCategoryModel> postCategoryModels){
        if(postCategoryModels != null && postCategoryModels.size() > 0){
            for(int i =0 ; i < postCategoryModels.size() ; i++){
                saveCategory(postCategoryModels.get(i));
            }
        }

    }

    public static void saveCategory(PostCategoryModel postsCategoryModel){
        if(checkExists(postsCategoryModel.getCategory_id(), postsCategoryModel.getPost_id()))return;
        PostsCategorySQL postsCategorySQL = merge(postsCategoryModel.getCategory_id(), postsCategoryModel.getPost_id());
        postsCategorySQL.save();
    }

    static PostsCategorySQL merge(long category_id, long post_id){
        PostsCategorySQL postsCategorySQL = new PostsCategorySQL();
        postsCategorySQL.setCategory_id(category_id);
        postsCategorySQL.setPost_id(post_id);
        return postsCategorySQL;
    }

    static boolean checkExists(long category_id, long post_id){
        String[] strings = new String[2];
        strings[0] = String.valueOf(category_id);
        strings[1] = String.valueOf(post_id);
        List<PostsCategorySQL> postsCategorySQL =  PostsCategorySQL.find(PostsCategorySQL.class, "category_id = ? AND post_id = ?", strings);
        if(postsCategorySQL != null && postsCategorySQL.size() > 0) return true;
        return false;
    }

    public static int removeRecord(long post_id) {
        return PostsCategorySQL.deleteAll(PostsCategorySQL.class, "post_id  = ?", new String[]{String.valueOf(post_id)});
    }

    public static String getPostCategoryNames(PostsSql postsSql) {
        String category_names = "";
        List<CategoriesSQL> categoriesSQLs = getPostsCategorySQL((int) postsSql.getPost_id());
        if(categoriesSQLs != null && categoriesSQLs.size() > 0){
            for (int i = 0; i < categoriesSQLs.size(); i++)
            category_names += ((category_names.length() > 0)? ", ":"")+ categoriesSQLs.get(i).getName();
        }
        return category_names;
    }


    public static List<CategoriesSQL> getPostsCategorySQL(int post_id){
        return PostsCategorySQL.findWithQuery(CategoriesSQL.class, "Select DISTINCT * from CATEGORIES_SQL C JOIN POSTS_CATEGORY_SQL PC ON (PC.category_id = C.id) where PC.post_id = " + post_id);
    }
}
