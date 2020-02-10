package models;

import lombok.Data;
import java.util.UUID;

@Data
public class User {
    private UUID user_id;
    private String name;
    private String email;
    private String password;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return this.name;
    }


}