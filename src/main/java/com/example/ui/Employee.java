package com.example.ui;

public class Employee {
    // attributes
    private int employee_id;
    private String first_name;
    private String last_name;
    private String phone;
    private String title;
    // constructor
    Employee(int employee_id, String first_name, String last_name, String phone_number, String title) {
        setEmployee_id(employee_id);
        setFirst_name(first_name);
        setLast_name(last_name);
        setPhone(phone_number);
        setTitle(title);
    }
    // getters
    public int getEmployee_id() {
        return employee_id;
    }
    public String getFirst_name() {
        return first_name;
    }
    public String getLast_name() {
        return last_name;
    }
    public String getPhone() {
        return phone;
    }
    public String getTitle() {
        return title;
    }
    // setters
    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}
