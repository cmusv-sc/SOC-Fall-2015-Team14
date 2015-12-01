package models;

import com.fasterxml.jackson.databind.JsonNode;
import util.APICall;
import util.Constants;

import java.util.ArrayList;

/**
 * @author: Xunrong Li
 * @andrewID: xunrongl
 * Created on 11/5/15.
 */
public class Post {

    //restful uris
    private static final String GET_POST_SERVICES_CALL = Constants.NEW_BACKEND+"/posts/getAllPosts/json";
    private static final String GET_POST_BY_ID_SERVICES_CALL = Constants.NEW_BACKEND+"/posts/postId/";
    private static final String GET_POST_BY_USER_SERVICES_CALL = Constants.NEW_BACKEND+"/posts/userId/";
    private static final String ADD_POST_SERVICE_CALL = Constants.NEW_BACKEND+"/posts/add";

    private String userId;
    private String userName;
    private String userPhotoType;
    private String content;
    private String time;
    private Boolean visibility = false;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

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

    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

    public String getUserPhotoType() {
        return userPhotoType;
    }

    public void setUserPhotoType(String userPhotoType) {
        this.userPhotoType = userPhotoType;
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
            JsonNode userJson = json.path("user");
            newPost.setUserName(userJson.path("userName").asText());
            newPost.setUserId(userJson.path("id").asText());
            System.out.println("type" + userJson.path("photoContentType").asText());
            newPost.setUserPhotoType(userJson.path("photoContentType").asText());
            newPost.setContent(json.path("content").asText());
            newPost.setTime(json.path("time").asText());
            if (json.path("visibility").asText().equals("true")) {
                newPost.setVisibility(true);
            } else {
                newPost.setVisibility(false);
            }
            
            posts.add(newPost);
        }
        return posts;
    }

    /*
        get user posts
     */

    public static ArrayList<Post> getUserPosts(String id) {
        ArrayList<Post> posts = new ArrayList<Post>();
        JsonNode postsNode = APICall.callAPI(GET_POST_BY_USER_SERVICES_CALL + id);

        if (postsNode == null || postsNode.has("error") || !postsNode.isArray()) {
            return posts;
        }

        System.out.println("get user posts");

        for (int i = 0; i < postsNode.size(); i++) {
            JsonNode json = postsNode.path(i);
            System.out.println(json.toString());
            Post newPost = new Post();
            JsonNode userJson = json.path("user");
            newPost.setUserName(userJson.path("userName").asText());
            newPost.setUserId(userJson.path("id").asText());
            System.out.println("type" + userJson.path("photoContentType").asText());
            newPost.setUserPhotoType(userJson.path("photoContentType").asText());
            newPost.setContent(json.path("content").asText());
            newPost.setTime(json.path("time").asText());
            if (json.path("visibility").asText().equals("true")) {
                newPost.setVisibility(true);
            } else {
                newPost.setVisibility(false);
            }

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
