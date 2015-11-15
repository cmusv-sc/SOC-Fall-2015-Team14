package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.metadata.PostService;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

//import views.sns.*;
//import views.html.*;
import views.html.sns.*;


/**
 * @author: Xunrong Li
 * @andrewID: xunrongl
 * Created on 11/5/15.
 */
public class PostController extends Controller {
    final static Form<PostService> postServiceForm = Form.form(PostService.class);

    public static Result getAllPosts() {
        return ok(posts.render(PostService.getAllPosts(), postServiceForm));
    }

    public static Result addPost() {
        return ok(addPost.render(postServiceForm));
    }

    public static Result newPost() {
        Form<PostService> dc = postServiceForm.bindFromRequest();
        ObjectNode jsonData = Json.newObject();
        jsonData.put("userId", 1);
        jsonData.put("content", dc.field("content").value());
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        jsonData.put("time", dateFormat.format(date));
        jsonData.put("visibility", "public");
        JsonNode response = PostService.addPost(jsonData);
        Application.flashMsg(response);
        return redirect("/sns/home");
    }

}
