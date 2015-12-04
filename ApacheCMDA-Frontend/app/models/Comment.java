package models;

import java.util.Date;

/**
 * @author: Xunrong Li
 * @andrewID: xunrongl
 * Created on 12/2/15.
 */
public class Comment {
    private long id;
    private User user;
    private Post post;
    private String content;
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
