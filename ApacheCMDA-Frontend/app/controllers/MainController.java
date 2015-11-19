package controllers;

import models.User;
import play.mvc.Result;
import play.mvc.Controller;
import views.html.sns.*;

/**
 * @author: Xunrong Li
 * @andrewID: xunrongl
 * Created on 11/19/15.
 */
public class MainController extends Controller {

    public static Result home() {
        return ok(home.render(
                User.getUserById(session().get("userId"))
        ));
    }

    public static Result main() {
        return ok(main.render(
                User.getUserById(session().get("userId"))
        ));
    }

}
