package dev.pragma.model;

import com.google.gson.Gson;

public class User {
    private int id;
    private String name;
    private String document;

    public User(int id, String name, String document) {
        this.id = id;
        this.name = name;
        this.document = document;
    }

    public User(String json) {
        Gson gson = new Gson();
        User tempUser = gson.fromJson(json, User.class);
        this.id = tempUser.id;
        this.name = tempUser.name;
        this.document = tempUser.document;
    }
    public String toString(){
        return new Gson().toJson(this);
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

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }
}
