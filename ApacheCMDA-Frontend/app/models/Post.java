package models;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
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
    private static final String GET_POST_BY_ID_SERVICES_CALL = Constants.NEW_BACKEND+"/posts/id/";
    private static final String GET_POST_BY_USER_SERVICES_CALL = Constants.NEW_BACKEND+"/posts/userId/";
    private static final String ADD_POST_SERVICE_CALL = Constants.NEW_BACKEND+"/posts/add";

    private String id;
    private User user;
    private String title;
    private String content;
    private String time;
    private int likeCount;
    private int shareCount;
    private int commentCount;

    private Boolean visibility;
    private ArrayList<User> likeUsers;
    private ArrayList<User> sharedUsers;
    private ArrayList<Comment> comments;

    private double latitude;
    private double longitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public ArrayList<User> getSharedUsers() {
        return sharedUsers;
    }

    public void setSharedUsers(ArrayList<User> sharedUsers) {
        this.sharedUsers = sharedUsers;
    }

    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public static Post find(String id) {
        Post post = new Post();
        post.getUser().setId(Integer.parseInt(id));
        return post;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

            Post newPost = Json.fromJson(json, Post.class);
            posts.add(newPost);
        }
        return posts;
    }

    /*
        get following posts
     */
    public static ArrayList<Post> getFollowingPosts(String userId) {
        ArrayList<Post> posts = new ArrayList<Post>();
        JsonNode postsNode = APICall.callAPI(Constants.NEW_BACKEND + Constants.GET_FOLLOWING_POSTS + userId);

        if (postsNode == null || postsNode.has("error") || !postsNode.isArray()) {
            return posts;
        }

        for (int i = 0; i < postsNode.size(); i++) {
            JsonNode json = postsNode.path(i);

            Post newPost = Json.fromJson(json, Post.class);
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
            Post newPost = Json.fromJson(json, Post.class);
            posts.add(newPost);

        }
        return posts;
    }

    public static ArrayList<Post> getSharedPosts(String id) {
        ArrayList<Post> posts = new ArrayList<>();
        JsonNode postNodes = APICall.callAPI("http://localhost:9034/posts/getSharedPosts/" + id);

        if (postNodes == null || postNodes.has("error") || !postNodes.isArray()) {
            return posts;
        }

        System.out.println("get shared posts");

        for (int i = 0; i < postNodes.size(); i++) {
            JsonNode json = postNodes.path(i);
            Post newPost = Json.fromJson(json, Post.class);
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

    /*
        get top ten posts
     */

    public static ArrayList<Post> getTopTenPosts() {
        ArrayList<Post> posts = new ArrayList<>();
        JsonNode postNodes = APICall.callAPI(Constants.NEW_BACKEND + Constants.GET_POPULAR_POSTS);

        if (postNodes == null || postNodes.has("error") || !postNodes.isArray()) {
            return posts;
        }

        System.out.println("get popular posts");

        for (int i = 0; i < postNodes.size(); i++) {
            JsonNode json = postNodes.path(i);
            Post newPost = Json.fromJson(json, Post.class);
            posts.add(newPost);
        }

        return posts;
    }
}
