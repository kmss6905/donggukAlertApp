package com.example.donggukalertapp.DataModel;

public class FileDataModel {
    String url;
    String title;

    @Override
    public String toString() {
        return "FileDataModel{" +
                "\nurl='" + url + '\'' +
                ",\n title='" + title + '\'' +
                '}';
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
