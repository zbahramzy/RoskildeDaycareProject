package com.example.ui;

public class ShowRoomsObjects {
    Integer class_id;
    String child_firstname;
    String child_lastname;
    String  teacher_firstname;
    String  teacher_lastname;

    public ShowRoomsObjects(Integer class_id, String child_firstname, String child_lastname, String teacher_firstname, String teacher_lastname) {
        this.class_id = class_id;
        this.child_firstname = child_firstname;
        this.child_lastname = child_lastname;
        this.teacher_firstname = teacher_firstname;
        this.teacher_lastname = teacher_lastname;
    }

    public Integer getClass_id() {return class_id;}
    public String getChild_firstname() {return child_firstname;}
    public String getChild_lastname() {return child_lastname;}
    public String getTeacher_firstname() {return teacher_firstname;}
    public String getTeacher_lastname() {return teacher_lastname;}

    public void setClass_id(Integer class_id) {this.class_id = class_id;}
    public void setChild_firstname(String child_firstname) {this.child_firstname = child_firstname;}
    public void setChild_lastname(String child_lastname) {this.child_lastname = child_lastname;}
    public void setTeacher_firstname(String teacher_firstname) {this.teacher_firstname = teacher_firstname;}
    public void setTeacher_lastname(String teacher_lastname) {this.teacher_lastname = teacher_lastname;}
}
