package com.info121.imessenger.models;

public class User {
    String id;
    String name;
    String phone;
    String notiKey;

    public User() {
    }

    public User(String id, String name, String phone, String notiKey) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.notiKey = notiKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNotiKey() {
        return notiKey;
    }

    public void setNotiKey(String notiKey) {
        this.notiKey = notiKey;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", notiKey='" + notiKey + '\'' +
                '}';
    }
}
