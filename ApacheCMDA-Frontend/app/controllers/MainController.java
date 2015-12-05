package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Post;
import models.User;
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

        if (user == null || user.getId() == 0) {
            System.out.println("user is null");
            System.out.println("user is null userName " + session().get("userName"));
            user = User.getUserByUserName(session().get("userName"));
            System.out.println(user.toString());
            session("userId", String.valueOf(user.getId()));
        } else {
            //update user
            System.out.println("userId " + user.getId());
            user = User.getUserById(String.valueOf(user.getId()));
        }

        posts = Post.getUserPosts(String.valueOf(user.getId()));
//        ArrayList<Post> sharedPosts = Post.getSharedPosts(String.valueOf(user.getId()));
//        System.out.println("share" + sharedPosts.toString());
//        posts.addAll(sharedPosts);

        //followingUsers = User.getFollowingUsers(String.valueOf(user.getId()));
        //followedUsers = User.getFollowedUsers(String.valueOf(user.getId()));

        return ok(home.render(user, postForm, posts, followingUsers, followedUsers));

    }

    public static Result main() {

        if (user == null || user.getId() == 0) {
            System.out.println("user is null");
            user = User.getUserByUserName(session().get("userName"));
            session("userId", String.valueOf(user.getId()));
        } else {
            //update user
            System.out.println(user.toString());
            System.out.println("main is " + user.getId());
            user = User.getUserById(String.valueOf(user.getId()));
        }
        posts = Post.getAllPosts();

        return ok(main.render(user, postForm, posts));
    }

    public static Result other(String userId) {
        User otherUser = User.getUserById(userId);
        ArrayList<Post> otherPosts = Post.getUserPosts(userId);
        ArrayList<User> otherFollowingUser = User.getFollowingUsers(userId);
        ArrayList<User> otherFollowedUser = User.getFollowedUsers(userId);

        return ok(other.render(otherUser, postForm, otherPosts, otherFollowingUser, otherFollowedUser));
    }

    public static Result search() {

        Http.RequestBody body = request().body();
        JsonNode jsonData = body.asJson();
        String keyword = jsonData.path("srch-term").asText();

        JsonNode userNodes = APICall.callAPI(Constants.NEW_BACKEND + Constants.FUZZY_SEARCH + keyword);
        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < userNodes.size(); i++) {
            JsonNode json = userNodes.path(i);
            User newUser = Json.fromJson(json, User.class);
            users.add(newUser);
        }

        return ok(search.render(postForm, users));
    }

}
