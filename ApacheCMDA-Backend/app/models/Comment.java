package models;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by tonyfu on 11/15/15.
 */
@Entity
public class Comment implements Comparable<Comment>{
    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Expose
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "creatorId", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "postId", referencedColumnName = "id")
    private Post post;
    @Expose
    private String content;
    @Expose
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    public Comment() {}

    public Comment(User user, Post post, String content, Date time) {
        super();
        this.user = user;
        setPost(post);
        this.content = content;
        this.time = time;
    }

    public long getId() { return id; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public Post getPost() { return post; }

    public void setPost(Post post) {

        if(post != null) {
            this.post = post;
            if(post.getComments().contains(this) == false) {
                post.getComments().add(this);
                post.setCommentCount(post.getComments().size());
            }
         } else {
            this.post.getComments().remove(this);
            this.post.setCommentCount(this.post.getComments().size());
            this.post = null;
        }

    }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public Date getTime() { return time; }

    public void setTime(Date time) { this.time = time; }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", user=" + user +
                ", post=" + post +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                '}';
    }


    @Override
    public int compareTo(Comment o) {
        //ascending order
        return this.getTime().compareTo(o.getTime());
    }
}
