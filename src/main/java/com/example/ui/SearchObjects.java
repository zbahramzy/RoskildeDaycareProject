package com.example.ui;

import java.time.LocalDate;

public class SearchObjects {
    private Integer class_id;
    private Integer parent_id;
    private Integer employee_id;
    private String child_firstname;
    private String child_lastname;
    private LocalDate child_date_of_birth;
    private String parent_firstname;
    private String parent_lastname;
    private String parent_phone;
    private String employee_firstname;
    private String employee_lastname;
    private String employee_phone;
    private String employee_title;

    public SearchObjects(Integer class_id, String child_firstname, String child_lastname, String employee_firstname, String employee_lastname) {
        this.class_id = class_id;
        this.child_firstname = child_firstname;
        this.child_lastname = child_lastname;
        this.employee_firstname = employee_firstname;
        this.employee_lastname = employee_lastname;
    }

    public SearchObjects(String employee_firstname, String employee_lastname, String employee_phone, String employee_title, Integer employee_id) {
        this.employee_firstname = employee_firstname;
        this.employee_lastname = employee_lastname;
        this.employee_phone = employee_phone;
        this.employee_title = employee_title;
        this.employee_id = employee_id;
    }

    public SearchObjects(String parent_firstname, String parent_lastname, String parent_phone) {
        this.parent_firstname = parent_firstname;
        this.parent_lastname = parent_lastname;
        this.parent_phone = parent_phone;
    }

    public SearchObjects(Integer class_id, Integer parent_id, Integer employee_id, String child_firstname, String child_lastname, String parent_firstname, String parent_lastname, String parent_phone, String employee_firstname, String employee_lastname, String employee_phone, String employee_title) {
        this.class_id = class_id;
        this.parent_id = parent_id;
        this.employee_id = employee_id;
        this.child_firstname = child_firstname;
        this.child_lastname = child_lastname;
        this.parent_firstname = parent_firstname;
        this.parent_lastname = parent_lastname;
        this.parent_phone = parent_phone;
        this.employee_firstname = employee_firstname;
        this.employee_lastname = employee_lastname;
        this.employee_phone = employee_phone;
        this.employee_title = employee_title;
    }

    //Getters
    public LocalDate getChild_date_of_birth() {
        return child_date_of_birth;
    }
    public Integer getClass_id() {
        return class_id;
    }
    public Integer getParent_id() {
        return parent_id;
    }
    public Integer getEmployee_id() {
        return employee_id;
    }
    public String getChild_firstname() {
        return child_firstname;
    }
    public String getChild_lastname() {
        return child_lastname;
    }
    public String getParent_firstname() {
        return parent_firstname;
    }
    public String getParent_lastname() {
        return parent_lastname;
    }
    public String getParent_phone() {
        return parent_phone;
    }
    public String getEmployee_firstname() {
        return employee_firstname;
    }
    public String getEmployee_lastname() {
        return employee_lastname;
    }
    public String getEmployee_phone() {
        return employee_phone;
    }
    public String getEmployee_title() {
        return employee_title;
    }

    //Setters
    public void setChild_date_of_birth(LocalDate child_date_of_birth) {
        this.child_date_of_birth = child_date_of_birth;
    }
    public void setClass_id(Integer class_id) {
        this.class_id = class_id;
    }
    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }
    public void setEmployee_id(Integer employee_id) {
        this.employee_id = employee_id;
    }
    public void setChild_firstname(String child_firstname) {
        this.child_firstname = child_firstname;
    }
    public void setChild_lastname(String child_lastname) {
        this.child_lastname = child_lastname;
    }
    public void setParent_firstname(String parent_firstname) {
        this.parent_firstname = parent_firstname;
    }
    public void setParent_lastname(String parent_lastname) {
        this.parent_lastname = parent_lastname;
    }
    public void setParent_phone(String parent_phone) {
        this.parent_phone = parent_phone;
    }
    public void setEmployee_firstname(String employee_firstname) {
        this.employee_firstname = employee_firstname;
    }
    public void setEmployee_lastname(String employee_lastname) {
        this.employee_lastname = employee_lastname;
    }
    public void setEmployee_phone(String employee_phone) {
        this.employee_phone = employee_phone;
    }
    public void setEmployee_title(String employee_title) {
        this.employee_title = employee_title;
    }

    @Override
    public String toString() {
        return "parent_firstname='" + parent_firstname + '\'' +
                ", parent_lastname='" + parent_lastname + '\'' +
                ", parent_phone='" + parent_phone + '\'' +
                '}';
    }
}


