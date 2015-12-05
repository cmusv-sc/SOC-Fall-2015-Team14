package models;

import com.google.gson.annotations.Expose;

import javax.persistence.*;

import java.util.*;

/**
 * Created by dachengwen on 11/3/15.
 */

@Entity
public class Post {

    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @Expose
    @JoinColumn(name = "creatorId", referencedColumnName = "id")
    private User user;
    @Expose
    private String title;
    @Expose
    private String content;
    @Expose
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
    @Expose
    private boolean visibility;
    @Expose
    private int likeCount;
    @Expose
    private int shareCount;

    @Expose
    @ManyToMany (fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(name = "LikePost",
            joinColumns = {@JoinColumn(name = "post_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    private Set<User> likeUsers;

    @ManyToMany (fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(name = "SharedPost",
            joinColumns = {@JoinColumn(name = "post_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    private Set<User> sharedUsers;

    @Expose
    @OneToMany(mappedBy="post")
    private List<Comment> comments;
    @Expose
    private int commentCount;


    // @OneToMany(mappedBy = "user", cascade={CascadeType.ALL})
    // private Set<ClimateService> climateServices = new
    // HashSet<ClimateService>();

    public Post() {
    }

    public Post(User user, String title, String content,
                Date time, boolean visibility) {
        super();
        this.user = user;
        this.content = content;
        this.time = time;
        this.visibility = visibility;
        this.title = title;
        this.likeCount = 0;
        this.likeUsers = new HashSet<>();
        this.shareCount = 0;
        this.sharedUsers = new HashSet<>();
        this.comments = new LinkedList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() { return time; }

    public void setTime(Date time) {
        this.time = time;
    }

    public boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public Set<User> getLikeUsers() { return likeUsers; }

    public void addLikeUsers(User user) { likeUsers.add(user); likeCount = likeUsers.size(); }

    public Set<User> getSharedUsers() { return sharedUsers; }

    public void addSharedUsers(User user) { sharedUsers.add(user); shareCount = sharedUsers.size();}

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public void setLikeUsers(Set<User> likeUsers) {
        this.likeUsers = likeUsers;
    }

    public void setSharedUsers(Set<User> sharedUsers) {
        this.sharedUsers = sharedUsers;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        commentCount = comments.size();
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    //https://en.wikibooks.org/wiki/Java_Persistence/OneToMany
    public void addComment(Comment comment) {
        this.comments.add(comment);
        if (comment.getPost() != this) {
            comment.setPost(this);
        }
        commentCount = comments.size();
    }

    //don't invoke this from Comment.setPost!
    public void removeComment(Comment comment) {
        if(comment.getPost() == this) {
            comment.setPost(null);
            this.comments.remove(comment);
            commentCount = comments.size();
        }
    }
    @Override
    public String toString() {
        return "Post [id=" + id + ", user=" + user + ", title="
                + title + ", content=" + content + ", time="
                + time + ", visibility=" + visibility + "]";
    }

}

