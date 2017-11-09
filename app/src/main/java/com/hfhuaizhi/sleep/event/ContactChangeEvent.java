package com.hfhuaizhi.sleep.event;

/**
 * Created by fullcircle on 2017/1/4.
 */

public class ContactChangeEvent {
    //发生改变的联系人昵称
    public String username;
    //联系人是增加了还是删除了
    public boolean isAdded;

    public ContactChangeEvent(String username, boolean isAdded) {
        this.username = username;
        this.isAdded = isAdded;
    }
}
