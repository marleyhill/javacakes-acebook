package models;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.sql.SQLOutput;
import java.util.List;
import java.util.UUID;

public class Sql2oModel implements Model {

    private Sql2o sql2o;

    public Sql2oModel(Sql2o sql2o) {
        this.sql2o = sql2o;

    }

 //   UUID userId = UUID.fromString("49921d6e-e210-4f68-ad7a-afac266278cb");

    @Override
    public UUID createUser(String name, String email, String password) {
        try (Connection conn = sql2o.beginTransaction()) {
            UUID userId = UUID.randomUUID();
            conn.createQuery("insert into users(user_id, name, email, password) VALUES (:user_id, :name, :email, :password)")
                    .addParameter("user_id", userId)
                    .addParameter("name", name)
                    .addParameter("email", email)
                    .addParameter("password", password)
                    .executeUpdate();
            conn.commit();
            return userId;
        }

    }

    @Override
    public List<User> getAllUsers() {
        try (Connection conn = sql2o.open()) {
            List<User> users = conn.createQuery("SELECT * FROM users")
                    .executeAndFetch(User.class);
            return users;
        }
    }

    @Override
    public boolean authenticate(String email, String password) {
        boolean correctLoginDetails = false;

        try (Connection conn = sql2o.open()) {
            List<User> authenticatedUser = conn.createQuery("SELECT * FROM users WHERE email = '" + email + "'")
                    .executeAndFetch(User.class);

            if (authenticatedUser == null) {
                return correctLoginDetails;
            } else {
                String dbPassword = conn.createQuery("SELECT password FROM users WHERE email = '" + email + "'")
                        .executeScalar(String.class);
                if (password.equals(dbPassword)) {
                    correctLoginDetails = true;
                }
            }

        }

        return correctLoginDetails;
    }

    @Override
    public String getName(String email) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT name FROM users WHERE email = '" + email + "'")
                    .executeScalar(String.class);

        }
    }
    @Override
    public String getNameByID(UUID user_id) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT name FROM users WHERE user_id = '" + user_id + "'")
                    .executeScalar(String.class);

        }
    }


    @Override
    public UUID getUserId(String email) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT user_id FROM users WHERE email = '" + email + "'")
                    .executeScalar(UUID.class);

        }
    }

    @Override
    public UUID getPostId(String content) {
        try (Connection conn = sql2o.open()) {
            UUID id = conn.createQuery("SELECT post_id FROM posts WHERE content = '" + content + "'")
                    .executeScalar(UUID.class);
            return id;
        }
    }

    @Override
    public UUID createPost(String content, UUID userId, String name) {
        try (Connection conn = sql2o.beginTransaction()) {
            UUID postId = UUID.randomUUID();
            conn.createQuery("INSERT INTO posts (post_id, user_id, name, content) VALUES (:post_id, :user_id, :name, :content)")
                    .addParameter("post_id", postId)
                    .addParameter("user_id", userId)
                    .addParameter("name", name)
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
    public void deletePostByUser(UUID postId, UUID userId) {
        try (Connection conn = sql2o.beginTransaction()) {
            conn.createQuery("DELETE FROM posts WHERE post_id = '"+ postId + "'")
            .executeUpdate();
            conn.commit();
        }
    }

    @Override
    public List<Comment> getAllComments() {
        try (Connection conn = sql2o.open()) {
            List<Comment> comments = conn.createQuery("SELECT * FROM comments")
                    .executeAndFetch(Comment.class);
            return comments;
        }
    }

    @Override
    public UUID createComment(String content, UUID userId, UUID postId, String name) {
        try (Connection conn = sql2o.beginTransaction()) {
            UUID commentId = UUID.randomUUID();
            conn.createQuery("INSERT INTO comments (comment_id, post_id, user_id, name, content) VALUES (:comment_id, :post_id, :user_id, :name, :content)")
                    .addParameter("comment_id", commentId)
                    .addParameter("post_id", postId)
                    .addParameter("user_id", userId)
                    .addParameter("name", name)
                    .addParameter("content", content)
                    .executeUpdate();
            conn.commit();
            return commentId;
        }
    }

    @Override
    public List<Comment> getCommentsByPostId(UUID postId) {
        try(Connection conn = sql2o.open()) {
            List<Comment> comments = conn.createQuery("SELECT * FROM comments WHERE post_id = '" + postId + "'")
                    .executeAndFetch(Comment.class);
            return comments;
        }
    }

    @Override
    public UUID createLike(UUID userID, UUID postID) {
        try (Connection conn = sql2o.beginTransaction()) {
            UUID postLikeID = UUID.randomUUID();
            conn.createQuery("insert into post_likes(post_like_id, user_id, post_id) VALUES (:post_like_id, :user_id, :post_id)")
                    .addParameter("post_like_id", postLikeID)
                    .addParameter("user_id", userID)
                    .addParameter("post_id", postID)
                    .executeUpdate();
            conn.commit();
            return postLikeID;
        }
    }

    @Override
    public int getPostLikesByPostId(UUID postId) {
        try (Connection conn = sql2o.open()) {
            List<PostLikes> likes = conn.createQuery("SELECT * FROM post_likes WHERE post_id = '" + postId + "'")
                    .executeAndFetch(PostLikes.class);
//            int likesNumber = likes.size();
            return likes.size();
        }
    }

    @Override
    public UUID createCommentLike(UUID userId, UUID commentId) {
        try (Connection conn = sql2o.beginTransaction()) {
            UUID commentLikeId = UUID.randomUUID();
            conn.createQuery("INSERT INTO comment_likes (comment_like_id, user_id, comment_id) VALUES (:comment_like_id, :user_id, :comment_id)")
                    .addParameter("comment_like_id", commentLikeId)
                    .addParameter("user_id", userId)
                    .addParameter("comment_id", commentId)
                    .executeUpdate();
            conn.commit();
            return commentLikeId;
        }
    }

    @Override
    public int getCommentLikesByCommentId(UUID commentId) {
        try (Connection conn = sql2o.open()) {
            List<CommentLikes> likes = conn.createQuery("SELECT * FROM comment_likes WHERE comment_id = '" + commentId + "'")
                    .executeAndFetch(CommentLikes.class);
            return likes.size();
        }
    }
          
    @Override
    public String getCommentNameById(UUID user_id) {
    try (Connection conn = sql2o.open()) {
        return conn.createQuery("SELECT name FROM comments WHERE user_id = '" + user_id + "'")
                .executeScalar(String.class);
          
        }
    }

    @Override
    public UUID getCommentId(String content) {
        try (Connection conn = sql2o.open()) {
            UUID id = conn.createQuery("SELECT comment_id FROM comments WHERE content = '" + content + "'")
                    .executeScalar(UUID.class);
            return id;
        }
    }
}

