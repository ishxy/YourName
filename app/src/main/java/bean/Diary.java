package bean;


import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

public class Diary<T> implements Serializable{

    private int id;


    private int userid;


    private String content;


    private Date contenttime;

    private String location;


    private String contentphote;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getContenttime() {
        return contenttime;
    }

    public void setContenttime(Date contenttime) {
        this.contenttime = contenttime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContentphote() {
        return contentphote;
    }

    public void setContentphote(String contentphote) {
        this.contentphote = contentphote;
    }

    
}
