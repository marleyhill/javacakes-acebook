package models;


import java.util.List;
import java.util.UUID;

public interface Model {
    UUID createPost(String content);
    List getAllPosts();

    UUID createUser(String name, String email, String password);
}


