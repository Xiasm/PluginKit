package com.xiasm.pluginkit.sample.testmodule;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.xiasm.pluginkit.common.annotation.Router;
import com.xiasm.pluginkit.router.core.RouterManager;

@Router("/testmodule/main")
public class TestModuleMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_module_main);
    }

    public void startSampleMain(View view) {
        RouterManager.getInstance().start(this, "/sample/main");
    }
}
