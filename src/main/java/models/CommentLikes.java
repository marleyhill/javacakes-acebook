package models;

import lombok.Data;

import java.util.UUID;

@Data
public class CommentLikes {
    UUID comment_like_id;
    UUID user_id;
    UUID comment_id;

    public CommentLikes(UUID comment_like_id, UUID user_id, UUID comment_id) {
        this.comment_like_id = comment_like_id;
        this.user_id = user_id;
        this.comment_id = comment_id;
    }
}
