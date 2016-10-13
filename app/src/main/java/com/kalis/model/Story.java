package com.kalis.model;

import java.io.Serializable;

/**
 * Created by Kalis on 1/15/2016.
 */
public class Story implements Serializable {

    private int id;
    private String title;
    private String author;
    private String description;
    private String content;
    private int favorite;
    private String language;
    private String url;
    private byte[] bytes;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public Story() {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Story(int id, String title, String author, String content, String description, int favorite, String language, String url, byte[] bytes) {

        this.id = id;
        this.title = title;
        this.author = author;
        this.content = content;
        this.description = description;
        this.favorite = favorite;
        this.language = language;
        this.url = url;
        this.bytes = bytes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public String getLaguage() {
        return language;
    }

    public void setLaguage(String language) {
        this.language = language;
    }

    public String getUrl() {
        return url;
    }

    public void setSrc(String src) {
        this.url = url;
    }
}
