package bean;

import java.util.ArrayList;

/**
 * Created by caolu on 2017/5/31.
 */

public class DiaryList extends BaseBean{
    private ArrayList<Diary> datalist;
    private String choisenDay;
    private Integer randUserId;

    public Integer getRandUserId() {
        return randUserId;
    }

    public void setRandUserId(Integer randUserId) {
        this.randUserId = randUserId;
    }

    public String getChoisenDay() {

        return choisenDay;
    }

    public void setChoisenDay(String choisenDay) {
        this.choisenDay = choisenDay;
    }

    public ArrayList<Diary> getDatalist() {
        return datalist;
    }

    public void setDatalist(ArrayList<Diary> datalist) {
        this.datalist = datalist;
    }
}
