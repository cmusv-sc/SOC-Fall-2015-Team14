package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import models.User;
import play.api.libs.json.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import util.APICall;
import util.Constants;

/**
 * @author: Xunrong Li
 * @andrewID: xunrongl
 * Created on 11/19/15.
 */
public class UserController extends Controller{
    private static User user = User.getUserById(session().get("userId"));

    public static void updateUser() {

    }

    public static Result uploadImage() {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        System.out.println(body.toString());
        Http.MultipartFormData.FilePart image = body.getFile("photo");
        System.out.println();
        if (image != null) {
            System.out.println("get image!");
            JsonNode response = APICall.postAPIwithFile(
                    Constants.NEW_BACKEND + Constants.UPLOAD_IMAGE + user.getId(), image.getFile());
            return ok(response);

        } else {
            flash("error", "Missing image");
            return redirect(routes.MainController.main());
        }

    }
}
