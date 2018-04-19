package com.hfhuaizhi.sleep.db.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2018\4\19 0019.
 */

public class Note implements Serializable{
    public int id = -1;
    public String title;
    public String content;
    public String date;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
