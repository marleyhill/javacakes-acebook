package models;

import org.apache.log4j.BasicConfigurator;
import org.flywaydb.core.Flyway;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    UUID postId1 = UUID.fromString("69921d6e-e210-4f68-ad7a-afac266278cb");

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

        conn.createQuery("INSERT INTO posts (post_id, user_id, content) VALUES (:post_id, :user_id, :content)")
                .addParameter("post_id", postId1)
                .addParameter("user_id", userId)
                .addParameter("content", "example content 2")
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
        model.createPost("Test Post", userId);
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
}