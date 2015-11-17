package models.metadata;

import com.fasterxml.jackson.databind.JsonNode;
import util.APICall;
import util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Xunrong Li
 * @andrewID: xunrongl
 * Created on 11/5/15.
 */
public class Post {

    //restful uris
    private static final String GET_POST_SERVICES_CALL = Constants.NEW_BACKEND+"posts/getAllPosts/json";
    private static final String GET_POST_BY_ID_SERVICES_CALL = Constants.NEW_BACKEND+"posts/postId/";
    private static final String GET_POST_BY_USER_SERVICES_CALL = Constants.NEW_BACKEND+"posts/userId/";
    private static final String ADD_POST_SERVICE_CALL = Constants.NEW_BACKEND+"posts/add";

    private String userId;
    private String content;
    private String time;
    private String visibility;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }


    public static Post find(String id) {
        Post post = new Post();
        post.setUserId(id);
        return post;
    }

    /*
        List all posts
     */
    public static ArrayList<Post> getAllPosts() {
        ArrayList<Post> posts = new ArrayList<Post>();
        JsonNode postsNode = APICall.callAPI(GET_POST_SERVICES_CALL);

        if (postsNode == null || postsNode.has("error") || !postsNode.isArray()) {
            return posts;
        }

        for (int i = 0; i < postsNode.size(); i++) {
            JsonNode json = postsNode.path(i);
            Post newPost = new Post();
            newPost.setUserId(json.path("userId").asText());
            newPost.setContent(json.path("content").asText());
            newPost.setTime(json.path("time").asText());
            newPost.setVisibility(json.path("visibility").asText());

            posts.add(newPost);
        }
        return posts;
    }

    /*
        add new post
     */
    public static JsonNode addPost(JsonNode jsonData) {
        return APICall.postAPI(ADD_POST_SERVICE_CALL, jsonData);
    }


}
