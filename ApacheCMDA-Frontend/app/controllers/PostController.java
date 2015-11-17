package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.metadata.Post;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

//import views.sns.*;
//import views.html.*;
import views.html.sns.main;
import views.html.sns.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author: Xunrong Li
 * @andrewID: xunrongl
 * Created on 11/5/15.
 */
public class PostController extends Controller {
    final static Form<Post> postForm = Form.form(Post.class);

    public static Result getAllPosts() {
        return ok(posts.render(Post.getAllPosts(), postForm));
    }

    public static Result addPost() {
        return ok(addPost.render(postForm));
    }

    public static Result home() {
        return ok(home.render());
    }

    public static Result main() {
        return ok(main.render());
    }

    public static Result newPost() {
        Form<Post> dc = postForm.bindFromRequest();
        ObjectNode jsonData = Json.newObject();
        jsonData.put("userId", 1);
        jsonData.put("content", dc.field("content").value());
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        jsonData.put("time", dateFormat.format(date));
        jsonData.put("visibility", "public");
        JsonNode response = Post.addPost(jsonData);
        Application.flashMsg(response);
        return redirect("/sns/home");
    }

}
