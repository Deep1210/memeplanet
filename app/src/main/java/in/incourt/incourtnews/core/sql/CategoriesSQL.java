package in.incourt.incourtnews.core.sql;


/**
 * Created by bhavan on 1/22/17.
 */

import com.orm.dsl.Column;
import com.orm.dsl.Table;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import in.incourt.incourtnews.IncourtApplication;
import in.incourt.incourtnews.core.http.RestAdapter;
import in.incourt.incourtnews.core.models.CategoriesModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Table
public class CategoriesSQL extends IncourtSQLModel {


    public static int CATEGORY_TYPE_TRENDING = 1;

    public static int CATEGORY_TYPE_TOP_STORIES = 2;

    private Long id;

    @Column(name = "category_id")
    int category_id;

    @Column(name = "name")
    String name;

    @Column(name = "index_position")
    int index_position;

    @Column(name = "is_active")
    int is_active;

    @Column(name = "update_at")
    String update_at;

    @Column(name = "type")
    int type;

    public CategoriesSQL(){

    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex_position() {
        return index_position;
    }

    public void setIndex_position(int index_position) {
        this.index_position = index_position;
    }

    public String getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(String update_at) {
        this.update_at = update_at;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    static long updateCategory(CategoriesModel.Category category){
        CategoriesSQL categoriesSQL = mergeRequest(category);
        return categoriesSQL.save();
    }

    static CategoriesSQL mergeRequest(CategoriesModel.Category category){
        CategoriesSQL  categoriesSQL = getByCategoryId(category.getId());
        if(categoriesSQL == null) {
            categoriesSQL = new CategoriesSQL();
        }
        categoriesSQL.setCategory_id(category.getId());
        categoriesSQL.setIndex_position(category.getPosition());
        categoriesSQL.setName(category.getName());
        categoriesSQL.setUpdate_at(category.getUpdated_at().toString());
        categoriesSQL.setIs_active(category.getIs_active());
        categoriesSQL.setType(category.getType());
        return categoriesSQL;
    }


    static long getId(int category_id){
        CategoriesSQL categoriesSQL = getByCategoryId(category_id);
        if(categoriesSQL != null) return categoriesSQL.getId();
        else return 0;
    }

    static boolean ckeckExists(int category_id){
        return (getByCategoryId(category_id) != null)?true:false;
    }

    public static CategoriesSQL getByCategoryId(int category_id){
        List<CategoriesSQL> categoriesSQLList =  find(CategoriesSQL.class, "category_id = " + category_id, null);
        if(categoriesSQLList != null && categoriesSQLList.size() > 0) return categoriesSQLList.get(0);
        return null;
    }

    public static long getMaxDate(){
        List<CategoriesSQL> categoriesSQLs = listAll(CategoriesSQL.class, "update_at DESC");
        if(categoriesSQLs != null && categoriesSQLs.size() > 0){
            try {
                return  new SimpleDateFormat("yyyy-mm-dd HH:mm:ss").parse(categoriesSQLs.get(0).getUpdate_at()).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static void syncDown(){
        RestAdapter.get().categoryList().enqueue(new Callback<CategoriesModel>() {
            @Override
            public void onResponse(Call<CategoriesModel> call, Response<CategoriesModel> response) {
                if(response.body() != null) {
                    extractRequest(response.body());
                }
            }

            @Override
            public void onFailure(Call<CategoriesModel> call, Throwable t) {

            }
        });

    }

    static void extractRequest(CategoriesModel categoriesModel){
        String category_ids = "";
        List<CategoriesModel.Category> category = categoriesModel.getCategoryList();
        if(category != null && category.size() > 0){
            for (int i = 0; i < category.size(); i++){
                updateCategory(category.get(i));
                category_ids += ((category_ids.length() > 0)? ",":"") + String.valueOf(category.get(i).getId());
            }
            IncourtApplication.categorySyncStatus = true;
            deleteOldCategory(category_ids);
        }
    }

    private static void deleteOldCategory(String category_ids) {
        CategoriesSQL.deleteAll(CategoriesSQL.class, "category_id not in("+ category_ids +")", new String[]{});
    }

    public static List<CategoriesSQL> getList(){
        return CategoriesSQL.find(CategoriesSQL.class, "is_active = ?", new String[]{"1"}, null, "index_position ASC", null);
    }

    public static List<CategoriesSQL> getCategoriesByType(int type){
        return find(CategoriesSQL.class, "type = "+ type + " and is_active = 1", null);
    }

}
