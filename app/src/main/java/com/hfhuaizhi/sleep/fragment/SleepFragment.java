package com.hfhuaizhi.sleep.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
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
import com.hfhuaizhi.sleep.activity.TodoDetailActivity;
import com.hfhuaizhi.sleep.db.domain.Todo;
import com.hfhuaizhi.sleep.utils.PrefUtils;
import com.hfhuaizhi.sleep.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by fullcircle on 2017/1/3.
 */

public class SleepFragment extends BaseFragment{

    private ListView mLv_todo;
    private Context mContext;
    private FloatingActionButton mFab_add;
    private List<Todo> mTodoList;
    private TodoAdapter mTdAdapter;
    private String mToday;
    private TextView mTv_home_kk;
    private SoundPool mSoundPool;
    private int mSoundID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("fragment","creat!!!!!!!!!!");
        return inflater.inflate(R.layout.fragment_sleep,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        initData();
        super.onViewCreated(view, savedInstanceState);
    }

    private void initData() {

        mContext = getContext();

        initSound();
        mTodoList = new ArrayList<>();
        getTdData(mTodoList);
        mTdAdapter = new TodoAdapter(mTodoList);
        mLv_todo.setAdapter(mTdAdapter);
        mFab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        addTodoItem();


            }
        });
        mFab_add.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                //showPopUp(v);
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
                playSound();
                changeItemState(position);
                return true;
            }
        });
    }

    private void clickItem(int position) {
        Todo tmp = mTodoList.get(position);
        Intent intent = new Intent(mContext, TodoDetailActivity.class);
        intent.putExtra("tmp",tmp);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            Todo tmp = (Todo) data.getSerializableExtra("tmp");
            if(tmp!=null){
                mTodoList.set(tmp.getId(),tmp);
                notifyAndSave();
            }
        }

    }

    private void notifyAndSave() {
        for(int i=0;i<mTodoList.size();i++){
            mTodoList.get(i).id = i;
        }
        mTdAdapter.notifyDataSetChanged();
        Gson gson = new Gson();
        PrefUtils.setString(mContext,mToday,gson.toJson(mTodoList));
    }

    private void playSound() {
        mSoundPool.play(
                mSoundID,0.1f,0.5f,0,0,1
        );
    }
    private void getTdData(List<Todo> todoList) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        mToday = sdf.format(new Date());
        String str = PrefUtils.getString(mContext,mToday,null);
        if(str==null){
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
            Todo userBean = gson.fromJson(msg, Todo.class);
            todoList.add(userBean);
        }
    }

    private void addTodoItem() {
        final Dialog bottomdialog = new Dialog(mContext,R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_msg_huifu, null);
        //
        //
        //初始化控件
        final EditText et_msg_comment = (EditText) inflate.findViewById(R.id.et_msg_comment);
        Button bt_msg_huifu = (Button) inflate.findViewById(R.id.bt_msg_huifu);
        //ImageView iv_td_class = inflate.findViewById(R.id.iv_td_class);
        //将布局设置给Dialog
        bottomdialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = bottomdialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 0;//设置Dialog距离底部的距离
//		       将属性设置给窗体
        WindowManager windowManager = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE); //获取屏幕宽度
        Display display = windowManager.getDefaultDisplay();
        lp.width = (int)(display.getWidth());
        dialogWindow.setAttributes(lp);

        bottomdialog.show();//显示对话框
        // mHandler.sendEmptyMessageDelayed(SHOW_INPUT, 500);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                et_msg_comment.setFocusable(true);
                et_msg_comment.setFocusableInTouchMode(true);
                //请求获得焦点
                et_msg_comment.requestFocus();
                //调用系统输入法
                InputMethodManager inputManager = (InputMethodManager) et_msg_comment
                        .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et_msg_comment, 0);
            }  }, 200);

        //提交
        bt_msg_huifu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String content = et_msg_comment.getText().toString().trim();
                if(content.isEmpty()){
                    ToastUtils.showToast(mContext, "不能空着哦");
                }else{
                    addItemToList(content);
                    bottomdialog.dismiss();
                }

            }
        });




    }

    private void showPopUp(View v) {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setBackgroundColor(Color.GRAY);
        TextView tv = new TextView(mContext);
        tv.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT));
        tv.setText("I'm a pop -----------------------------!");
        tv.setTextColor(Color.WHITE);
        layout.addView(tv);

        PopupWindow popupWindow = new PopupWindow(layout,120,120);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        int[] location = new int[2];
        v.getLocationOnScreen(location);

        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1]-popupWindow.getHeight());
    }
    private void addItemToList(String content) {
        Todo todo = new Todo();
        todo.title = content;
        mTodoList.add(todo);
       notifyAndSave();

    }
    private void initSound() {

        mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        mSoundID = mSoundPool.load(mContext, R.raw.todo, 1);

    }

    private void initView(View view) {
        mLv_todo = view.findViewById(R.id.lv_todo);
        mFab_add = view.findViewById(R.id.fab_add);
        mTv_home_kk = view.findViewById(R.id.tv_home_kk);
    }
class TodoAdapter extends BaseAdapter{

public List<Todo> todoList;
public TodoAdapter(List list){
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        TodoHolder holder = null;
        if(convertView==null){
            holder = new TodoHolder();
            convertView = View.inflate(mContext,R.layout.item_todo,null);
            holder.tv_title = convertView.findViewById(R.id.tv_todo_title);
            holder.bt_cricle_state = convertView.findViewById(R.id.bt_cricle_state);
            holder.tv_msg = convertView.findViewById(R.id.tv_todo_msg);
            convertView.setTag(holder);

        }else{
            holder = (TodoHolder) convertView.getTag();
        }

        holder.tv_title.setText(todoList.get(position).title);
        Resources resources = mContext.getResources();
        Drawable drawable = null;
        switch (todoList.get(position).state){
            case 1:
                drawable = resources.getDrawable(R.drawable.shape_cricle_gray);
                break;
            case 2:
                drawable = resources.getDrawable(R.drawable.shape_cricle_green);
                break;
            case 3:
                drawable = resources.getDrawable(R.drawable.shape_cricle_yellow);
                break;
            case 4:
                drawable = resources.getDrawable(R.drawable.shape_cricle_red);
                break;
        }
        if(todoList.get(position).over){
            drawable = resources.getDrawable(R.drawable.ico_ok);
        }
        holder.bt_cricle_state.setBackground(drawable);

        if(todoList.get(position).over){
            holder.tv_title.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
            holder.tv_title.setTextColor(getResources().getColor(R.color.gray));
        }else{
            holder.tv_title.getPaint().setFlags(0);
            holder.tv_title.setTextColor(Color.BLACK);
        }
        if(todoList.get(position).msg!=null){
            holder.tv_msg.setText(todoList.get(position).msg);
        }
        return convertView;
    }
    class TodoHolder{
        public TextView tv_title;
        public Button bt_cricle_state;
        public TextView tv_msg;
    }
}

    private void changeItemState(int position) {
        if(mTodoList.get(position).over==false){
            mTodoList.get(position).over = true;
        }else{
            mTodoList.get(position).over = false;
        }
        mTdAdapter.notifyDataSetChanged();
    }

}
