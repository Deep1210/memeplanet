package in.incourt.incourtnews.core.sql;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.orm.dsl.Column;
import com.orm.dsl.Ignore;
import com.orm.dsl.Table;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import in.incourt.incourtnews.FabricKpi.IncourtFabricBookmarks;
import in.incourt.incourtnews.FabricKpi.IncourtFabricLike;
import in.incourt.incourtnews.FabricKpi.IncourtFabricWired;
import in.incourt.incourtnews.core.models.AdvertisementModel;
import in.incourt.incourtnews.core.models.PostsModel;
import in.incourt.incourtnews.helpers.ImageHelper;
import in.incourt.incourtnews.helpers.IncourtDateHelper;
import in.incourt.incourtnews.helpers.IncourtToastHelprer;
import in.incourt.incourtnews.helpers.MyInterestHelper;

/**
 * Created by bhavan on 1/31/17.
 */

@Table
public class PostsSql extends IncourtSQLModel {

    public static int POST_LIKE = 1;
    public static int POST_UN_LIKE = 0;

    public static int POST_BOOKMARKS = 1;
    public static int POST_BOOKMARKS_REMOVE = 0;

    public static int POST_PUBLISH_TYPE_MORE_AT = 1;
    public static int POST_PUBLISH_TYPE_POWERED_BY = 2;

    public static int POST_WIRED = 1;

    public static int PAGE_LIMIT = 5;

    @Ignore
    public AdvertisementModel.Advertisement advertisementModel;

    private Long id;

    @Column(name = "post_id")
    long post_id;

    @Column(name = "title")
    String title;

    @Column(name = "description")
    String description;

    @Column(name = "link")
    String link;

    @Column(name = "url")
    String url;

    @Column(name = "is_approved")
    int is_approved;

    @Column(name = "title_tag")
    String title_tag;

    @Column(name = "meta_des")
    String meta_des;

    @Column(name = "share_cat")
    int share_cat;

    @Column(name = "author_name")
    String author_name;

    @Column(name = "type")
    String type;

    @Column(name = "publisher_id")
    int publisher_id;

    @Column(name = "channel_id")
    int channel_id;

    @Column(name = "share_title")
    String share_title;

    @Column(name = "rss_url")
    String rss_url;

    @Column(name = "pubDate")
    String pubDate;

    @Column(name = "created_at")
    String created_at;

    @Column(name = "updated_at")
    String updated_at;

    @Column(name = "like_count")
    int like_count;

    @Column(name = "tiny_url")
    String tiny_url;

    @Column(name = "publish_type")
    int publish_type;

    @Column(name = "is_like")
    int is_like = 0;

    @Column(name = "is_wired")
    int is_wired = 0;

    @Column(name = "is_bookmarks")
    int is_bookmarks = 0;

    @Column(name = "is_read")
    boolean is_read = false;

    @Column(name = "contributor_name")
    String contributor_name;

    @Ignore
    public List<PostImageSql> postImageSqls;

    @Ignore
    boolean FROM_SHARE = false;

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

