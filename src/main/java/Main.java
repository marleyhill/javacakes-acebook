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

            model.createUser(name, email, password);
            req.session().attribute("name", name);

            res.redirect("/posts");

            return null;
        });

        post("/sessions", (req, res) -> {
            String email = req.queryParams("email");
            String password = req.queryParams("password");


            req.session().attribute("name" );

            res.redirect("/posts");

            return null;
        });

        get("/posts", (req, res) -> {
            if(model.getAllPosts().size() == 0) {
                UUID postId = model.createPost("test message body");
            }

            HashMap postsListings = new HashMap();
            postsListings.put("posts", model.getAllPosts());
//            System.out.println(postsListings.get("posts"));
////            models.Post test = postsListings.get("posts").get(0);
//            System.out.println(model.getAllPosts().get(0).getContent());
//            System.out.println(postsListings.get("posts"));
            return new ModelAndView(postsListings, "templates/posts.vtl");
        }, new VelocityTemplateEngine());

        post("/posts/new", (request, response) -> {
            String content = request.queryParams("post");
            model.createPost(content);
            response.redirect("/posts");
            return null;
        });
    }
}
