package com.hfhuaizhi.sleep.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.hfhuaizhi.sleep.R;
import com.hfhuaizhi.sleep.activity.AddNoteActivity;
import com.hfhuaizhi.sleep.activity.TodoDetailActivity;
import com.hfhuaizhi.sleep.db.domain.Note;
import com.hfhuaizhi.sleep.db.domain.Todo;
import com.hfhuaizhi.sleep.net.NetConfig;
import com.hfhuaizhi.sleep.utils.DialogUtil;
import com.hfhuaizhi.sleep.utils.MyHttpUtil;
import com.hfhuaizhi.sleep.utils.PrefUtils;
import com.hfhuaizhi.sleep.utils.ToastUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.FormBody;


/**
 * Created by fullcircle on 2017/1/3.
 */

public class NoteFragment extends BaseFragment{
    private ListView mLv_todo;
    private Context mContext;
    private FloatingActionButton mFab_add;
    private List<Note> mTodoList;
    private NoteFragment.NoteAdapter mTdAdapter;


    private String mToday;
    private TextView mTv_home_kk;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("fragment","creat!!!!!!!!!!");
        return inflater.inflate(R.layout.fragment_note,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        initData();
        super.onViewCreated(view, savedInstanceState);
    }
    private void initData() {


        mContext = getContext();

        mTodoList = new ArrayList<>();
        getTdData(mTodoList);
        mTdAdapter = new NoteFragment.NoteAdapter(mTodoList);
        mLv_todo.setAdapter(mTdAdapter);
        mFab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNoteItem();
            }
        });
        mFab_add.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {



                return true;
            }
        });
        mLv_todo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtils.showToast(mContext,"click"+position);
                clickItem(position);
            }
        });
        mLv_todo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteNote(position);
                return true;
            }
        });
    }

    private void deleteNote(final int position) {
        DialogUtil.showChooseDialog(mContext, "提示", "确定删除吗?", new DialogUtil.DialogResult() {
            @Override
            public void positive() {

            }

            @Override
            public void negative() {
                mTodoList.remove(position);
                notifyAndSaveAndUpload();
            }
        });
    }

    private void addNoteItem() {
        startActivityForResult(new Intent(mContext, AddNoteActivity.class), 1);
    }

    private void clickItem(int position) {
        Note tmp = mTodoList.get(position);
        Intent intent = new Intent(mContext, AddNoteActivity.class);
        intent.putExtra("tmp",tmp);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            Note tmp = (Note) data.getSerializableExtra("tmp");
            if(tmp!=null){
                if(tmp.content.isEmpty()){
                    return;
                }
                if(tmp.id ==-1){
                    mTodoList.add(0,tmp);
                }else{
                    mTodoList.set(tmp.getId(),tmp);
                }
                notifyAndSaveAndUpload();
            }
        }

    }

    private void notifyAndSaveAndUpload() {
        for(int i=0;i<mTodoList.size();i++){
            mTodoList.get(i).id = i;
        }
        mTdAdapter.notifyDataSetChanged();
        Gson gson = new Gson();
        PrefUtils.setString(mContext,mToday,gson.toJson(mTodoList));
                FormBody formBody = new FormBody.Builder()
                .add("username", PrefUtils.getString(mContext,"username",null))
                .add("notes", gson.toJson(mTodoList))
                .build();
        MyHttpUtil.doPost(NetConfig.SERVER_UPLOADNOTE,formBody,new MyHttpUtil.MyHttpResult() {
            @Override
            public void onFailure(IOException e) {
                ToastUtils.showToast(mContext,"连接服务器失败");

            }

            @Override
            public void onResponse(String response) {
                if(response.equals("success")){
                   // ToastUtils.showToast(mContext,"便签同步成功");
                }else{
                    ToastUtils.showToast(mContext,"便签同步失败");
                }

            }
        });
    }


    private void getTdData(List<Note> todoList) {

        String str = PrefUtils.getString(mContext,"note",null);

        if(str==null){
            getNoteFromServer();
            return;
        }
        mTv_home_kk.setVisibility(View.GONE);
        JsonParser parser = new JsonParser();
        //将JSON的String 转成一个JsonArray对象
        JsonArray jsonArray = parser.parse(str).getAsJsonArray();

        Gson gson = new Gson();
        //加强for循环遍历JsonArray
        for (JsonElement msg : jsonArray) {
            //使用GSON，直接转成Bean对象
            Note userBean = gson.fromJson(msg, Note.class);
            todoList.add(userBean);
        }
    }

    private void getNoteFromServer() {
        FormBody formBody = new FormBody.Builder()
                .add("username", PrefUtils.getString(mContext,"username",null))
                .build();
        MyHttpUtil.doPost(NetConfig.SERVER_GETNOTE, formBody, new MyHttpUtil.MyHttpResult() {
            @Override
            public void onFailure(IOException e) {
                ToastUtils.showToast(mContext,"无法和服务器同步");
            }

            @Override
            public void onResponse(String response) {
                if(response.length()>7){
                    addServerDateToLocal(response);
                }else{
                    ToastUtils.showToast(mContext,"和服务器同步失败");
                }

            }
        });
    }

    private void addServerDateToLocal(String response) {
        mTv_home_kk.setVisibility(View.GONE);
        JsonParser parser = new JsonParser();
        //将JSON的String 转成一个JsonArray对象
        JsonArray jsonArray = parser.parse(response).getAsJsonArray();

        Gson gson = new Gson();
        //加强for循环遍历JsonArray
        for (JsonElement msg : jsonArray) {
            //使用GSON，直接转成Bean对象
            Note userBean = gson.fromJson(msg, Note.class);
            mTodoList.add(userBean);
        }
    }


    private void initView(View view) {
        mLv_todo = view.findViewById(R.id.lv_todo);
        mFab_add = view.findViewById(R.id.fab_add);
        mTv_home_kk = view.findViewById(R.id.tv_home_kk);
    }
    class NoteAdapter extends BaseAdapter {

        public List<Todo> todoList;
        public NoteAdapter(List list){
            todoList = list;
        }
        @Override
        public int getCount() {
            return todoList.size();
        }

        @Override
        public Object getItem(int position) {
            return todoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NoteHolder holder = null;
            if(convertView==null){
                holder = new NoteHolder();
                convertView = View.inflate(mContext,R.layout.item_note,null);
                holder.tv_title = convertView.findViewById(R.id.tv_bq_title);
                holder.tv_content = convertView.findViewById(R.id.tv_bq_zy);
                holder.tv_date = convertView.findViewById(R.id.tv_bq_date);
                convertView.setTag(holder);

            }else{
                holder = (NoteHolder) convertView.getTag();
            }
            Note tmp = mTodoList.get(position);
            holder.tv_title.setText(tmp.title);
            holder.tv_content.setText(tmp.content);
            holder.tv_date.setText(tmp.date);


            return convertView;
        }
        class NoteHolder{
            public TextView tv_title;
            public TextView tv_content;
            public TextView tv_date;
        }
    }


}



