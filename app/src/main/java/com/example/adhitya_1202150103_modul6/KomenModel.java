package com.example.adhitya_1202150103_modul6;

import java.io.Serializable;

//Class model untuk Menerima output dari json Comment di firebase
//implements Serializable berfungsi supaya model dapat di passing menggunakan putExtra
public class KomenModel implements Serializable {
    //Deklarasi variable
    public String name;
    public String email;
    public String comment;

    //konstruktor kosong *diperlukan oleh firebase
    public KomenModel() {
    }

    //konstruktor
    public KomenModel(String name, String email, String comment) {
        this.name = name;
        this.email = email;
        this.comment = comment;
    }

    //getter setter semua variable
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
