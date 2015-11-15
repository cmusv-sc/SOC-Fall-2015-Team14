package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import models.*;
import models.Post;
import models.PostRepository;
import play.mvc.Controller;
import play.mvc.Result;
import util.Common;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.PersistenceException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dachengwen on 11/3/15.
 */
@Named
@Singleton
public class PostController extends Controller {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // We are using constructor injection to receive a repository to support our
    // desire for immutability.
    @Inject
    public PostController(final PostRepository postRepository,
                          final UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Result addPost() {
        JsonNode json = request().body().asJson();
        if (json == null) {
            System.out.println("Post not created, expecting Json data");
            return badRequest("Post not created, expecting Json data");
        }

        // Parse JSON file
        long userId = json.path("userId").asLong();
        String title = json.path("title").asText();
        String content = json.path("content").asText();
        String visibility = json.path("visibility").asText();

        User user = userRepository.findOne(userId);
        if (user == null) {
            System.out.println("User not found with with id: " + userId);
            return notFound("User not found with with id: " + userId);
        }

        Date time = new Date();
        SimpleDateFormat format = new SimpleDateFormat(Common.DATE_PATTERN);
        try {
            time = format.parse(json.findPath("time").asText());
        } catch (ParseException e) {
            System.out.println("No creation date specified, set to current time");
        }

        try {
            Post post = new Post(user, title, content, time, visibility);
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

    public Result deletePostsByUser(Long id) {
        User user = userRepository.findOne(id);
        if (user == null) {
            System.out.println("User not found with with id: " + id);
            return notFound("User not found with with id: " + id);
        }

        for (Post post : postRepository.findByUserOrderByTimeDesc(user)) {
            postRepository.delete(post);
        }

        System.out.println("Posts are deleted for User: " + id);
        return ok("Posts are deleted for User: " + id);
    }

    public Result updatePost(Long id) {
        JsonNode json = request().body().asJson();
        if (json == null) {
            System.out.println("Post not saved, expecting Json data");
            return badRequest("Post not saved, expecting Json data");
        }

        // Parse JSON file
        String title = json.path("title").asText();
        String content = json.path("content").asText();
        String visibility = json.path("visibility").asText();

        Date time = new Date();
        SimpleDateFormat format = new SimpleDateFormat(Common.DATE_PATTERN);
        try {
            time = format.parse(json.findPath("time").asText());
        } catch (ParseException e) {
            System.out.println("No creation date specified, set to current time");
        }

        try {
            Post updatePost = postRepository.findOne(id);

            updatePost.setTitle(title);
            updatePost.setContent(content);
            updatePost.setTime(time);
            updatePost.setVisibility(visibility);

            Post savedPost = postRepository.save(updatePost);
            System.out.println("Post updated: " + savedPost.getId()
                    + " " + savedPost.getContent());
            return ok("Post updated: " + savedPost.getId() + " "
                    + savedPost.getContent());
        } catch (PersistenceException pe) {
            pe.printStackTrace();
            System.out.println("Post not updated: " + id + " "
                    + title);
            return badRequest("Post not updated: " + id + " " + title);
        }
    }

    public Result updatePostVisibility(Long id) {
        JsonNode json = request().body().asJson();
        if (json == null) {
            System.out.println("Post not saved, expecting Json data");
            return badRequest("Post not saved, expecting Json data");
        }

        // Parse JSON file
        String visibility = json.path("visibility").asText();

        try {
            Post updatePost = postRepository.findOne(id);

            updatePost.setVisibility(visibility);

            Post savedPost = postRepository.save(updatePost);
            System.out.println("Post visibility updated: " + savedPost.getId());
            return ok("Post visibility updated: " + savedPost.getId());
        } catch (PersistenceException pe) {
            pe.printStackTrace();
            System.out.println("Post visibility not updated: " + id);
            return badRequest("Post visibility not updated: " + id);
        }
    }

    public Result addSharedUser(Long id) {
        if (id == null) {
            System.out.println("Post id is null or empty!");
            return badRequest("Post id is null or empty!");
        }

        Post post = postRepository.findOne(id);
        if (post == null) {
            System.out.println("Post not found with with id: " + id);
            return notFound("Post not found with with id: " + id);
        }

        JsonNode json = request().body().asJson();
        if (json == null) {
            System.out.println("Post not saved, expecting Json data");
            return badRequest("Post not saved, expecting Json data");
        }

        long userId = json.path("userId").asLong();

        User user = userRepository.findOne(userId);
        if (user == null) {
            System.out.println("User not found with with id: " + id);
            return notFound("User not found with with id: " + id);
        }

        try {
            post.addSharedUsers(user);
            Post savedPost = postRepository.save(post);
            System.out.println("Post updated: " + savedPost.getId());
            return ok("Post updated: " + savedPost.getId());
        } catch (PersistenceException pe) {
            pe.printStackTrace();
            System.out.println("Post not updated: " + id);
            return badRequest("Post not updated: " + id);
        }
    }

    public Result addPostLike(Long id) {
        if (id == null) {
            System.out.println("Post id is null or empty!");
            return badRequest("Post id is null or empty!");
        }

        Post post = postRepository.findOne(id);
        if (post == null) {
            System.out.println("Post not found with with id: " + id);
            return notFound("Post not found with with id: " + id);
        }

        post.addOneToLikeCount();

        try {
            Post savedPost = postRepository.save(post);
            System.out.println("Post updated: " + savedPost.getId());
            return ok("Post updated: " + savedPost.getId());
        } catch (PersistenceException pe) {
            pe.printStackTrace();
            System.out.println("Post not updated: " + id);
            return badRequest("Post not updated: " + id);
        }
    }

    public Result getPost(Long id, String format) {
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
        if (format.equals("json")) {
            result = new Gson().toJson(post);
        }

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

    public Result getAllPostsByUser(Long id, String format) {
        User user = userRepository.findOne(id);
        if (user == null) {
            System.out.println("User not found with with id: " + id);
            return notFound("User not found with with id: " + id);
        }

        List<Post> posts = postRepository.findByUserOrderByTimeDesc(user);

        String result = new String();
        if (format.equals("json")) {
            result = new Gson().toJson(posts);
        }
        return ok(result);
    }
}
