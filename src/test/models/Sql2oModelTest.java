package models;

import org.apache.log4j.BasicConfigurator;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Sql2oModelTest {

    Sql2o sql2o = new Sql2o("jdbc:postgresql://localhost:5432/" + "acebook_test",
            null, null, new PostgresQuirks() {
        {
            // make sure we use default UUID converter.
            converters.put(UUID.class, new UUIDConverter());
        }
    });

    UUID postId = UUID.fromString("59921d6e-e210-4f68-ad7a-afac266278cb");
    UUID commentId = UUID.fromString("19921d6e-e210-4f68-ad7a-afac266278cb");

    @BeforeAll
    static void setUpClass() {
        BasicConfigurator.configure();
        Flyway flyway = Flyway.configure().dataSource("jdbc:postgresql://localhost:5432/acebook_test", null, null).load();
        flyway.migrate();

    }
    @BeforeEach
    void setUp() {
        Connection conn = sql2o.beginTransaction();
        UUID userId = UUID.fromString("39921d6e-e210-4f68-ad7a-afac266278cb");
        conn.createQuery("INSERT INTO users (user_id, name, email, password) VALUES (:user_id, :name, :email, :password)")
                .addParameter("user_id", userId)
                .addParameter("name", "Test Person 1")
                .addParameter("email", "person1@test.com")
                .addParameter("password", "password")
                .executeUpdate();

        conn.createQuery("INSERT INTO posts (post_id, user_id, content) VALUES (:post_id, :user_id, :content)")
                .addParameter("post_id", postId)
                .addParameter("user_id", userId)
                .addParameter("content", "example content")
                .executeUpdate();

        conn.createQuery("INSERT INTO comments (comment_id, post_id, user_id, content) VALUES (:comment_id, :post_id, :user_id, :content)")
                .addParameter("comment_id", commentId)
                .addParameter("post_id", postId)
                .addParameter("user_id", userId)
                .addParameter("content", "Test Comment")
                .executeUpdate();
        conn.commit();
    }

    @AfterEach
    void tearDown() {
        Connection conn = sql2o.beginTransaction();
        conn.createQuery("TRUNCATE TABLE users CASCADE")
                .executeUpdate();
        conn.commit();
    }

    @Test
    void createUser() {
        Model model = new Sql2oModel(sql2o);
        model.createUser("Test Person 2", "person2@test.com", "password");
        assertEquals(model.getAllUsers().size(), 2);
        assertThat(model.getAllUsers(), hasToString(containsString("Test Person 2")));
    }

    @Test
    void authenticate() {
        Model model = new Sql2oModel(sql2o);
        boolean signInAttempt = model.authenticate("person1@test.com", "password");
        assertThat(signInAttempt, equalTo(true));
    }

    @Test
    void createPost() {
        Connection conn = sql2o.beginTransaction();
        UUID userId = UUID.fromString("49921d6e-e210-4f68-ad7a-afac266278cb");
        conn.createQuery("INSERT INTO users (user_id, name, email, password) VALUES (:user_id, :name, :email, :password)")
                .addParameter("user_id", userId)
                .addParameter("name", "Test Person 1")
                .addParameter("email", "person1@test.com")
                .addParameter("password", "password")
                .executeUpdate();
        conn.commit();

        Model model = new Sql2oModel(sql2o);
        model.createPost("Test Post", userId, "test user");
        assertEquals(model.getAllPosts().size(), 3);
        assertThat(model.getAllPosts(), hasToString(containsString("Test Post")));
    }

    @Test
    void getAllPosts() {
        Model model = new Sql2oModel(sql2o);
        model.getAllPosts();
        assertThat(model.getAllPosts(), hasToString(containsString("example content")));
        assertThat(model.getAllPosts(), hasToString(startsWith("[Post{post_id=59921d6e-e210-4f68-ad7a-afac266278cb")));
    }

    @Test
    void getAllPostsHaveTimestamps() {
        Model model = new Sql2oModel(sql2o);
        model.getAllPosts();
        assertThat(model.getAllPosts(), hasToString(containsString("time_stamp")));
    }

    @Test
    void getAllComments() {
        Model model = new Sql2oModel(sql2o);
        model.getAllComments();
        assertThat(model.getAllComments(), hasToString(containsString("Test Comment")));
    }

    @Test
    void getCommentsByPostId() {
        Model model = new Sql2oModel(sql2o);
        model.getCommentsByPostId(postId);
        assertThat(model.getCommentsByPostId(postId), hasToString(containsString("59921d6e-e210-4f68-ad7a-afac266278cb")));
    }

    @Test
    void createComment() {
        Connection conn = sql2o.beginTransaction();
        UUID userId = UUID.fromString("49921d6e-e210-4f68-ad7a-afac266278cb");
        conn.createQuery("INSERT INTO users (user_id, name, email, password) VALUES (:user_id, :name, :email, :password)")
                .addParameter("user_id", userId)
                .addParameter("name", "Test Person 1")
                .addParameter("email", "person1@test.com")
                .addParameter("password", "password")
                .executeUpdate();
        conn.commit();

        Model model = new Sql2oModel(sql2o);
        model.createComment("Second Comment", userId, postId, "Test Person 1");
        assertThat(model.getAllComments(), hasToString(containsString("Second Comment")));
    }
  
    @Test
    void getName() {
        Model model = new Sql2oModel(sql2o);
        String name = model.getName("person1@test.com");
        assertEquals(name, "Test Person 1");
    }

    @Test
    void getUserId() {
        Model model = new Sql2oModel(sql2o);
        UUID userId = model.getUserId("person1@test.com");
        String userIdAsString = userId.toString();
        assertEquals(userIdAsString, "39921d6e-e210-4f68-ad7a-afac266278cb");

    }

    @Test
    void getNameByID() {
        Model model = new Sql2oModel(sql2o);
        UUID userId = UUID.fromString("39921d6e-e210-4f68-ad7a-afac266278cb");
        String name = model.getNameByID(userId);
        assertEquals(name, "Test Person 1");

    }

    @Test
    void createLike() {
        Model model = new Sql2oModel(sql2o);
        UUID testUserID = model.getUserId("person1@test.com");
        model.createLike(testUserID, postId);
        assertEquals(model.getPostLikes().size(), 1);
        assertThat(model.getPostLikes(), hasToString(containsString("59921d6e-e210-4f68-ad7a-afac266278cb")));
    }

}