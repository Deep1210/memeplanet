package in.incourt.incourtnews.core.sql;

import android.support.annotation.NonNull;

import com.orm.dsl.Column;
import com.orm.dsl.Table;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import in.incourt.incourtnews.IncourtApplication;
import in.incourt.incourtnews.core.http.RestAdapter;
import in.incourt.incourtnews.core.models.TopicsModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by bhavan on 1/30/17.
 */

@Table
public class TopicSQL extends IncourtSQLModel  {
    private Long id;


    @Column(name = "topic_id")
    int topic_id;

    @Column(name = "name")
    String name;

    @Column(name = "created_at")
    String created_at;

    @Column(name = "updated_at")
    String updated_at;


    public TopicSQL() {

    }



    @Override
    public Long getId() {
        return id;
    }

    public int getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(int topic_id) {
        this.topic_id = topic_id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    static long updateTopic(TopicsModel.Topic topic) {
        TopicSQL topicSQL = mergeRequest(topic);
        return topicSQL.save();

    }



    static TopicSQL mergeRequest(TopicsModel.Topic topic) {
        TopicSQL topicSQL = getByTopicId(topic.getId());
        if(topicSQL == null){
            topicSQL = new TopicSQL();
        }
        topicSQL.setTopic_id(topic.getId());
        topicSQL.setName(topic.getName());
        topicSQL.setCreated_at(topic.getCreated_at());
        topicSQL.setUpdated_at(topic.getUpdated_at());
        return topicSQL;
    }


    static long getId(int topic_id) {
        TopicSQL topicSQL = getByTopicId(topic_id);
        if (topicSQL != null) return topicSQL.getId();
        else return 0;
    }

    static boolean ckeckExists(int topic_id) {
        return (getByTopicId(topic_id) != null) ? true : false;
    }

    public static TopicSQL getByTopicId(int topic_id) {
        List<TopicSQL> topicSQLList = TopicSQL.find(TopicSQL.class, "topic_id = " + topic_id, null);
        if (topicSQLList != null && topicSQLList.size() > 0) return topicSQLList.get(0);
        return null;
    }

    public static long getMaxDate() {
        List<TopicSQL> topicSQLList = TopicSQL.listAll(TopicSQL.class, "updated_at DESC");
        if (topicSQLList != null && topicSQLList.size() > 0) {
            try {
                return new SimpleDateFormat("yyyy-mm-dd HH:mm:ss").parse(topicSQLList.get(0).getUpdated_at()).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static void syncDown() {
        RestAdapter.get().topicList().enqueue(new Callback<TopicsModel>() {
            @Override
            public void onResponse(Call<TopicsModel> call, Response<TopicsModel> response) {
                if (response.body() != null) {
                    extractRequest(response.body());
                }
            }

            @Override
            public void onFailure(Call<TopicsModel> call, Throwable t) {

            }
        });
    }

    static void extractRequest(TopicsModel topicsModel) {
        List<TopicsModel.Topic> topicList = topicsModel.getTopicList();
        String topics_ids = "";
        if (topicList != null && topicList.size() > 0) {
            for (int i = 0; i < topicList.size(); i++) {
                updateTopic(topicList.get(i));
                topics_ids += ((topics_ids.length() > 0)? ",":"") + String.valueOf(topicList.get(i).getId());
            }
            IncourtApplication.topicSyncStatus= true;
        }
        deleteOldTopics(topics_ids);
    }

    private static void deleteOldTopics(String topics_ids) {
        TopicSQL.deleteAll(TopicSQL.class, "topic_id not in("+ topics_ids +")", new String[]{});
    }

    public static List<TopicSQL> getList() {
        return TopicSQL.find(TopicSQL.class, null, null, "id", "created_at DESC", "20");
    }

    public static List<TopicSQL> parseData(TopicsModel topicsModel){
        List<TopicSQL> topicSQLList = new ArrayList<>();
        if (topicsModel != null) {
            List<TopicsModel.Topic> topicList = topicsModel.getTopicList();
            if (topicList != null && topicList.size() > 0) {
                for (int i = 0; i < topicList.size(); i++) {
                    topicSQLList.add(i, mergeRequest(topicList.get(i)));
                }
            }
        }
        return topicSQLList;
    }



}

