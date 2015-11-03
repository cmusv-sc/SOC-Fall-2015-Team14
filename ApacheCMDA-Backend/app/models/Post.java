package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by dachengwen on 11/3/15.
 */

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String userId;
    private String title;
    private String content;
    private String time;
    private String visibility;

    // @OneToMany(mappedBy = "user", cascade={CascadeType.ALL})
    // private Set<ClimateService> climateServices = new
    // HashSet<ClimateService>();

    public Post() {
    }

    public Post(String userId, String title, String content,
                String time, String visibility) {
        super();
        this.userId = userId;
        this.content = content;
        this.time = time;
        this.visibility = visibility;
        this.title = title;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    @Override
    public String toString() {
        return "Post [id=" + id + ", userId=" + userId + ", title="
                + title + ", content=" + content + ", time="
                + time + ", visibility=" + visibility + "]";
    }

}

