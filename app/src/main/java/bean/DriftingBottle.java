package bean;


public class DriftingBottle {

    private int id;


    private int postuserid;


    private int receiveuserid;


    private String bottlecontent;

    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

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

    public String getBottlecontent() {
        return bottlecontent;
    }

    public void setBottlecontent(String bottlecontent) {
        this.bottlecontent = bottlecontent;
    }
}
