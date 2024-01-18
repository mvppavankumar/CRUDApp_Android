package com.example.android_curd_native;

public class Book {
    private int id;
    private String name;
    private String author;
    private String email;
    private String added_by;

    public Book(int id, String name, String author, String email, String added_by) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.email = email;
        this.added_by = added_by;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getEmail() {
        return email;
    }

    public String getAddedBy() {
        return added_by;
    }
}
