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

    @Override
    public UUID createPost(String title, String content) {
        //TODO - implement this
        return null;
    }

    @Override
    public List<Post> getAllPosts() {
        //TODO - implement this
        return null;
    }

    @Override
    public User createUser(String name, String email, String password) {
        try (Connection conn = sql2o.beginTransaction()) {
            conn.createQuery("insert into users(name, email, password) VALUES (:name, email, password)")
                    .addParameter("name", name)
                    .addParameter("email", email)
                    .addParameter("password", password)
                    .executeUpdate();
            conn.commit();

        try (Connection conn = sql2o.beginTransaction()) {
            conn.createQuery("insert into users(name, email, password) VALUES (:name, email, password)")
                    .addParameter("name", name)
                    .addParameter("email", email)
                    .addParameter("password", password)
                    .executeUpdate();
            conn.commit();

            return
        }
    }

}