    public String getTitle() {
        if(title != null) title.trim();
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIs_approved() {
        return is_approved;
    }

    public void setIs_approved(int is_approved) {
        this.is_approved = is_approved;
    }

    public String getTitle_tag() {
        return title_tag;
    }

    public void setTitle_tag(String title_tag) {
        this.title_tag = title_tag;
    }

    public String getMeta_des() {
        return meta_des;
    }

    public void setMeta_des(String meta_des) {
        this.meta_des = meta_des;
    }

    public int getShare_cat() {
        return share_cat;
    }

    public void setShare_cat(int share_cat) {
        this.share_cat = share_cat;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPublisher_id() {
        return publisher_id;
    }

    public void setPublisher_id(int publisher_id) {
        this.publisher_id = publisher_id;
    }

    public int getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(int channel_id) {
        this.channel_id = channel_id;
    }

    public String getShare_title() {
        return share_title;
    }

    public void setShare_title(String share_title) {
        this.share_title = share_title;
    }

    public String getRss_url() {
        return rss_url;
    }

    public void setRss_url(String rss_url) {
        this.rss_url = rss_url;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public String getTiny_url() {
        return tiny_url;
    }

    public void setTiny_url(String tiny_url) {
        this.tiny_url = tiny_url;
    }

    public int getIs_like() {
        return is_like;
    }

    public void setIs_like(int is_like) {
        this.is_like = is_like;
    }

    public int getIs_bookmarks() {
        return is_bookmarks;
    }

    public void setIs_bookmarks(int is_bookmarks) {
        this.is_bookmarks = is_bookmarks;
    }

    public int getIs_wired() {
        return is_wired;
    }

    public void setIs_wired(int is_wired) {
        this.is_wired = is_wired;
    }

    public boolean is_read() {
        return is_read;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }

    public String getContributor_name() {
        return contributor_name;
    }

    public int getPublish_type() {
        return publish_type;
    }

    public void setPublish_type(int publish_type) {
        this.publish_type = publish_type;
    }

    public void setContributor_name(String contributor_name) {
        this.contributor_name = contributor_name;
    }

    public List<PostImageSql> getPostImages(long post_id) {
        if(postImageSqls != null) return postImageSqls;
        return find(PostImageSql.class, "post_id = ?", new String[]{String.valueOf(post_id)});
    }

    public List<PostImageSql> getPostImages() {
        return this.postImageSqls;
    }

    public void setPostImageSqls(List<PostImageSql> postImageSqls){
        this.postImageSqls = postImageSqls;
    }

    public boolean isFROM_SHARE() {
        return FROM_SHARE;
    }

    public void setFROM_SHARE(boolean FROM_SHARE) {
        this.FROM_SHARE = FROM_SHARE;
    }

    public static int extractRequest(PostsModel postsModel, boolean flag) {
        int i = 0;
        List<PostsModel.PostModel> postModelList = postsModel.getPostModelList();
        if (postModelList != null && postModelList.size() > 0) {
            for (; i < postModelList.size(); i++) {
                savePostModel(postModelList.get(i), flag);
            }
        }
        return i;
    }

    static void savePostModel(PostsModel.PostModel postModel, boolean flag) {
        PostsSql postsSql = mergeRequest(postModel, flag);
        if (postsSql.save() > 0) {
            PostImageSql.postImages(postModel.getImages());
            PostsCategorySQL.extractRequest(postModel.getPostCategoryModels());
            //PostsTopicSQL.extractRequest(postModel.getPostTopicModels());
        }
    }

    static long getId(int post_id) {
        PostsSql postsSql = getByPostId(post_id);
        if (postsSql != null) return postsSql.getId();
        else return 0;
    }

    public static PostsSql getByPostId(int post_id) {
        List<PostsSql> postsSqls = PostsSql.find(PostsSql.class, "post_id = " + post_id, null);
        if (postsSqls != null && postsSqls.size() > 0) return postsSqls.get(0);
        return null;
    }


    static PostsSql mergeRequest(PostsModel.PostModel postModel, boolean flag) {

        PostsSql postsSql = null;

        if(flag) postsSql = getByPostId(postModel.getId());

        if(postsSql == null){
            postsSql = new PostsSql();
        }

        postsSql.setTitle(postModel.getTitle());
        postsSql.setUpdated_at(postModel.getUpdated_at());
        postsSql.setCreated_at(postModel.getCreated_at());
        postsSql.setPubDate(postModel.getPubDate());
        postsSql.setChannel_id(postModel.getChannel_id());
        postsSql.setDescription(postModel.getDescription());
        postsSql.setPost_id(postModel.getId());
        postsSql.setUrl(postModel.getUrl());
        postsSql.setTiny_url(postModel.getTiny_url());
        postsSql.setIs_wired(postModel.getWired_count());
        postsSql.setIs_approved(postModel.getIs_approved());
        postsSql.setLike_count(postModel.getLikes_count());
        postsSql.setIs_like(postModel.getIs_like());
        postsSql.setIs_wired(postModel.getWired_count());
        postsSql.setPublish_type(postModel.getPublish_type());
        postsSql.setLike_count(postModel.getLikes_count());

        if(postModel.getPublisher() != null) {
            postsSql.setAuthor_name(postModel.getPublisher().getName());
        }

        postsSql.setContributor_name(postModel.getPublisher_name());
        
        return postsSql;
    }


    public static List<PostsSql> parseData(PostsModel postsModel, boolean flag) {
        List<PostsSql> postsSqls = new ArrayList<>();
        if (postsModel != null) {
            List<PostsModel.PostModel> postModelList = postsModel.getPostModelList();
            if (postModelList != null && postModelList.size() > 0) {
                for (int i = 0; i < postModelList.size(); i++) {
                    postsSqls.add(i, mergeRequest(postModelList.get(i), flag));
                    postsSqls.get(i).setPostImageSqls(PostImageSql.parseData(postModelList.get(i).getImages(), flag));
                }
            }
        }
        return postsSqls;
    }


    public static long getMaxDate() {
        List<PostsSql> postsSqls = PostsSql.listAll(PostsSql.class, "pubDate DESC");
        if (postsSqls != null && postsSqls.size() > 0) {
            try {
                return IncourtDateHelper.getTime(postsSqls.get(0).getPubDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }


    public static String getMaxDateString() {
        List<PostsSql> postsSqls = PostsSql.listAll(PostsSql.class, "pubDate DESC");
        if (postsSqls != null && postsSqls.size() > 0) {
                return postsSqls.get(0).getPubDate();
        }
        return "";
    }

    public static String getMaxUpdateDate() {
        List<PostsSql> postsSqls = PostsSql.listAll(PostsSql.class, "updated_at DESC");
        if (postsSqls != null && postsSqls.size() > 0) {
            return String.valueOf(postsSqls.get(0).getUpdated_at());
        }
        return "";
    }

    public static int getMaxPostId(String type) {
        List<PostsSql> postsSqls = PostsSql.listAll(PostsSql.class, "post_id DESC");
        if (postsSqls != null && postsSqls.size() > 0) {
            return Integer.parseInt(String.valueOf(postsSqls.get(0).getPost_id()));
        }
        return 0;
    }


    public static List<PostsSql> getList(int page, int post_id) {
        return PostsSql.find(PostsSql.class, "id > ? AND post_id > ? AND is_approved = ? AND post_id != ?", new String[]{"0", "0", "1", String.valueOf(post_id)}, null, getPublishOrder(), getLimitOffset(page));
    }

    public static List<PostsSql> getBookmarksList(int page, int post_id) {
        return PostsSql.find(PostsSql.class, "is_bookmarks = ? AND is_approved = ? AND post_id != ?", new String[]{"1", "1", String.valueOf(post_id)}, null, getPublishOrder(), getLimitOffset(page));
    }


    public static List<PostsSql> getLikeList(int page, int post_id) {
        return PostsSql.find(PostsSql.class, "is_like = ? AND is_approved = ? AND post_id != ?", new String[]{"1", "1", String.valueOf(post_id)}, null, getPublishOrder(), getLimitOffset(page));
    }

    public static List<PostsSql> getMyWire(int page_no, int post_id) {
        return PostsSql.find(PostsSql.class, "is_wired = 1 AND is_approved = 1 AND post_id != " + String.valueOf(post_id), null, null, getPublishOrder(), getLimitOffset(page_no));
    }

    public static String getPublishOrder(){
        return "pubDate DESC";
    }


    public static String getLimitOffset(@Nullable int page){
        return null;
        /*if(page <= 0) page = 1;
        return String.valueOf((page-1) * PAGE_LIMIT) + ", " + String.valueOf(PAGE_LIMIT);*/
    }


    public static boolean getLike(PostsSql postsSql) {
        postsSql = getCheckPostSqlExit(postsSql);
        postsSql.setLike_count(postsSql.getLike_count()+1);
        if(postsSql == null)return false;
        postsSql.setIs_like(PostsSql.POST_LIKE);
        IncourtFabricLike.logPostLike(postsSql);
        return likeChange(postsSql);
    }

    public static boolean getUnLike(PostsSql postsSql) {
        postsSql = getCheckPostSqlExit(postsSql);
        postsSql.setLike_count(postsSql.getLike_count()-1);
        postsSql.setIs_like(PostsSql.POST_UN_LIKE);
        IncourtFabricLike.logPostDisLike(postsSql);
        return likeChange(postsSql);
    }


    public static long getWired(PostsSql postsSql){
        postsSql = getCheckPostSqlExit(postsSql);
        postsSql.setIs_wired(PostsSql.POST_WIRED);
        PostsModel.getWired(postsSql.getPost_id());
        IncourtFabricWired.logPostWired(postsSql);
        return postsSql.save();
    }


    public static boolean likeChange(PostsSql postsSql){
        if (postsSql.save() > 0) {
            PostsModel.getLikeChange(postsSql.getPost_id(), postsSql.getIs_like());
            return true;
        }
        return false;
    }

    static boolean bookmarksChange(PostsSql postsSql){
        if (postsSql.save() > 0) {
            IncourtToastHelprer.showBookmarksToast(postsSql.getIs_bookmarks());
            PostsModel.bookmarkChnage(postsSql.post_id, postsSql.getIs_bookmarks());
            return true;
        }
        return false;
    }

    public static boolean postBookmarks(PostsSql postsSql){
        postsSql = getCheckPostSqlExit(postsSql);
        postsSql.setIs_bookmarks(PostsSql.POST_BOOKMARKS);
       // IncourtFabricBookmarks.logPostBookmarks(postsSql);
        return bookmarksChange(postsSql);
    }

    public static boolean postBookmarksRemove(PostsSql postsSql){
        postsSql = getCheckPostSqlExit(postsSql);
        postsSql.setIs_bookmarks(PostsSql.POST_BOOKMARKS_REMOVE);
       // IncourtFabricBookmarks.logPostBookmarksRemove(postsSql);
        return bookmarksChange(postsSql);
    }


    public static long getPostsCount(){
        return PostsSql.count(PostsSql.class);
    }


    public static PostsSql getFirstPost(){
        List<PostsSql> postsSqls = PostsSql.getList(0, 0);
        if(postsSqls != null && postsSqls.size() > 0) return postsSqls.get(0);
        return null;
    }

    public static List<PostsSql> getTopStories(int page_no, int post_id) {
        return PostsSql.findWithQuery(PostsSql.class, PostsSql.getTypeQuery(PostsSql.getTypeCategoryIds(CategoriesSQL.CATEGORY_TYPE_TOP_STORIES), page_no, post_id));
    }

    public static int getPostCountAboveId(Long max_id){
        return (int) PostsSql.count(PostsSql.class, "id > ?", new String[]{String.valueOf(max_id)});
    }

    public static Long getMaxId(String type){
        List<PostsSql> postsSql = PostsSql.findWithQuery(PostsSql.class, "SELECT * from POSTS_SQL order by id desc limit 1", null);
        if(postsSql != null && postsSql.size() > 0){
            return postsSql.get(0).getId();
        }
        return Long.valueOf(0);
    }

    public static List<PostsSql> getTrending(int page_no, int post_id) {
        return PostsSql.findWithQuery(PostsSql.class, PostsSql.getTypeQuery(PostsSql.getTypeCategoryIds(CategoriesSQL.CATEGORY_TYPE_TRENDING), page_no, post_id));
    }

    public static String getTypeCategoryIds(int type){
        String ids = "";
        List<CategoriesSQL> categoriesSQL = CategoriesSQL.getCategoriesByType(type);
        if(categoriesSQL != null && categoriesSQL.size() > 0){
            for(int i = 0; i < categoriesSQL.size() ; i++){
                if(ids.length() > 0) ids = ids + "," + categoriesSQL.get(i).getCategory_id();
                else ids = String.valueOf(categoriesSQL.get(i).getCategory_id());
            }
        }
        return ids;
    }

    public static List<PostsSql> getMyFeed(int page_no, int post_id) {
        return PostsSql.findWithQuery(PostsSql.class, PostsSql.getTypeQuery(MyInterestHelper.get(), page_no, post_id));
    }

    public static String getTypeQuery(String category_ids, int page_no, int post_id){
        return  "SELECT DISTINCT P.* FROM POSTS_SQL P JOIN POSTS_CATEGORY_SQL C ON (C.post_id = P.post_id) where is_approved = 1 AND P.post_id != "+ String.valueOf(post_id) +" AND  C.category_id IN ("+category_ids+") order by P.pubDate DESC";
    }

    static PostsSql getCheckPostSqlExit(PostsSql postsSql){
        if(postsSql != null && postsSql.getPost_id() > 0){
            PostsSql tmpPostSql = PostsSql.getByPostId((int) postsSql.getPost_id());
            if(tmpPostSql != null) return tmpPostSql;
        }
        PostImageSql.savePostImageFromSql(postsSql.getPostImages());
        return postsSql;
    }

    public static void unReadNewsPost(PostsSql postsSql){
        if(postsSql == null && postsSql.getPost_id() <= 0) return;
        postsSql = PostsSql.getByPostId((int) postsSql.getPost_id());
        if(postsSql != null && postsSql.is_read() == false) {
            postsSql.setIs_read(true);
            postsSql.save();
        }
    }

    public static int getUnReadCount(){
        return PostsSql.find(PostsSql.class, "is_read = ? AND is_approved = ?", new String[]{"0", "1"}).size();
    }


    public static PostsSql getPostsSqlFormSerialize(String serialize){
        List<PostsSql> postsSqls = PostsSql.parseData(new Gson().fromJson(serialize, PostsModel.class), false);
        if(postsSqls != null && postsSqls.size() > 0) return postsSqls.get(0);
        return null;
    }

    public AdvertisementModel.Advertisement getAdvertisementModel() {
        return advertisementModel;
    }

    public void setAdvertisementModel(AdvertisementModel.Advertisement advertisementModel) {
        this.advertisementModel = advertisementModel;
    }

    public boolean hasAdvertisementModel(){
        return (getAdvertisementModel() != null);
    }

    public static boolean hasVideo(PostsSql postsSql){
        List<PostImageSql> postImageSqls =  postsSql.getPostImages();
        if(postImageSqls != null && postImageSqls.size() > 0){
            return (postImageSqls.get(0).getType().equals(ImageHelper.IMAGE_TYPE_VIDEO));
        }
        return false;
    }

    public static int getMinPostId(String type) {
        List<PostsSql> postsSqls = PostsSql.listAll(PostsSql.class, "post_id ASC");
        if (postsSqls != null && postsSqls.size() > 0) {
            return Integer.parseInt(String.valueOf(postsSqls.get(0).getPost_id()));
        }
        return 0;
    }

    public static void removeRecords(final int update_count) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(PostsSql.count(PostsSql.class) > 200){
                    List<PostsSql> postsSqls = PostsSql.find(PostsSql.class, "is_bookmarks != ? and is_like != ? and is_wired <= ?", new String[]{"1", "1", "0"}, null, "pubDate DESC", "200, 100");
                    if(postsSqls == null)return;
                    for(int i = 0; i < postsSqls.size(); i++){
                        PostsSql.delete(postsSqls.get(i));
                        PostsCategorySQL.removeRecord(postsSqls.get(i).getPost_id());
                    }
                }
            }
        }).start();
    }


    public static void updateExistsPost(PostsModel postsModel, boolean b, List<PostsSql> postsSqlList) {
        List<PostsSql> postsSqls = PostsSql.parseData(postsModel, b);
        if(postsSqls != null && postsSqls.size() > 0){
            for(int i = 0; i < postsSqls.size(); i++){
                if(PostsSql.getByPostId((int) postsSqls.get(i).getPost_id()) != null) {
                    postsSqls.get(i).save();
                    PostImageSql.updatePostImages(postsModel.getPostModelList().get(i).getImages(), (int) postsSqls.get(i).getPost_id());
                    updateCurrentPostListIndex(postsSqlList, postsSqls.get(i));
                }
            }
        }
    }

    private static void updateCurrentPostListIndex(List<PostsSql> postsSqls, PostsSql postsSql) {
        if(postsSqls != null && postsSqls.size() > 0){
            for(int i = 0; i < postsSqls.size() ; i ++){
                if(postsSqls.get(i).getId() != null && postsSqls.get(i).getId().equals(postsSql.getId())){
                    postsSqls.set(i, postsSql);
                }
            }
        }
    }

    public static void deleteOldRecords(PostsModel body) {
        if(body != null && body.getPostModelList() != null){
            removeRecords(body.getPostModelList().size());
        }
    }
}
