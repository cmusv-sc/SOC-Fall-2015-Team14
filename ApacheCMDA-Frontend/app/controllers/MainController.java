package controllers;

import models.User;
import play.mvc.Result;
import play.mvc.Controller;

import views.html.sns.home;
import views.html.sns.main;

/**
 * @author: Xunrong Li
 * @andrewID: xunrongl
 * Created on 11/19/15.
 */
public class MainController extends Controller {

    private static User user;
    public static Result home() {

        if (user == null) {
            user = User.getUserByUserName(session().get("userName"));
            System.out.println(user.toString());
            session("userId", String.valueOf(user.getId()));
        } else {
            System.out.println("exisited" + user.toString());
        }

        return ok(home.render(user));
    }

    public static Result main() {

        if (user == null) {
            user = User.getUserByUserName(session().get("userName"));
            session("userId", String.valueOf(user.getId()));
        }

        return ok(main.render(user));
    }

}
