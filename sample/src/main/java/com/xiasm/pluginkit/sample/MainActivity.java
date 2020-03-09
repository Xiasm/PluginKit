package com.xiasm.pluginkit.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.xiasm.pluginkit.common.annotation.Router;
import com.xiasm.pluginkit.router.core.RouterManager;

@Router("/sample/main")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 通过路由插件进行模块跳转
     * @param view
     */
    public void startTextModule(View view) {
        RouterManager.getInstance().start(this, "/testmodule/main");
    }
}
