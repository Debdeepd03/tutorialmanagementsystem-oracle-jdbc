package com.tutorials.entity;

import java.time.LocalDate;
import java.util.Objects;

public class Tutorial {
    private int id;
    private String title;
    private String author;
    private String url;
    private LocalDate publishedDate;
    public Tutorial() {}

    public Tutorial(String title, String author, String url, LocalDate publishedDate) {
        this.title = title;
        this.author = author;
        this.url = url;
        this.publishedDate = publishedDate;
    }

    public Tutorial(int id, String title, String author, String url, LocalDate publishedDate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.url = url;
        this.publishedDate = publishedDate;
    }

    // getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public LocalDate getPublishedDate() { return publishedDate; }
    public void setPublishedDate(LocalDate publishedDate) { this.publishedDate = publishedDate; }

    @Override
    public String toString() {
        return "Tutorial{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", url='" + url + '\'' +
                ", publishedDate=" + publishedDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tutorial tutorial = (Tutorial) o;
        return id == tutorial.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

