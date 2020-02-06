package models;

import lombok.Data;

import java.util.UUID;
@Data
public class Post {
    private UUID post_id;
    private String content;
}
