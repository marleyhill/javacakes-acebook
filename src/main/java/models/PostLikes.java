package models;

import lombok.Data;

import java.util.UUID;

@Data
public class PostLikes {
    UUID post_like_id;
    UUID user_id;
    UUID post_id;


    public PostLikes(UUID post_like_id, UUID user_id, UUID post_id) {
        this.post_like_id = post_like_id;
        this.user_id = user_id;
        this.post_id = post_id;
    }
}
