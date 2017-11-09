package com.hfhuaizhi.sleep.utils;


import com.hfhuaizhi.sleep.fragment.BaseFragment;
import com.hfhuaizhi.sleep.fragment.SleepFragment;
import com.hfhuaizhi.sleep.fragment.MessageFragment;
import com.hfhuaizhi.sleep.fragment.SleepFragment3;

/**
 * Created by fullcircle on 2017/1/3.
 */

public class FragmentFactory {
    private static SleepFragment sleepFragment = null;
    private static MessageFragment sleepFragment2 = null;
    private static SleepFragment3 sleepFragment3 = null;
//    private static ConversationFragment conversationFragment = null;
//    private static PlugInFragment plugInFragment = null;

    /**
     * 根据底部导航的索引 获取对应的fragment的实例
     * @param position
     * @return
     */
    public static BaseFragment getFragment(int position){
        BaseFragment fragment = null;
        switch (position){
            case 0:
                if(sleepFragment == null){
                    sleepFragment = new SleepFragment();
                }
                fragment = sleepFragment;
                break;
            case 1:
                if(sleepFragment2 == null){
                    sleepFragment2 = new MessageFragment();
                }
                fragment = sleepFragment2;
                break;
            case 2:
                if(sleepFragment3 == null){
                    sleepFragment3 = new SleepFragment3();
                }
                fragment = sleepFragment3;
                break;
        }
        return fragment;
    }
}
