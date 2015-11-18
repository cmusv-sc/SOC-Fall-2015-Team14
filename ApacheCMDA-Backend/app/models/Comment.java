package models;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by tonyfu on 11/15/15.
 */
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "creatorId", referencedColumnName = "id")
    private User user;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "postId", referencedColumnName = "id")
    private Post post;
    private String content;
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    public Comment() {}

    public Comment(User user, Post post, String content, Date time) {
        super();
        this.user = user;
        this.post = post;
        this.content = content;
        this.time = time;
    }

    public long getId() { return id; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public Post getPost() { return post; }

    public void setPost(Post post) { this.post = post; }

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
}
