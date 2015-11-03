package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import models.Post;
import models.PostRepository;
import models.Post;
import models.PostRepository;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dachengwen on 11/3/15.
 */
@Named
@Singleton
public class PostController extends Controller {

    private final PostRepository postRepository;

    // We are using constructor injection to receive a repository to support our
    // desire for immutability.
    @Inject
    public PostController(final PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Result addPost() {
        JsonNode json = request().body().asJson();
        if (json == null) {
            System.out.println("Post not created, expecting Json data");
            return badRequest("Post not created, expecting Json data");
        }

        // Parse JSON file
        String userId = json.path("userId").asText();
        String title = json.path("title").asText();
        String content = json.path("content").asText();
        String time = json.path("time").asText();
        String visibility = json.path("visibility").asText();

        try {
            Post post = new Post(userId, title, content, time, visibility);
            postRepository.save(post);
            System.out.println("Post saved: " + post.getId());
            return created(new Gson().toJson(post.getId()));
        } catch (PersistenceException pe) {
            pe.printStackTrace();
            System.out.println("Post not saved: " + userId + " " + title + " " + content);
            return badRequest("Post not saved: " + userId + " " + title + " " + content);
        }
    }

    public Result deletePost(Long id) {
        Post deletePost = postRepository.findOne(id);
        if (deletePost == null) {
            System.out.println("Post not found with id: " + id);
            return notFound("Post not found with id: " + id);
        }

        postRepository.delete(deletePost);
        System.out.println("Post is deleted: " + id);
        return ok("Post is deleted: " + id);
    }

    public Result updatePost(long id) {
        JsonNode json = request().body().asJson();
        if (json == null) {
            System.out.println("Post not saved, expecting Json data");
            return badRequest("Post not saved, expecting Json data");
        }

        // Parse JSON file
        String userId = json.path("userId").asText();
        String title = json.path("title").asText();
        String content = json.path("content").asText();
        String time = json.path("time").asText();
        String visibility = json.path("visibility").asText();

        try {
            Post updatePost = postRepository.findOne(id);

            updatePost.setTitle(title);
            updatePost.setContent(content);
            updatePost.setTime(time);
            updatePost.setVisibility(visibility);

            Post savedPost = postRepository.save(updatePost);
            System.out.println("Post updated: " + savedPost.getId()
                    + " " + savedPost.getContent());
            return created("Post updated: " + savedPost.getId() + " "
                    + savedPost.getContent());
        } catch (PersistenceException pe) {
            pe.printStackTrace();
            System.out.println("Post not updated: " + userId + " "
                    + title);
            return badRequest("Post not updated: " + userId + " " + title);
        }
    }

    public Result getPost(Long id) {
        if (id == null) {
            System.out.println("Post id is null or empty!");
            return badRequest("Post id is null or empty!");
        }

        Post post = postRepository.findOne(id);

        if (post == null) {
            System.out.println("Post not found with with id: " + id);
            return notFound("Post not found with with id: " + id);
        }
        String result = new String();
        result = new Gson().toJson(post);

        return ok(result);
    }

    public Result getAllPosts(String format) {
        Iterable<Post> postIterable = postRepository.findAll();
        List<Post> postList = new ArrayList<Post>();
        for (Post post : postIterable) {
            postList.add(post);
        }
        String result = new String();
        if (format.equals("json")) {
            result = new Gson().toJson(postList);
        }
        return ok(result);
    }

    public Result getAllPostsByUserId(String userId) {

        List<Post> posts = postRepository.findByUserId(userId);

        String result = new String();
        result = new Gson().toJson(posts);
        return ok(result);
    }

}
