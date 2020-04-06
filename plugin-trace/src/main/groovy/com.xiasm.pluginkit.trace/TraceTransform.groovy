package com.xiasm.pluginkit.trace;

import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformOutputProvider
import com.xiasm.pluginkit.base.AsmBaseTransform
import com.xiasm.pluginkit.base.AsmWhiteList
import com.xiasm.pluginkit.trace.asm.TraceClassFileVisitor
import org.apache.commons.io.IOUtils
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes;

import java.util.Set
import java.util.jar.JarEntry
import java.util.jar.JarFile;

public class TraceTransform extends AsmBaseTransform {
    private Project project

    TraceTransform(Project project) {
        this.project = project
    }

    @Override
    public boolean isIncremental() {
        return true
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
        TraceClassFileVisitor classVisitor = new TraceClassFileVisitor(Opcodes.ASM5, classWriter)
        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)

        FileOutputStream fos = new FileOutputStream(classFile.path)
        fos.write(classWriter.toByteArray())
        fos.close()
    }

    @Override
    void processJarClass(JarFile jarFile, JarEntry jarEntry) {
        InputStream inputStream = jarFile.getInputStream(jarEntry)
        ClassReader classReader = new ClassReader(IOUtils.toByteArray(inputStream))
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
        TraceClassFileVisitor classVisitor = new TraceClassFileVisitor(Opcodes.ASM5, classWriter)
        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
        inputStream.close()
    }

    @Override
    void onTransformStart() {

    }

    @Override
    void onTransformEnd(TransformOutputProvider transformOutputProvider) {

    }
}
