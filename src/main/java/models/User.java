package models;

public class User {
    private int userId;
    private String name;
    private String email;
    private String password;
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
//    static User createUser(String name, String email, String password) {
//       User user = new User();
//       return user;
//    }
}