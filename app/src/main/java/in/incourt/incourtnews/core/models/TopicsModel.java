package in.incourt.incourtnews.core.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by bhavan on 1/30/17.
 */

public class TopicsModel extends IncourtModel {

    @SerializedName("topics")
    @Expose
    List<Topic> topicList;

    public List<Topic> getTopicList() {
        return topicList;
    }

    public void setTopicList(List<Topic> topicList) {
        this.topicList = topicList;
    }


    public class Topic {

        @SerializedName("id")
        @Expose
        int id;

        @SerializedName("topic")
        @Expose
        String name;

        @SerializedName("created_at")
        @Expose
        String created_at;

        @SerializedName("updated_at")
        @Expose
        String updated_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
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
    }

    public Iterator<String> getRecentSearchIterator(){
        Set<String> recentSearch = new HashSet<>();
        List<Topic> topicList = getTopicList();
        if(topicList != null && topicList.size() > 0){
            for (int i = 0; i < topicList.size() ; i++){
                recentSearch.add(topicList.get(i).getName());
            }
        }
        return recentSearch.iterator();
    }

}
