package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import play.api.libs.json.Json;
import scala.collection.immutable.Map;
import scala.util.parsing.json.JSONArray;
import scala.util.parsing.json.JSONObject;
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

    private String postId;
    private String userId;
    private String userName;
    private String userPhotoType;
    private String content;
    private String time;
    private int likeCount;
    private int shareCount;
    private int commentCount;

    private Boolean visibility = false;
    private ArrayList<User> likeUsers;
    private ArrayList<User> shareUsers;
    private ArrayList<Comment> comments;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

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

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public ArrayList<User> getLikeUsers() {
        return likeUsers;
    }

    public void setLikeUsers(ArrayList<User> likeUsers) {
        this.likeUsers = likeUsers;
    }

    public ArrayList<User> getShareUsers() {
        return shareUsers;
    }

    public void setShareUsers(ArrayList<User> shareUsers) {
        this.shareUsers = shareUsers;
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

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
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
            newPost.setPostId(json.path("id").asText());
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
        JsonNode postNodes = APICall.callAPI(GET_POST_BY_USER_SERVICES_CALL + id);

        if (postNodes == null || postNodes.has("error") || !postNodes.isArray()) {
            return posts;
        }

        System.out.println("get user posts");

        for (int i = 0; i < postNodes.size(); i++) {
            JsonNode json = postNodes.path(i);

            //Post newPost = Json.fromJson(json, Post.class);


            Post newPost = new Post();
            JsonNode userJson = json.path("user");

            newPost.setUserName(userJson.path("userName").asText());
            newPost.setUserId(userJson.path("id").asText());

            newPost.setUserPhotoType(userJson.path("photoContentType").asText());
            newPost.setContent(json.path("content").asText());
            newPost.setTime(json.path("time").asText());
            newPost.setCommentCount(Integer.parseInt(json.path("commentCount").asText()));
            newPost.setLikeCount(Integer.parseInt(json.path("likeCount").asText()));
            newPost.setShareCount(Integer.parseInt(json.path("shareCount").asText()));

            //set comment list
            JsonNode commentNodes = json.path("comments");
            for (int j = 0; j < commentNodes.size(); j++) {
                JsonNode commentJson = commentNodes.path(j);

            }


            //set share user list

            //set like user list

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
