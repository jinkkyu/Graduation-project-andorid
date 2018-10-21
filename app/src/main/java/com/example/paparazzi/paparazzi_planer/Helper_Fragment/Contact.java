package com.example.paparazzi.paparazzi_planer.Helper_Fragment;

import java.util.ArrayList;

/**전화번호부 - 변수와 게터,세터로만 구성된 클래스를 POJO pure old java class
 */
public class Contact {
    private int id;
    private String name;
    private ArrayList<String> number;
    private ArrayList<Contact> contact;

    public ArrayList<Contact> getContact () {
        return contact;
    }

    public Contact() {
        number = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getNumber() {
        return number;
    }

    public void setNumber(ArrayList<String> number) {
        this.number = number;
    }

    public void addNumber(String number) {
        this.number.add(number);
    }

    public void removeNumber(String number) {
        this.number.remove(number);
    }

    public String getNumberOne(){
        if(number.size() > 0)
            return number.get(0);
        else
            return null;
    }
}
