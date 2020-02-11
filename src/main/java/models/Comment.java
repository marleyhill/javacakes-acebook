package models;

import lombok.Data;

import java.util.UUID;

@Data
public class Comment {
    private UUID comment_id;
    private UUID post_id;
    private String name;
    private String content;
    private UUID user_id;
    private String time_stamp;

    public Comment(UUID comment_id, UUID post_id, String name, String content, UUID user_id, String time_stamp) {
        this.comment_id = comment_id;
        this.post_id = post_id;
        this.name = name;
        this.content = content;
        this.user_id = user_id;
        this.time_stamp = time_stamp;
    }
}
