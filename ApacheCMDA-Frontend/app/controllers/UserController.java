package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.User;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import util.APICall;
import util.Constants;
import views.html.sns.home;
import views.html.sns.main;

/**
 * @author: Xunrong Li
 * @andrewID: xunrongl
 * Created on 11/19/15.
 */
public class UserController extends Controller{
    private static User user = User.getUserById(session().get("userId"));
    final static Form<User> userForm = Form.form(User.class);

    @BodyParser.Of(BodyParser.Json.class)
    public static Result updateUser() {
        Http.RequestBody body = request().body();
        JsonNode json = body.asJson();
        JsonNode response = null;

        try {
            user.setUserName(json.path("userName").asText());
            user.setFirstName(json.path("firstName").asText());
            user.setLastName(json.path("lastName").asText());
            user.setAffiliation(json.path("affiliation").asText());
            user.setEmail(json.path("email").asText());
            user.setPhoneNumber(json.path("phoneNumber").asText());
            user.setResearchInterests(json.path("researchInterests").asText());

            response = APICall.putAPI(Constants.NEW_BACKEND + Constants.USER_API + user.getId(), json);
        } catch (IllegalStateException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("finally" + user.getAffiliation());
            return ok(response);
        }
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
            return redirect(routes.MainController.home());
        }

    }

    public static Result addFollowee(String followeeID) {
        ObjectNode jsonData = Json.newObject();
        jsonData.put("followerID", session().get("userId"));
        jsonData.put("followeeID", followeeID);

        System.out.println(" add followee" + jsonData.toString());
        String response = String.valueOf(APICall.postAPI(Constants.NEW_BACKEND + Constants.ADD_FOLLOWEE, jsonData));
        System.out.println(response);
        return ok();
    }

    public static Result removeFollowee(String followeeID) {
        String response = String.valueOf(APICall.deleteAPI(Constants.NEW_BACKEND + "/users/" + session().get("userId")
                + "/unfollow/" + followeeID));

        System.out.println(response);
        return ok();
    }

    public static Result getAllUsers() {

        JsonNode response = APICall.callAPI(Constants.NEW_BACKEND + "/users/getAllUsers/json");
        return ok(response);
    }

    public static Result addFrontLayer() {
        String id = String.valueOf(user.getId());
        ObjectNode jsonData= Json.newObject();
        jsonData.put("hasFrontLayerPhoto", true);
        JsonNode response = APICall.putAPI(Constants.NEW_BACKEND + "/users/updateFrontLayerPhotoFlag/" + id, jsonData);

        return ok();
    }

    public static Result removeFrontLayer() {
        String id = String.valueOf(user.getId());
        ObjectNode jsonData= Json.newObject();
        jsonData.put("hasFrontLayerPhoto", false);
        JsonNode response = APICall.putAPI(Constants.NEW_BACKEND + "/users/updateFrontLayerPhotoFlag/" + id, jsonData);

        return ok();
    }


}
