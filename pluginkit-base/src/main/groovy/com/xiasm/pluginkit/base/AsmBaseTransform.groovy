package com.xiasm.pluginkit.base

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager

import java.util.jar.JarEntry
import java.util.jar.JarFile

/**
 * 封装以下几个功能
 * （1）统一处理transform 对文件的操作过程
 * （2）封装样板式的实现
 * （3）支持增量编译
 */
abstract class AsmBaseTransform extends Transform {

    @Override
    String getName() {
        return this.getClass().getSimpleName()
    }

    /**
     * ContentType，数据类型
     * 其中的"CONTENT_CLASS"包含了源项目中的.class文件和第三方库中的.class文件
     * @return 对应getInputTypes() 方法
     */
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    /**
     * Scope，表示要处理的.class文件的范围
     * 主要有 PROJECT， SUB_PROJECTS，EXTERNAL_LIBRARIES等
     * @return 对应getScopes() 方法
     */
    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    /**
     * 是否开启增量编译
     * 如果开启了，即 isIncremental() 返回true
     * 在transform() 是否是增量编译还需要进行判断
     *      1、clean之后，第一次编译，即使Transform里面isIncremental()返回true，
     *      此时对Transform来说仍然不是增量编译， transform方法中isIncremental = false；
     *
     *      2、不做任何改变直接进行第二次编译，Transform别标记为up-to-date，被跳过执行；
     *
     *      3、修改一个文件中代码，进行第三次编译，此时对Transform来说是增量编译，transform() 方法中 isIncremental = true
     *
     *      结论：一次编译对Transform来说是否是增量编译取决于两个方面：
     *          （1）当前Transform是否开启增量编译；
     *          （2）当前编译是否有增量基础；
     *
     * 如果不是增量编译，就依次处理所有的class文件（参考 plugin-router 中 RouterTransform）
     * 如果是增量编译，就需要根据每个文件的 Status 处理文件
     *      NOTCHANGED: 当前文件不需处理，甚至复制操作都不用；
     *      ADDED、CHANGED: 正常处理，输出给下一个任务 ；
     *      REMOVED: 移除outputProvider获取路径对应的文件；
     *
     * @return
     */
    @Override
    boolean isIncremental() {
        return true
    }

    @Override
    void transform(Context context,
                   Collection<TransformInput> inputs,
                   Collection<TransformInput> referencedInputs,
                   TransformOutputProvider outputProvider,
                   boolean isIncremental) throws IOException, TransformException, InterruptedException {
        if (!isIncremental) {
            outputProvider.deleteAll()
        }
        TransformProcessor processor = new TransformProcessor(this)
        inputs.each { TransformInput input ->
            input.directoryInputs.each { DirectoryInput directoryInput ->
                processor.processDir(outputProvider, directoryInput, isIncremental)
            }

            input.jarInputs.each { JarInput jarInput ->
                processor.processJar(outputProvider, jarInput, isIncremental)
            }

        }

        onTransformEnd(outputProvider)
    }

    /**
     * ASM处理需要过滤的类 白名单
     * className.endsWith("R.class")
     * || className.endsWith("BuildConfig.class")
     * || name.contains("R\$")
     * @param className
     * @return 是否需要过滤的类
     */
    abstract boolean isWhiteListClass(String className)

    /**
     * ASM处理需要过滤的jar 白名单
     * filePath.endsWith(".jar")
     * && !filePath.contains("com.android.support")
     * && !filePath.contains("/com/android/support")
     * @param jarName
     * @return 是否需要过滤的jar
     */
    abstract boolean isWhiteListJar(String jarFilePath)

    /**
     *
     * @param className
     * @param classFile
     */
    abstract void processDirClass(String className, File classFile)

    /**
     * 处理jar中的类
     *
     *       InputStream inputStream = jarFile.getInputStream(jarEntry)
     *       ClassReader classReader = new ClassReader(IOUtils.toByteArray(inputStream))
     *       ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
     *
     * @param jarFile 整个jar
     * @param jarEntry 相当于 classFile
     */
    abstract void processJarClass(JarFile jarFile, JarEntry jarEntry)

    abstract void onTransformStart()

    abstract void onTransformEnd(TransformOutputProvider outputProvider)
}
