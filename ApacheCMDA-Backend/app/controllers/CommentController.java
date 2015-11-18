package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import models.CommentRepository;
import play.mvc.*;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.PersistenceException;

import models.*;
import util.Common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by tonyfu on 11/15/15.
 */
@Named
@Singleton
public class CommentController extends Controller {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // We are using constructor injection to receive a repository to support our
    // desire for immutability.
    @Inject
    public CommentController(final CommentRepository commentRepository,
                              UserRepository userRepository,
                              PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public Result addComment() {
        JsonNode json = request().body().asJson();
        if (json == null) {
            System.out.println("Comment not created, expecting Json data");
            return badRequest("Comment not created, expecting Json data");
        }

        // Parse JSON file
        long userId = json.path("userId").asLong();
        long postId = json.path("postId").asLong();
        String content = json.path("content").asText();

        Post post = postRepository.findOne(postId);
        if (post == null) {
            System.out.println("Post not found with with id: " + post);
            return notFound("Post not found with with id: " + post);
        }

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
            Comment comment = new Comment(user, post, content, time);
            commentRepository.save(comment);
            System.out.println("Comment saved: " + comment.getId());
            return created(new Gson().toJson(comment.getId()));
        } catch (PersistenceException pe) {
            pe.printStackTrace();
            System.out.println("Comment not saved: " + userId + " " + postId + " " + content);
            return badRequest("Comment not saved: " + userId + " " + postId + " " + content);
        }
    }

    public Result deleteComment(Long id) {
        Comment deleteComment = commentRepository.findOne(id);
        if (deleteComment == null) {
            System.out.println("Comment not found with id: " + id);
            return notFound("Comment not found with id: " + id);
        }

        commentRepository.delete(deleteComment);
        System.out.println("Comment is deleted: " + id);
        return ok("Comment is deleted: " + id);
    }

    public Result getCommentsByPost(Long id, String format) {
        if (id == null) {
            System.out.println("Comment id is null or empty!");
            return badRequest("Comment id is null or empty!");
        }

        Post post = postRepository.findOne(id);
        if (post == null) {
            System.out.println("Post not found with with id: " + post);
            return notFound("Post not found with with id: " + post);
        }

        List<Comment> comments = commentRepository.findByPostOrderByTimeDesc(post);

        if (comments == null && comments.size() == 0) {
            System.out.println("Comment not found with with id: " + id);
            return notFound("Comment not found with with id: " + id);
        }
        String result = new String();
        if (format.equals("json")) {
            result = new Gson().toJson(comments);
        }

        return ok(result);
    }

    public Result deleteCommentsByPost(Long id) {
        if (id == null) {
            System.out.println("Comment id is null or empty!");
            return badRequest("Comment id is null or empty!");
        }

        Post post = postRepository.findOne(id);
        if (post == null) {
            System.out.println("Post not found with with id: " + post);
            return notFound("Post not found with with id: " + post);
        }

        List<Comment> comments = commentRepository.findByPostOrderByTimeDesc(post);

        for (Comment comment : comments) {
            System.out.println("Comment is deleted: " + comment.getId());
            commentRepository.delete(comment);
        }

        System.out.println("Comments are deleted for Post: " + id);
        return ok("Comments are deleted for Post: " + id);
    }
}
