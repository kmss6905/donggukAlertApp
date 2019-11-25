package com.example.donggukalertapp.DataModel;

public class dataModel {
    String url; // 해당 아이템 링크주소
    String title; // 제목
    String time; // 등록시간
    String endTime; // 마감일
    boolean isNew = false; // 새로운것?
    boolean havingFile = false; //파일 가지고 있음?
    boolean isNotice = false; // 느낌표 공지

    @Override
    public String toString() {
        return "dataModel{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", time='" + time + '\'' +
                ", endTime='" + endTime + '\'' +
                ", isNew=" + isNew +
                ", havingFile=" + havingFile +
                ", isNotice=" + isNotice +
                '}';
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isNotice() {
        return isNotice;
    }

    public void setNotice(boolean notice) {
        isNotice = notice;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isHavingFile() {
        return havingFile;
    }

    public void setHavingFile(boolean havingFile) {
        this.havingFile = havingFile;
    }
}
