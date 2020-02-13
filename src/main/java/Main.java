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
            Boolean loginError = false;
            Boolean deletePostError = false;

            if ((req.session().attribute("loginError")) != null) {
                loginError = true;
            } else if ((req.session().attribute("isSignedIn")) != null) {
                res.redirect("/posts");
            }

            HashMap index = new HashMap();

            if (loginError == true) {
                index.put("loginError", true);
            }
            return new ModelAndView(index, "templates/index.vtl");
        }, new VelocityTemplateEngine());

        post("/user/new", (req, res) -> {
            String name  = req.queryParams("name");
            String email = req.queryParams("email");
            String password = req.queryParams("password");

            UUID userId = model.createUser(name, email, password);
            req.session().attribute("name", name);
            req.session().attribute("userId", userId);
            req.session().attribute("isSignedIn", true);
            req.session().attribute("deletePostError", false);
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
                req.session().attribute("deletePostError", false);
                req.session().attribute("userId", userId);
                res.redirect("/posts");
            } else {
                req.session().attribute("loginError", true);
                res.redirect("/");
            }

            return null;
        });

        get("/posts", (req, res) -> {

            String name = req.session().attribute("name");
            UUID userId = req.session().attribute("userId");
            Boolean isSignedIn = req.session().attribute("isSignedIn");

            try {
                if (isSignedIn == true) {
                    Boolean deletePostError = req.session().attribute("deletePostError");
                    UUID deletePostId = req.session().attribute("deletePostId");
                    HashMap postsListings = new HashMap();
                    postsListings.put("posts", model.getAllPosts());
                    postsListings.put("modelMethods", model);
                    postsListings.put("name", name);
                    postsListings.put("userId", userId);
                    if(deletePostError == true) {
                        postsListings.put("deletePostError", true);
                        postsListings.put("deletePostId", deletePostId);
                    }
                    req.session().attribute("deletePostError", false);
                    return new VelocityTemplateEngine().render(
                            new ModelAndView(postsListings, "templates/posts.vtl")
                    );

                }
            } catch (Exception e) {

                res.redirect("/");
            }

            return null;

        });

        post("/posts/new", (req, res) -> {

            String content;

            Boolean isSignedIn = req.session().attribute("isSignedIn");
            if (isSignedIn == true) {
                content = req.queryParams("post");
                UUID userId = req.session().attribute("userId");
                String authorName = model.getNameByID(userId);
                model.createPost(content, userId, authorName);
                res.redirect("/posts");
                }
            return null;
        });

        post("/posts/delete", (req, res) -> {
            Boolean deletePostError = false;
            UUID userId = req.session().attribute("userId");
            String idAsString = req.queryParams("post-id-delete");
            UUID deletePostId = UUID.fromString(idAsString);
            UUID postAuthorId = model.getPostAuthorId(deletePostId);
            if(!userId.toString().equals(postAuthorId.toString())) {
                deletePostError = true;
                req.session().attribute("deletePostError", true);
                req.session().attribute("deletePostId", deletePostId);
            } else {
                model.deletePostByUser(deletePostId, postAuthorId);
                req.session().attribute("deletePostError", false);
            }
            res.redirect("/posts");
            return null;
            });

        post("/comments/new", (req, res) -> {
            Boolean isSignedIn = req.session().attribute("isSignedIn");
            if (isSignedIn == true) {
                String postIdAsString = req.queryParams("post-id-for-comment");
                UUID postId = UUID.fromString(postIdAsString);
                String content = req.queryParams("comment");
                UUID userId = req.session().attribute("userId");
                String authorName = model.getNameByID(userId);
                model.createComment(content, userId, postId, authorName);
                res.redirect("/posts");
            }else {
                res.redirect("/");
            }
            return null;
        });

        post("/comments/delete", (req, res) -> {
            UUID currentUserId = req.session().attribute("userId");
            String commentIdAsString = req.queryParams("comment-id-delete");
            UUID deleteCommentId = UUID.fromString(commentIdAsString);
            UUID commentAuthorId = model.getCommentAuthorId(deleteCommentId);
            if(currentUserId.equals(commentAuthorId)) {
                model.deleteCommentByUser(deleteCommentId, commentAuthorId);
                res.redirect("/posts");
            }else{
                res.redirect("/posts");
            }
            return null;
        });

        post("session/destroy", (req, res) -> {
            req.session().invalidate();
            res.redirect("/");
            return null;
        });

        internalServerError((req, res) -> {
            res.type("application/json");
            return "Email already exists, please try again";
        });

        post("/posts/likes", (req, res) -> {
            UUID userId = req.session().attribute("userId");
            System.out.println("userID: " + userId);
            String idAsString = req.queryParams("post-id-for-like");
            System.out.println("post id for like: " + idAsString);
            UUID postIdRetrieved = UUID.fromString(idAsString);
            System.out.println("post id retrieved: " + postIdRetrieved);
            System.out.println(model.createLike(userId, postIdRetrieved));
            res.redirect("/posts");
            return null;
        });

        post("/comments/likes", (req, res) -> {
            UUID userId = req.session().attribute("userId");
            String commentIdAsString = req.queryParams("comment-id-for-like");
            UUID commentId = UUID.fromString(commentIdAsString);
            model.createCommentLike(userId, commentId);
            res.redirect("/posts");
            return null;
        });
    }
}
