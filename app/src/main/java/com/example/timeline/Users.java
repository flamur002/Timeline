package com.example.timeline;

public class Users {

    private String name;
    private String surname;
    private String username;
    private String password;
    private String ID;


    public Users(String name, String surname, String username, String password) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
    }

    public Users(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Users(String username) {
        this.username = username;
    }

    public Users(String name, String surname, String username, String password, String ID) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
