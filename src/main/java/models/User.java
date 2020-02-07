package models;

import lombok.Data;
import java.util.UUID;

@Data
public class User {
    private UUID user_id;
    private String name;
    private String email;
    private String password;


}