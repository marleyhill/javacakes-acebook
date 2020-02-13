package models;

import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

@Data
public class Post {
    private UUID post_id;
    private UUID user_id;
    private String name;
    private String content;
    private String time_stamp;

    public Post(UUID post_id, UUID user_id, String name, String content, String time_stamp) {
        this.post_id = post_id;
        this.user_id = user_id;
        this.name = name;
        this.content = content;
        this.time_stamp = time_stamp;
    }

    @Override
    public String toString() {
        return "Post{" +
                "post_id=" + post_id +
                ", user_id=" + user_id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", time_stamp='" + time_stamp + '\'' +
                '}';
    }

    public String getContent() {
        return this.content;
    }

    public UUID getUserID() {return this.user_id;}

    public UUID getPostID() {return this.post_id;}

    public String getUserName() { return this.name;}


    public String getTimeStamp() throws ParseException {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(this.time_stamp);
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss z");
        String dateStr = sdfDate.format(date);
        String timeStr = sdfTime.format(date);
        return "on " + dateStr + " at " + timeStr;
    }

}
