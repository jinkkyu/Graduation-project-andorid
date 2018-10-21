package com.example.paparazzi.paparazzi_planer.Memo;

/**
 * Created by LeeJinKyu on 2018-04-20.
 */
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "memo")
public class Memo {
    @DatabaseField(generatedId = true)
    int id;
    @DatabaseField
    String memo;
    @DatabaseField
    Date date;
    @DatabaseField
    String title;
    @DatabaseField
    String year;
    @DatabaseField
    String month;
    @DatabaseField
    String day;
    @DatabaseField
    String helper;
    @DatabaseField
    Boolean isSelected;
    @DatabaseField
    Boolean isClicked;

    public Boolean getClicked() {
        return isClicked;
    }

    public void setClicked(Boolean clicked) {
        isClicked = clicked;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public String getYear() {return year;}

    public void setYear(String year) {this.year = year;}

    public String getMonth() {return month;}

    public void setMonth(String month) {this.month = month;}

    public String getDay() {return day;}

    public void setDay(String day) {this.day = day;}

    public String getHelper() {return helper;}

    public void setHelper(String helper) {this.helper = helper;}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Memo() {

    }

    //create시에 사용할 생성자
    public Memo(String month, String day, String title, String memo, String helper, Boolean isSelected, Boolean isClicked) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.title = title;
        this.memo = memo;
        this.helper = helper;
        this.date = new Date(System.currentTimeMillis());
        this.isSelected = isSelected;
        this.isClicked = isClicked;
    }

    public Memo(String title){
        this.title = title;
    }

}