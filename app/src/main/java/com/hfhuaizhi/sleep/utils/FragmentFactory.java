package com.hfhuaizhi.sleep.utils;


import com.hfhuaizhi.sleep.fragment.BaseFragment;
import com.hfhuaizhi.sleep.fragment.SleepFragment;

/**
 * Created by fullcircle on 2017/1/3.
 */

public class FragmentFactory {
    private static SleepFragment sleepFragment = null;
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
//                if(contactFragment == null){
//                    contactFragment = new ContactFragment();
//                }
//                fragment = contactFragment;
                break;
            case 2:
//                if(plugInFragment == null){
//                    plugInFragment = new PlugInFragment();
//                }
//                fragment = plugInFragment;
                break;
        }
        return fragment;
    }
}
