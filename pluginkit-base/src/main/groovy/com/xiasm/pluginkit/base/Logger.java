package com.xiasm.pluginkit.base;

public final class Logger {
    private static final String DEFAULT_TAG = "RouterLogger";

    public static final void d(String msg) {
        d(msg, DEFAULT_TAG);
    }

    public static final void d(String msg, String tag) {
        if (tag == null || "".equals(tag)) {
            tag = DEFAULT_TAG;
        }
        System.out.println("[" + tag + "]" + "  " + msg);
    }
}
