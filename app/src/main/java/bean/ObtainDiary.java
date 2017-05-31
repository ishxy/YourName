package bean;


import java.util.Date;

public class ObtainDiary {

    private int id;


    private int userid;


    private int otheruserid;


    private Date diarytime;


    private double completion;

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

    public int getOtheruserid() {
        return otheruserid;
    }

    public void setOtheruserid(int otheruserid) {
        this.otheruserid = otheruserid;
    }

    public Date getDiarytime() {
        return diarytime;
    }

    public void setDiarytime(Date diarytime) {
        this.diarytime = diarytime;
    }

    public double getCompletion() {
        return completion;
    }

    public void setCompletion(double completion) {
        this.completion = completion;
    }
}
