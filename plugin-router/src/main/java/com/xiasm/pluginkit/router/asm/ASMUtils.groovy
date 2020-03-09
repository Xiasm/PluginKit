package com.xiasm.pluginkit.router.asm


import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

public final class ASMUtils {
    public static final String ROUTER_MAP_PKG = "com/xiasm/plugin/router/RouterMapping"
    public static final String ROUTER_INTERFACE_PKG = "com/xiasm/pluginkit/router/core/IRouteMapping"
    public static final String ROUTER_MAP_CLASS_NAME = ROUTER_MAP_PKG + ".class"
    public static final String ROUTER_JAVA_SIMPLE_NAME = "RouterMapping.java"


    public static void generateRouterInfoManager(HashMap<String, String> routes, File routerFile) {
        FileOutputStream fos = new FileOutputStream(routerFile)
        JarOutputStream jarOutputStream = new JarOutputStream(fos)
        ZipEntry zipEntry = new ZipEntry(ROUTER_MAP_CLASS_NAME)
        jarOutputStream.putNextEntry(zipEntry)
        jarOutputStream.write(
                ASMCoder.generateRouterInfoManager(
                        ROUTER_MAP_PKG,
                        ROUTER_JAVA_SIMPLE_NAME,
                        ROUTER_INTERFACE_PKG,
                routes))
        jarOutputStream.closeEntry()
        jarOutputStream.close()
        fos.close()
    }

}
