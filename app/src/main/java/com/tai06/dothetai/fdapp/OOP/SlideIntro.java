package com.tai06.dothetai.fdapp.OOP;

import java.io.Serializable;

public class SlideIntro implements Serializable {
    private int image;
    private String title;
    private String content;

    public SlideIntro() {
    }

    public SlideIntro(int image, String title, String content) {
        this.image = image;
        this.title = title;
        this.content = content;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
