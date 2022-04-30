package com.example.ready4gre.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.ready4gre.R;
import com.example.ready4gre.fragment.StarFragment;
import com.example.ready4gre.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {

    private MainFragment mainFragment;
    private StarFragment starFragment;
    private DrawerLayout mainDrawerLayout;
    private RelativeLayout rlHome, rlStar;

    private int currentSelectItem = R.id.rl_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        findViewById(R.id.iv_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.iv_menu:// 打开左边抽屉
                        mainDrawerLayout.openDrawer(Gravity.LEFT);
                        break;
                }
            }
        });

        initLeftMenu(); //初始化左边菜单

        mainFragment = new MainFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.content_frame, mainFragment).commit();

        setWindowStatus();
    }

    private void initLeftMenu() {
        rlHome = (RelativeLayout) findViewById(R.id.rl_home);
        rlStar = (RelativeLayout) findViewById(R.id.rl_star);

        rlHome.setOnClickListener(onLeftMenuClickListener);
        rlStar.setOnClickListener(onLeftMenuClickListener);

        rlHome.setSelected(true);
    }

    private View.OnClickListener onLeftMenuClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (currentSelectItem != v.getId()) {//防止重复点击
                currentSelectItem = v.getId();
                noItemSelect();
                changeFragment(v.getId());//设置fragment显示切换
                switch (v.getId()) {
                    case R.id.rl_home:
                        rlHome.setSelected(true);
                        break;
                    case R.id.rl_star:
                        rlStar.setSelected(true);
                        break;
                }
                mainDrawerLayout.closeDrawer(Gravity.LEFT);
            }
        }
    };

    private void noItemSelect(){
        rlHome.setSelected(false);
        rlStar.setSelected(false);
    }

    private void changeFragment(int resId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();//开启一个Fragment事务

        hideFragments(transaction);//隐藏所有fragment
        if(resId == R.id.rl_home){//主页
            if(mainFragment == null){//如果为空先添加进来.不为空直接显示
                mainFragment = new MainFragment();
                transaction.add(R.id.content_frame, mainFragment);
            }else {
                transaction.show(mainFragment);
            }
        }else if(resId == R.id.rl_star){
            if(starFragment == null){
                starFragment = new StarFragment();
                transaction.add(R.id.content_frame, starFragment);
            }else {
                transaction.show(starFragment);
            }
        }
        transaction.commitAllowingStateLoss();//一定要记得提交事务
    }

    private void hideFragments(FragmentTransaction transaction){
        if (mainFragment != null)//不为空才隐藏,如果不判断第一次会有空指针异常
            transaction.hide(mainFragment);
        if (starFragment != null)
            transaction.hide(starFragment);
    }

    // 设置状态栏
    private void setWindowStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            // 设置状态栏颜色
            getWindow().setBackgroundDrawableResource(R.color.main_color);
        }
    }
}