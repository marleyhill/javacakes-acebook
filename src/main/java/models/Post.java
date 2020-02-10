package models;

import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Data
public class Post {
    private UUID post_id;
    private UUID user_id;
    private String content;
    private String time_stamp;

    public Post(UUID post_id, UUID user_id, String content, String time_stamp) {
        this.post_id = post_id;
        this.user_id = user_id;
        this.content = content;
        this.time_stamp = time_stamp;
    }

    @Override
    public String toString() {
        return "Post{" +
                "post_id=" + post_id +
                ", user_id=" + user_id +
                ", content='" + content + '\'' +
                ", time_stamp='" + time_stamp + '\'' +
                '}';
    }

    public String getContent() {
        return this.content;
    }

//    public String getDate() {
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
//        String date = sdf.format(this.time_stamp);
//        return date;
//    }
//
//    public String getTime() {
//        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
//        String time = sdf.format(this.time_stamp);
//        return time;
//    }

    public String getTimeStamp() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(this.time_stamp);
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm:ss");
        String dateStr = sdfDate.format(date);
        String timeStr = sdfTime.format(date);
        return "Posted  on: " + dateStr + " at: " + timeStr;
    }

}
