package com.xiasm.pluginkit.base;

final class AsmWhiteList {

    static boolean isDefaultWhiteListClass(String  className) {
        if (className == null) {
            return false
        }
        if (className.endsWith("R.class")
                || className.endsWith("BuildConfig.class")
                || className.contains("R\$")) {
            return true
        }
        return false
    }

    static boolean isDefaultWhiteListJar(String  jarFilePath) {
        if (jarFilePath == null) {
            return false
        }
        if (jarFilePath.endsWith(".jar")
                && !jarFilePath.contains("com.android.support")
                && !jarFilePath.contains("/com/android/support")) {
            return false
        }
        return true
    }
}
