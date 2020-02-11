import models.Model;
import models.Sql2oModel;
import org.flywaydb.core.Flyway;
import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;
import spark.ModelAndView;

import java.util.HashMap;
import java.util.UUID;
import static spark.Spark.*;
import static spark.Spark.get;

public class Main {

    public static void main(String[] args) {
        staticFileLocation("/public");
        String dbName = "acebook";
        for(String a:args) {
            dbName = a;
        }
        System.out.println(dbName);
        Flyway flyway = Flyway.configure().dataSource("jdbc:postgresql://localhost:5432/"+dbName, null, null).load();
        flyway.migrate();

        Sql2o sql2o = new Sql2o("jdbc:postgresql://localhost:5432/" + dbName, null, null, new PostgresQuirks() {
            {
                // make sure we use default UUID converter.
                converters.put(UUID.class, new UUIDConverter());
            }
        });

        Model model = new Sql2oModel(sql2o);

        get("/", (req, res) -> {
            return new ModelAndView(new HashMap(), "templates/index.vtl");
        }, new VelocityTemplateEngine());

        post("/user/new", (req, res) -> {
            String name  = req.queryParams("name");
            String email = req.queryParams("email");
            String password = req.queryParams("password");

            UUID userId = model.createUser(name, email, password);
            req.session().attribute("name", name);
            req.session().attribute("userId", userId);

            res.redirect("/posts");

            return null;
        });

        post("/session", (req, res) -> {
            String email = req.queryParams("login-email");
            String password = req.queryParams("login-password");
            String name;
            UUID userId;

            if (model.authenticate(email, password) == true) {
                name = model.getName(email);
                userId = model.getUserId(email);

                req.session().attribute("name", name);
                req.session().attribute("isSignedIn", true);
                req.session().attribute("userId", userId);
                res.redirect("/posts");
            } else {
                res.redirect("/");
            }

            return null;
        });

        get("/posts", (req, res) -> {

            String name = req.session().attribute("name");
            UUID userId = req.session().attribute("userId");
            Boolean isSignedIn = req.session().attribute("isSignedIn");
//
//            if (isSignedIn == true) {
//
//            } else {
//                res.redirect("/");
//            }
            HashMap postsListings = new HashMap();
            postsListings.put("posts", model.getAllPosts());
            postsListings.put("users", model.getAllUsers());
            postsListings.put("name", name);
            postsListings.put("userId", userId);
            return new ModelAndView(postsListings, "templates/posts.vtl");
        }, new VelocityTemplateEngine());

        post("/posts/new", (req, res) -> {
            String content = req.queryParams("post");
            UUID userId = req.session().attribute("userId");
            String authorName = model.getNameByID(userId);
            model.createPost(content, userId, authorName);
            res.redirect("/posts");
            return null;
        });

        post("session/destroy", (req, res) -> {
            req.session().invalidate();
            res.redirect("/");
            return null;
        });
    }
}
