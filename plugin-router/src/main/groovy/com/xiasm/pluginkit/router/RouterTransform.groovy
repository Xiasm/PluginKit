package com.xiasm.pluginkit.router

import com.android.build.api.transform.Format
import com.android.build.api.transform.TransformOutputProvider
import com.xiasm.pluginkit.base.AsmBaseTransform
import com.xiasm.pluginkit.base.AsmWhiteList
import com.xiasm.pluginkit.router.asm.ASMUtils
import com.xiasm.pluginkit.router.asm.RouterClassVisitor
import org.apache.commons.io.IOUtils
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

import java.util.jar.JarEntry
import java.util.jar.JarFile

public class RouterTransform extends AsmBaseTransform {
    HashMap<String, String> routers = new HashMap<>();

    public RouterTransform(Project project) {

    }

    @Override
    public boolean isIncremental() {
        return true;
    }

    @Override
    public boolean isWhiteListClass(String className) {
        return AsmWhiteList.isDefaultWhiteListClass(className)
    }

    @Override
    public boolean isWhiteListJar(String jarFilePath) {
        return AsmWhiteList.isDefaultWhiteListJar(jarFilePath)
    }


    @Override
    void processDirClass(String className, File classFile) {
        ClassReader classReader = new ClassReader(classFile.bytes)
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
        RouterClassVisitor classVisitor = new RouterClassVisitor(Opcodes.ASM5, classWriter)
        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
        processAnnotation(classVisitor)
    }

    @Override
    void processJarClass(JarFile jarFile, JarEntry jarEntry) {
        InputStream inputStream = jarFile.getInputStream(jarEntry)
        ClassReader classReader = new ClassReader(IOUtils.toByteArray(inputStream))
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
        RouterClassVisitor classVisitor = new RouterClassVisitor(Opcodes.ASM5, classWriter)
        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
        processAnnotation(classVisitor)
        inputStream.close()
    }

    @Override
    void onTransformStart() {

    }

    @Override
    void onTransformEnd(TransformOutputProvider outputProvider) {
        File routerFile = outputProvider.
                getContentLocation("router", getOutputTypes(), getScopes(), Format.JAR)
        if (!routerFile.getParentFile().exists()) {
            routerFile.getParentFile().delete()
        }
        if (routerFile.exists()) {
            routerFile.delete()
        }
        ASMUtils.generateRouterInfoManager(routers, routerFile)
    }

    public void processAnnotation(RouterClassVisitor rcv) {
        if (rcv != null && rcv.isRouteClass()) {
            String className = rcv.getRouteClassName()
            String routeValue = rcv.getRouteAnnotationValue()
            routers.put(routeValue, className)
        }
    }

}
