package controllers;

import models.Post;
import models.User;
import play.data.Form;
import play.mvc.Result;
import play.mvc.Controller;

import views.html.sns.home;
import views.html.sns.main;

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

        if (user == null) {
            System.out.println("userName " + session().get("userName"));
            user = User.getUserByUserName(session().get("userName"));
            System.out.println(user.toString());
            session("userId", String.valueOf(user.getId()));
        } else {
            //update user
            System.out.println("userId " + session().get("userId"));
            user = user.getUserById(session("userId"));
        }

        posts = Post.getUserPosts(String.valueOf(user.getId()));
        followingUsers = User.getFollowingUsers(String.valueOf(user.getId()));
        followedUsers = User.getFollowedUsers(String.valueOf(user.getId()));

        return ok(home.render(user, postForm, posts, followingUsers, followedUsers));

    }

    public static Result main() {

        if (user == null) {
            user = User.getUserByUserName(session().get("userName"));
            session("userId", String.valueOf(user.getId()));
        } else {
            //update user
            user = user.getUserById(session("userId"));
        }
        posts = Post.getAllPosts();

        return ok(main.render(user, postForm, posts));
    }

}
