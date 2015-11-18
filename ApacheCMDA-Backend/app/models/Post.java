package models;

import javax.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dachengwen on 11/3/15.
 */

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "creatorId", referencedColumnName = "id")
    private User user;
    private String title;
    private String content;
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
    private String visibility;
    private int likeCount;
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

    public int getLikeCount() { return likeCount; }

    public void addOneToLikeCount() { likeCount++; }

    public Set<User> getSharedUsers() { return sharedUsers; }

    public void addSharedUsers(User user) { sharedUsers.add(user); }

    @Override
    public String toString() {
        return "Post [id=" + id + ", user=" + user + ", title="
                + title + ", content=" + content + ", time="
                + time + ", visibility=" + visibility + "]";
    }

}

