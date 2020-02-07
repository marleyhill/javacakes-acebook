package models;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;
import java.util.UUID;

public class Sql2oModel implements Model {

    private Sql2o sql2o;

    public Sql2oModel(Sql2o sql2o) {
        this.sql2o = sql2o;

    }

    UUID userId = UUID.fromString("49921d6e-e210-4f68-ad7a-afac266278cb");

    @Override
    public UUID createPost(String content) {
        try (Connection conn = sql2o.beginTransaction()) {
            UUID postId = UUID.randomUUID();
            conn.createQuery("INSERT INTO posts (post_id, user_id, content) VALUES (:post_id, :user_id, :content)")
                    .addParameter("post_id", postId)
                    .addParameter("user_id", userId)
                    .addParameter("content", content)
                    .executeUpdate();
            conn.commit();
            return postId;
        }
    }

    @Override
    public List<Post> getAllPosts() {
        try (Connection conn = sql2o.open()) {
            List<Post> posts = conn.createQuery("SELECT * FROM posts ORDER BY time_stamp DESC")
                    .executeAndFetch(Post.class);
            return posts;
        }
    }

    @Override
    public UUID createUser(String name, String email, String password) {
        try (Connection conn = sql2o.beginTransaction()) {
            UUID userId = UUID.randomUUID();
            conn.createQuery("insert into users(name, email, password) VALUES (:name, email, password)")
                    .addParameter("name", name)
                    .addParameter("email", email)
                    .addParameter("password", password)
                    .executeUpdate();
            conn.commit();
            return userId;
            }
        }
    }