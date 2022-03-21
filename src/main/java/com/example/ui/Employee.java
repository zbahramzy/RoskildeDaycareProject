package com.example.ui;

public class Employee {
    //attributes
    private int employee_id;
    private String firstname;
    private String lastname;
    private String phone;
    private String title;
    //constructor
    public Employee(int employee_id, String firstname, String lastname, String phone, String title) {
        this.employee_id = employee_id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.title = title;
    }
    //getters
    public int getEmployee_id() {
        return employee_id;
    }
    public String getFirstname() {
        return firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public String getPhone() {
        return phone;
    }
    public String getTitle() {
        return title;
    }
    //setters
    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    //toString
    @Override
    public String toString() {
        return "Employee{" +
                "employee_id=" + employee_id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", phone='" + phone + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
