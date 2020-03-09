package com.xiasm.pluginkit.router.core;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import java.util.HashMap;

public class RouterManager {
    private static final String MAP_CLASS = "com.xiasm.plugin.router.RouterMapping";
    private HashMap<String, String> mRouters;
    private static class SingleHolder {
        private static RouterManager INSTANCE = new RouterManager();
    }

    private RouterManager() {
        init();
    }

    private void init() {
        if (mRouters == null) {
            mRouters = new HashMap<>();
        }
        try {
            Class<?> routerINfoManager = Class.forName(MAP_CLASS);
            Object instance = routerINfoManager.newInstance();
            if (instance instanceof IRouteMapping) {
                ((IRouteMapping) instance).getRouterMap(mRouters);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public static RouterManager getInstance() {
        return SingleHolder.INSTANCE;
    }

    public void start(Context context, String routePath) {
        try {
            String classPath = mRouters.get(routePath);
            if (!TextUtils.isEmpty(classPath)) {
                Class<?> activityClass = Class.forName(classPath);
                Intent intent = new Intent(context, activityClass);
                context.startActivity(intent);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
