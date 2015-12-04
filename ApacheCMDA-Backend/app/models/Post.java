package models;

import com.google.gson.annotations.Expose;

import javax.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
    private String visibility;
    @Expose
    private int likeCount;

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


    // @OneToMany(mappedBy = "user", cascade={CascadeType.ALL})
    // private Set<ClimateService> climateServices = new
    // HashSet<ClimateService>();

    public Post() {
    }

    public Post(User user, String title, String content,
                Date time, String visibility) {
        super();
        this.user = user;
        this.content = content;
        this.time = time;
        this.visibility = visibility;
        this.title = title;
        this.likeCount = 0;
        this.likeUsers = new HashSet<>();
        this.sharedUsers = new HashSet<>();
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

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public Set<User> getLikeUsers() { return likeUsers; }

    public void addLikeUsers(User user) { likeUsers.add(user); likeCount++; }

    public Set<User> getSharedUsers() { return sharedUsers; }

    public void addSharedUsers(User user) { sharedUsers.add(user); }

    @Override
    public String toString() {
        return "Post [id=" + id + ", user=" + user + ", title="
                + title + ", content=" + content + ", time="
                + time + ", visibility=" + visibility + "]";
    }

}

