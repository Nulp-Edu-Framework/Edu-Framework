package com.nulp.eduframework.model;

import java.util.Date;

public class Message {
    private final String text;
    private final String author;

    public Message(String author, String text) {
        this.author = author;
        this.text = text;
    }

    public String toString() {
        return "{ \"text\" : \"" + text + "\", \"author\" : \"" + author + "\" , \"time\" : " + new Date().getTime() + "}";
    }
}
