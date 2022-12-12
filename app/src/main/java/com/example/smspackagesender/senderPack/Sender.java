package com.example.smspackagesender.senderPack;

import java.io.Serializable;

public class Sender implements Serializable {
    private int id;
    private String number;
    private String name;
    private static int cont;

    public Sender(String name, String number) {
        this.id = cont++;
        this.number = number;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
