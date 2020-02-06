package models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.*;
import org.sql2o.Connection;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    //    @BeforeEach
//    void setUp() {
//        Connection conn = sql2o.beginTransaction();
//        conn.createQuery("insert into posts(post_id, content) VALUES (:post_id, :content)")
//                .addParameter("post_id", 100)
//                .addParameter("content", "example content")
//                .executeUpdate();
//
//        conn.commit();
//    }

    @Test
    public void createUser_createsNewUser() {
        User testUser = User.createUser();
        assertThat(testUser, is(instanceOf(User.class)));
    }

    @AfterEach
    void tearDown() {
    }
}