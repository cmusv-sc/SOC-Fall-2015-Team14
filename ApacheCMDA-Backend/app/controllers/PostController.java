package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.*;
import models.Post;
import models.PostRepository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import util.Common;
import util.CustomExclusionStrategy;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.PersistenceException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dachengwen on 11/3/15.
 */
@Named
@Singleton
@Transactional
@EnableTransactionManagement
public class PostController extends Controller {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    // We are using constructor injection to receive a repository to support our
    // desire for immutability.
    @Inject
    public PostController(final PostRepository postRepository,
                          UserRepository userRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
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
        boolean visibility = json.path("visibility").asBoolean();
        double latitude = json.path("latitude").asDouble();
        double longitude = json.path("longitude").asDouble();

        User user = userRepository.findOne(userId);
        if (user == null) {
            System.out.println("User not found with with id: " + userId);
            return notFound("User not found with with id: " + userId);
        }

        Date time = new Date();
        /*SimpleDateFormat format = new SimpleDateFormat(Common.DATE_PATTERN);
        try {
            time = format.parse(json.findPath("time").asText());
        } catch (ParseException e) {
            System.out.println("No creation date specified, set to current time");
        }*/

        try {
            Post post = new Post(user, title, content, time, visibility, latitude, longitude);

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

        deletePost.setUser(null);
        postRepository.save(deletePost);
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
            post.setUser(null);
            postRepository.save(post);
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
        boolean visibility = json.path("visibility").asBoolean();
        double latitude = json.path("latitude").asDouble();
        double longitude = json.path("longitude").asDouble();

        Date time = new Date();
        /*SimpleDateFormat format = new SimpleDateFormat(Common.DATE_PATTERN);
        try {
            time = format.parse(json.findPath("time").asText());
        } catch (ParseException e) {
            System.out.println("No creation date specified, set to current time");
        }*/

        try {
            Post updatePost = postRepository.findOne(id);

            updatePost.setTitle(title);
            updatePost.setContent(content);
            updatePost.setTime(time);
            updatePost.setVisibility(visibility);
            updatePost.setLatitude(latitude);
            updatePost.setLongitude(longitude);

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
        boolean visibility = json.path("visibility").asBoolean();

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

    public Result addSharedUser() {
        JsonNode json = request().body().asJson();
        if (json == null) {
            System.out.println("Post not saved, expecting Json data");
            return badRequest("Post not saved, expecting Json data");
        }

        long id = json.path("postId").asLong();
        long userId = json.path("userId").asLong();

        Post post = postRepository.findOne(id);
        if (post == null) {
            System.out.println("Post not found with with id: " + id);
            return notFound("Post not found with with id: " + id);
        }

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

    public Result addPostLike() {
        JsonNode json = request().body().asJson();
        if (json == null) {
            System.out.println("Post not saved, expecting Json data");
            return badRequest("Post not saved, expecting Json data");
        }

        long postId = json.path("postId").asLong();
        long userId = json.path("userId").asLong();

        Post post = postRepository.findOne(postId);
        if (post == null) {
            System.out.println("Post not found with with id: " + postId);
            return notFound("Post not found with with id: " + postId);
        }

        User user = userRepository.findOne(userId);
        if (user == null) {
            System.out.println("User not found with with id: " + userId);
            return notFound("User not found with with id: " + userId);
        }

        post.addLikeUsers(user);

        try {
            Post savedPost = postRepository.save(post);
            System.out.println("Post updated: " + savedPost.getId());
            return ok("Post updated: " + savedPost.getId());
        } catch (PersistenceException pe) {
            pe.printStackTrace();
            System.out.println("Post not updated: " + postId);
            return badRequest("Post not updated: " + postId);
        }
    }


    public List<Comment> getCommentsByPost(Long id) {
        List<Comment> comments = null;

        Post post = postRepository.findOne(id);
        if (post == null) {
            System.out.println("Post not found with with id: " + post);
        } else {
            comments = commentRepository.findByPostOrderByTimeDesc(post);
        }
        return comments;
    }

    public Result getPost(Long id, String format) {
        if (id == null) {
            System.out.println("Post id is null or empty!");
            return badRequest("Post id is null or empty!");
        }

        Post post = postRepository.findOne(id);

        List<Comment> comments = post.getComments();
        Collections.sort(comments);
        post.setComments(comments);

        if (post == null) {
            System.out.println("Post not found with with id: " + id);
            return notFound("Post not found with with id: " + id);
        }



        String result = new String();
        if (format.equals("json")) {
            //result = new Gson().toJson(post);
            Gson gson = new GsonBuilder().serializeNulls()
                    .setExclusionStrategies(new CustomExclusionStrategy(Post.class))
                    .excludeFieldsWithoutExposeAnnotation().create();

            result = gson.toJson(post);
        }

        return ok(result);
    }

    public Result getAllPosts(String format) {
        Iterable<Post> postIterable = postRepository.findAll();
        List<Post> postList = new ArrayList<Post>();
        for (Post post : postIterable) {
            List<Comment> comments = post.getComments();
            Collections.sort(comments);
            post.setComments(comments);

            postList.add(post);
        }

        String result = new String();



        if (format.equals("json")) {
            //result = new Gson().toJson(postList);
            Gson gson = new GsonBuilder().serializeNulls()
                    .setExclusionStrategies(new CustomExclusionStrategy(Post.class))
                    .excludeFieldsWithoutExposeAnnotation().create();

            result = gson.toJson(postList);
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

        for(Post post: posts) {
            List<Comment> comments = post.getComments();
            Collections.sort(comments);
            post.setComments(comments);
        }


        String result = new String();
        if (format.equals("json")) {
            Gson gson = new GsonBuilder().serializeNulls()
                    .setExclusionStrategies(new CustomExclusionStrategy(Post.class))
                    .excludeFieldsWithoutExposeAnnotation().create();

            result = gson.toJson(posts);
        }
        return ok(result);
    }

    public Result getSharedUser(Long postId, String format) {
        Post post = postRepository.findOne(postId);
        if (post == null) {
            System.out.println("Post not found with with id: " + postId);
            return notFound("Post not found with with id: " + postId);
        }

        Set<User> users = post.getSharedUsers();

        String result = new String();
        if (format.equals("json")) {
            Gson gson = new GsonBuilder().serializeNulls()
                    .setExclusionStrategies(new CustomExclusionStrategy(User.class))
                    .excludeFieldsWithoutExposeAnnotation().create();

            result = gson.toJson(new ArrayList<User>(users));
        }
        return ok(result);
    }

    public Result getSharedPosts(Long userId, String format) {
        User user = userRepository.findOne(userId);
        if (user == null) {
            System.out.println("User not found with with id: " + userId);
            return notFound("User not found with with id: " + userId);
        }

        List<Post> posts = postRepository.findBySharedUsersOrderByTimeDesc(user);

        for(Post post: posts) {
            List<Comment> comments = post.getComments();
            Collections.sort(comments);
            post.setComments(comments);
        }


        String result = new String();
        if (format.equals("json")) {
            Gson gson = new GsonBuilder().serializeNulls()
                    .setExclusionStrategies(new CustomExclusionStrategy(Post.class))
                    .excludeFieldsWithoutExposeAnnotation().create();

            result = gson.toJson(posts);
        }
        return ok(result);
    }

    public Result getLikeUsers(Long id, String format) {
        if (id == null) {
            System.out.println("Post id is null or empty!");
            return badRequest("Post id is null or empty!");
        }

        Post post = postRepository.findOne(id);
        if (post == null) {
            System.out.println("Post not found with with id: " + id);
            return notFound("Post not found with with id: " + id);
        }

        Set<User> users = post.getLikeUsers();

        String result = new String();
        if (format.equals("json")) {
            Gson gson = new GsonBuilder().serializeNulls()
                    .setExclusionStrategies(new CustomExclusionStrategy(User.class))
                    .excludeFieldsWithoutExposeAnnotation().create();

            result = gson.toJson(new ArrayList<User>(users));
        }
        return ok(result);
    }

    public Result getMostPopularPosts(String format) {
        List<Post> posts = postRepository.findTop10ByOrderByLikeCountDesc();

        for(Post post: posts) {
            List<Comment> comments = post.getComments();
            Collections.sort(comments);
            post.setComments(comments);
        }


        String result = new String();
        if (format.equals("json")) {
            Gson gson = new GsonBuilder().serializeNulls()
                    .setExclusionStrategies(new CustomExclusionStrategy(Post.class))
                    .excludeFieldsWithoutExposeAnnotation().create();

            result = gson.toJson(posts);
        }
        return ok(result);
    }

    public Result searchPosts(String key, String format) {
        List<Post> posts = postRepository.findByTitleContainingOrContentContaining(key, key);

        for(Post post: posts) {
            List<Comment> comments = post.getComments();
            Collections.sort(comments);
            post.setComments(comments);
        }


        String result = new String();
        if (format.equals("json")) {
            Gson gson = new GsonBuilder().serializeNulls()
                    .setExclusionStrategies(new CustomExclusionStrategy(Post.class))
                    .excludeFieldsWithoutExposeAnnotation().create();

            result = gson.toJson(posts);
        }
        return ok(result);
    }
}
