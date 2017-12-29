package com.ares.xq.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MenuView menu = findViewById(R.id.menu_tabs);
        String[] titles = {"菜单-1", "菜单-2", "菜单-3", "菜单-4"};
        menu.setDropDownMenu(Arrays.asList(titles));
        menu.setListener(new MenuView.MenuViewListner() {
            @Override
            public void onClickCancle(int tag) {
                LogUtils.i("取消----" + tag);
                menu.setTabText(tag, "取消" + tag);
            }

            @Override
            public void onClickMenuItem(int tag) {
                LogUtils.i("选中----" + tag);
                menu.setTabText(tag, "选中" + tag);
            }
        });
    }
}
