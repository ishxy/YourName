package bean;

import java.util.Date;

public class BottleSetting {

    private int id;


    private int postuserid;


    private int receiveuserid;


    private Date endtime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostuserid() {
        return postuserid;
    }

    public void setPostuserid(int postuserid) {
        this.postuserid = postuserid;
    }

    public int getReceiveuserid() {
        return receiveuserid;
    }

    public void setReceiveuserid(int receiveuserid) {
        this.receiveuserid = receiveuserid;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }
}
