package cs.hku.group14.schedule.model;

import java.util.Date;

public class NoteEntity {
    private String id;

    private String username;

    private String title;

    private String body;

    private String date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public NoteEntity() {

    }

    public NoteEntity(String username, String title, String body, String date) {
        this.username = username;
        this.title = title;
        this.body = body;
        this.date = date;
    }
}
