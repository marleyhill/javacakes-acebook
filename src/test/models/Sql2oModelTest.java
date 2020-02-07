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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Sql2oModelTest {

    Sql2o sql2o = new Sql2o("jdbc:postgresql://localhost:5432/" + "acebook_test",
            null, null, new PostgresQuirks() {
        {
            // make sure we use default UUID converter.
            converters.put(UUID.class, new UUIDConverter());
        }
    });

    UUID userId = UUID.fromString("49921d6e-e210-4f68-ad7a-afac266278cb");
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
        conn.createQuery("INSERT INTO posts (post_id, user_id, content) VALUES (:post_id, :user_id, :content)")
                .addParameter("post_id", postId)
                .addParameter("user_id", userId)
                .addParameter("content", "example content")
                .executeUpdate();
        conn.commit();
    }

    @AfterEach
    void tearDown() {
        Connection conn = sql2o.beginTransaction();
        conn.createQuery("TRUNCATE TABLE posts CASCADE")
                .executeUpdate();
        conn.commit();
    }

    @Test
    void createPost() {
        Model model = new Sql2oModel(sql2o);
        model.createPost("Test Post");
        assertEquals(model.getAllPosts().size(), 2);
    }

    @Test
    void getAllPosts() {
        Model model = new Sql2oModel(sql2o);
        List<Post> posts = new ArrayList<Post>();
        posts.add(new Post(postId1, userId, "some kind of message", "2020-02-07"));
        System.out.println(posts);
        System.out.println("-------------------");
        System.out.println(model.getAllPosts());
        System.out.println(model.getAllPosts().get(0).getClass());
//        assertEquals(model.getAllPosts().get(0).getContent(), "example content");
    }
}