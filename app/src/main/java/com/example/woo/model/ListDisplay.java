package com.example.woo.model;

/**
 * Created by Woo on 19/09/2017.
 */

public class ListDisplay {


    private int Code;
    private Long Money;
    private String Group;
    private String With;
    private int Image;
    private String Day;
    private String Note;
    private int CodeSpend;

    public ListDisplay() {

    }

    public ListDisplay(int code, Long money, String group, String with, int image, String day, String note, int codeSpend) {
        Code = code;
        Money = money;
        Group = group;
        With = with;
        Image = image;
        Day = day;
        Note = note;
        CodeSpend = codeSpend;
    }


    public int getCodeSpend() {
        return CodeSpend;
    }

    public void setCodeSpend(int codeSpend) {
        CodeSpend = codeSpend;
    }

    public Long getMoney() {
        return Money;
    }

    public void setMoney(Long money) {
        Money = money;
    }

    public String getGroup() {
        return Group;
    }

    public void setGroup(String group) {
        Group = group;
    }

    public String getWith() {
        return With;
    }

    public void setWith(String with) {
        With = with;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }
    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }
}
