package com.hfhuaizhi.sleep.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fullcircle on 2017/1/3.
 */

public class DBUtils {

    private static Context context = null;

    public static void initDBUtils(Context context){
        DBUtils.context = context.getApplicationContext();
    }

    /**
     * 初始化联系人数据
     */
    public static List<String> initContact(String username){
        List<String> result = new ArrayList<>();
        if(context == null){
            throw new RuntimeException("请调用initDBUtils 初始化之后再使用!");
        }else{
        ContactOpenHelper openHelper = new ContactOpenHelper(context);
            SQLiteDatabase db = openHelper.getReadableDatabase();
            Cursor cursor = db.query("contact_info", new String[]{"contact"}, "username = ?", new String[]{username}, null, null, null);
            while(cursor.moveToNext()){
                String name = cursor.getString(0);
                result.add(name);
            }
            cursor.close();
            db.close();
        }
        return result;
    }

    /**
     * 从网络获取数据之后同步到数据库中
     * @param username 当前的用户名
     * @param contacts 联系人的集合
     */
    public static void updateContactFromEMServer(String username,List<String> contacts){
        if(context == null){
            throw new RuntimeException("请调用initDBUtils 初始化之后再使用!");
        }else{
            ContactOpenHelper openHelper = new ContactOpenHelper(context);
            SQLiteDatabase db = openHelper.getReadableDatabase();
            db.beginTransaction();
            try {
                //把事务标记为成功
                //①先删除本地所有的联系人
                db.delete("contact_info","username = ?",new String[]{username});
                //②把环信的返回结构都保存到数据库中
                ContentValues values = new ContentValues();
                values.put("username",username);
                for(String contact:contacts){
                    values.put("contact",contact);
                    db.insert("contact_info",null,values);
                }
                db.setTransactionSuccessful();
            }finally {
                //结束事务 如果没发现成功标记 会回滚数据
                db.endTransaction();
            }
        }
    }
}
