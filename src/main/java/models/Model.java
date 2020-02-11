package models;


import java.util.List;
import java.util.UUID;

public interface Model {
    UUID createPost(String content, UUID userId);
    List getAllPosts();

    UUID createUser(String name, String email, String password);
    List getAllUsers();
    boolean authenticate(String email, String password);
    String getName(String email);
    UUID getUserId(String email);

    UUID createComment(String content, UUID userId, UUID postId);
    List getAllComments();

    List getCommentsByPostId(UUID postId);
}


