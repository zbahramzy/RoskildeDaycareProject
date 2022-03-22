package com.example.ui;

public class ParentsSearchModel {
    Integer parent_id;
    String first_name;
    String last_name;
    String phone;

    public ParentsSearchModel(Integer parent_id, String first_name, String last_name, String phone) {
        this.parent_id = parent_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
    }

    public ParentsSearchModel(String first_name, String last_name, String phone) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
    }

    public Integer getParent_id() {
        return parent_id;
    }

    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
//this my todtring for show the information in the table-soheil table
    @Override
    public String toString() {
        return
                //fix emount of space in the table with the different size of screen


                        String.format("%1$-50s FIRST NAME:  %1$-60s", first_name) +
                        String.format("LAST NAME:  %1$-60s", last_name) +
                        String.format("DATE OF BIRTH:  %1$-60s", phone);


    }
}
