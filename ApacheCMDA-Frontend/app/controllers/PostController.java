package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Post;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import util.APICall;
import util.Constants;
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
        return null;
//        return ok(posts.render(Post.getAllPosts(), postForm));
    }

    public static Result newPost() {
        System.out.println("new post");
        Form<Post> dc = postForm.bindFromRequest();
        ObjectNode jsonData = Json.newObject();
        jsonData.put("userId", session().get("userId"));
        System.out.println("content is " + dc.field("content").value());
        jsonData.put("content", dc.field("content").value());
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        jsonData.put("time", dateFormat.format(date));
        //Todo get value from check box
        System.out.println("visibility is " + dc.field("visibility").value());
        jsonData.put("visibility", dc.field("visibility").value());

        JsonNode response = APICall.postAPI(Constants.NEW_BACKEND + Constants.ADD_POST, jsonData);
        Application.flashMsg(response);
        System.out.println(response.toString());
        return redirect("/sns/home");
//        return ok(header.render(dc));
    }

}
