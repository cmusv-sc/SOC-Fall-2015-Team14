package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Comment;
import models.Post;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
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
    final static Form<Comment> commentForm = Form.form(Comment.class);

    public static Result newPost() {

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
        return redirect("/sns/main");
    }

    public static void getComment() {
        //get current post

        //get comment by id

    }

    public static Result newComment() {

        Http.RequestBody body = request().body();
        JsonNode json = body.asJson();

        ObjectNode jsonData = Json.newObject();
        jsonData.put("userId", session().get("userId"));
        //get current post
        jsonData.put("postId", json.path("postId"));
        //get content from json
        jsonData.put("content", json.path("content"));

        System.out.println(jsonData.toString());

        APICall.postAPI(Constants.NEW_BACKEND + Constants.ADD_COMMENT, jsonData);
        return ok();
    }

    public static Result newLike() {

        Http.RequestBody body = request().body();
        JsonNode json = body.asJson();

        ObjectNode jsonData = Json.newObject();
        jsonData.put("postId", json.path("postId"));
        jsonData.put("userId", session().get("userId"));

        APICall.postAPI(Constants.NEW_BACKEND + Constants.ADD_LIKE, jsonData);
        return ok();
    }

    public static Result newShare() {
        Http.RequestBody body = request().body();
        JsonNode json = body.asJson();

        ObjectNode jsonData = Json.newObject();
        jsonData.put("postId", json.path("postId"));
        jsonData.put("userId", session().get("userId"));

        System.out.println("add share");

        APICall.postAPI(Constants.NEW_BACKEND + Constants.ADD_SHARE, jsonData);
        return ok();
    }

    public static Result deletePost(String id) {
        System.out.println("delete");
        APICall.deleteAPI(Constants.NEW_BACKEND + Constants.DELETE_POST + id);
        return redirect("/sns/home");
    }
}
