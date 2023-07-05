package com.example.thuchanhduan.model;

public class Item {

    private String name;
    private  Integer id;
    private boolean check;

    public Item() {
    }

    public Item(String name, Integer id, boolean check) {
        this.name = name;
        this.id = id;
        this.check = check;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
