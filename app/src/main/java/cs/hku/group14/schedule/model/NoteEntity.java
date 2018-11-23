package cs.hku.group14.schedule.model;

import java.util.Date;

public class NoteEntity {
    private String title;

    private String body;

    private Date date;

    private String location;

    private int level;

    public NoteEntity(String title, String body, Date date, String location) {
        this.title = title;
        this.body = body;
        this.date = date;
        this.location = location;
    }

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
