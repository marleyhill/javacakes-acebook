package models;

import lombok.Data;

import java.util.UUID;

@Data
public class Post {
    private UUID post_id;
    private UUID user_id;
    private String content;
    private String time_stamp;

    public Post(UUID post_id, UUID user_id, String content, String time_stamp) {
        this.post_id = post_id;
        this.user_id = user_id;
        this.content = content;
        this.time_stamp = time_stamp;
    }

    @Override
    public String toString() {
        return "Post{" +
                "post_id=" + post_id +
                ", user_id=" + user_id +
                ", content='" + content + '\'' +
                ", time_stamp='" + time_stamp + '\'' +
                '}';
    }

    public String getContent() {
        return this.content;
    }
}
