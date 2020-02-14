package models;


import java.util.List;
import java.util.UUID;

public interface Model {
    UUID getPostId(String content);
    UUID getPostAuthorId(UUID postId);
    UUID createPost(String content, UUID userId, String name);
    List  getAllPosts();
    void deletePostByUser(UUID postId, UUID userId);

    UUID createUser(String name, String email, String password);
    Boolean isUserExisting(String name, String email);
    List getAllUsers();
    boolean authenticate(String email, String password);
    String getName(String email);
    String getNameByID(UUID user_id);
    UUID getUserId(String email);

    void deleteCommentByUser(UUID commentId, UUID userId);

    UUID getCommentAuthorId(UUID commentId);

    UUID createComment(String content, UUID userId, UUID postId, String name);
    List getAllComments();

    List getCommentsByPostId(UUID postId);

    UUID createLike(UUID userID, UUID postId);
    Boolean isLikeExisting(UUID userID, UUID postID);

    int getPostLikesByPostId(UUID postId);

    UUID createCommentLike(UUID userId, UUID commentId);
    Boolean isCommentLikeExisting(UUID userID, UUID commentID);

    int getCommentLikesByCommentId(UUID commentId);

    String getCommentNameById(UUID user_id);

    UUID getCommentId(String content);
}


