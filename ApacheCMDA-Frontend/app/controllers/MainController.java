package controllers;

import com.amazonaws.util.json.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Post;
import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Controller;

import util.APICall;
import util.Constants;
import views.html.sns.home;
import views.html.sns.main;
import views.html.sns.other;
import views.html.sns.search;
import views.html.sns.topPosts;

import java.util.ArrayList;

/**
 * @author: Xunrong Li
 * @andrewID: xunrongl
 * Created on 11/19/15.
 */
public class MainController extends Controller {

    private static User user;
    final static Form<Post> postForm = Form.form(Post.class);
    private static ArrayList<Post> posts = new ArrayList<>();
    private static ArrayList<User> followingUsers = new ArrayList<>();
    private static ArrayList<User> followedUsers = new ArrayList<>();

    public static Result home() {
        if(session().get("userName") == null) {
            return redirect("/sns/login");
        }

        user = User.getUserByUserName(session().get("userName"));
        session("userId", String.valueOf(user.getId()));
        posts = Post.getUserPosts(String.valueOf(user.getId()));
        ArrayList<Post> sharedPosts = Post.getSharedPosts(String.valueOf(user.getId()));
        posts.addAll(sharedPosts);

        followingUsers = User.getFollowingUsers(String.valueOf(user.getId()));
        followedUsers = User.getFollowedUsers(String.valueOf(user.getId()));

        return ok(home.render(user, postForm, posts, followingUsers, followedUsers));

    }

    public static Result main() {
        if(session().get("userName") == null) {
            return redirect("/sns/login");
        }
        user = User.getUserByUserName(session().get("userName"));
        session("userId", String.valueOf(user.getId()));
        posts = Post.getFollowingPosts(String.valueOf(user.getId()));

        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).getVisibility()) {
                //if it is private
                posts.remove(i);
            }
        }

        return ok(main.render(user, postForm, posts));
    }

    public static Result other(String userId) {
        User otherUser = User.getUserById(userId);
        ArrayList<Post> otherPosts = Post.getUserPosts(userId);
        ArrayList<User> otherFollowingUser = User.getFollowingUsers(userId);
        ArrayList<User> otherFollowedUser = User.getFollowedUsers(userId);

        return ok(other.render(otherUser, postForm, otherPosts, otherFollowingUser, otherFollowedUser));
    }

    public static Result fuzzySearch() {

        DynamicForm dynamicForm = Form.form().bindFromRequest();
        String keyword = dynamicForm.get("fuzzy-search");

        JsonNode userNodes = APICall.callAPI(Constants.NEW_BACKEND + Constants.FUZZY_SEARCH + keyword);
        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < userNodes.size(); i++) {
            JsonNode json = userNodes.path(i);
            User newUser = Json.fromJson(json, User.class);
            users.add(newUser);
        }

        return ok(search.render(users));
    }

    public static Result advancedSearch() {

        DynamicForm df = Form.form().bindFromRequest();

        ObjectNode jsonData = Json.newObject();
        jsonData.put("firstName", df.get("firstName-search"));
        jsonData.put("lastName", df.get("lastName-search"));
        jsonData.put("affiliation", df.get("affiliation-search"));
        jsonData.put("email", df.get("email-search"));

        JsonNode userNodes = APICall.postAPI(Constants.NEW_BACKEND + Constants.EXACT_SEARCH, jsonData);

        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < userNodes.size(); i++) {
            JsonNode json = userNodes.path(i);
            User newUser = Json.fromJson(json, User.class);
            users.add(newUser);
        }

        return ok(search.render(users));
    }

    public static Result popularPosts() {

        ArrayList<Post> topTenPosts = Post.getTopTenPosts();
        return ok(topPosts.render(topTenPosts));
    }

}
