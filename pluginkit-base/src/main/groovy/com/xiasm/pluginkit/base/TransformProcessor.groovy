package com.xiasm.pluginkit.base

import com.android.build.api.transform.*
import com.google.common.io.Files
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils

import java.util.jar.JarEntry
import java.util.jar.JarFile

public class TransformProcessor {
    public static final String CLASS_FUFFIX = ".class"
    public static final String JAR_SUFFIX = ".jar"

    private AsmBaseTransform transform

    TransformProcessor(AsmBaseTransform transform) {
        this.transform = transform
    }

    public void processDir(TransformOutputProvider outputProvider,
                    DirectoryInput directoryInput,
                    boolean isIncremental) {
        if (isIncremental) {
            transformDirWithIncremental(outputProvider, directoryInput)
        } else {
            transformDirNoIncremental(outputProvider, directoryInput)
        }
    }

    public void processJar(TransformOutputProvider outputProvider,
                           JarInput jarInput,
                           boolean isIncremental) {
        if (isIncremental) {
            transformJarWithIncremental(outputProvider, jarInput)
        } else {
            transformJarNoIncremental(outputProvider, jarInput)
        }
    }

    private String destJarName(JarInput jarInput) {
        String jarName = jarInput.name
        def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
        if (jarName.endsWith(JAR_SUFFIX)) {
            jarName = jarName.substring(0, jarName.length() - 4)
        }
        return jarName + md5Name
    }

    private void transformJarNoIncremental(TransformOutputProvider outputProvider, JarInput jarInput) {

        String filePath = jarInput.file.getAbsolutePath()
        if (filePath != null && filePath.endsWith(JAR_SUFFIX) && !transform.isWhiteListJar(filePath)) {
            JarFile jarFile = new JarFile(jarInput.file)
            Enumeration enumeration = jarFile.entries()
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) enumeration.nextElement()
                String entryName = jarEntry.name
                if (entryName.endsWith(CLASS_FUFFIX) && !transform.isWhiteListClass(entryName)) {
                    transform.processJarClass(jarFile, jarEntry)
                }
            }
            try {
                jarFile.close()
            } catch(IOException e) {}
        }

        File dest = outputProvider.getContentLocation(
                destJarName(jarInput),
                jarInput.getContentTypes(),
                jarInput.getScopes(),
                Format.JAR)

        //copy file
        FileUtils.copyFile(jarInput.file, dest)
    }

    private void transformJarWithIncremental(TransformOutputProvider outputProvider, JarInput jarInput) {
        File dest = outputProvider.getContentLocation(
                destJarName(jarInput),
                jarInput.getContentTypes(),
                jarInput.getScopes(),
                Format.JAR)

        Status status = jarInput.getStatus()
        switch (status) {
            case Status.NOTCHANGED:
                break
            case Status.REMOVED:
                if (dest.exists()) {
                    FileUtils.forceDelete(dest);
                }
                break
            case Status.CHANGED:
            case Status.ADDED:
                transformJarNoIncremental(outputProvider, jarInput)
                break
        }

    }

    private void transformDirNoIncremental(TransformOutputProvider outputProvider, DirectoryInput directoryInput) {
        if (directoryInput.file.isDirectory()) {
            directoryInput.file.eachFileRecurse { File file ->
                String fileName = file.name
                if (fileName != null && fileName.endsWith(CLASS_FUFFIX)) {
                    if (!transform.isWhiteListClass(fileName)) {
                        transform.processDirClass(fileName, file)
                    }
                }
            }
        }

        File dest = outputProvider.getContentLocation(
                directoryInput.name,
                directoryInput.contentTypes,
                directoryInput.scopes,
                Format.DIRECTORY)
        FileUtils.forceMkdir(dest);
        //copy file
        FileUtils.copyDirectory(directoryInput.file, dest)
    }

    private void transformDirWithIncremental(TransformOutputProvider outputProvider, DirectoryInput directoryInput) {
        File dest = outputProvider.getContentLocation(
                directoryInput.name,
                directoryInput.contentTypes,
                directoryInput.scopes,
                Format.DIRECTORY)
        FileUtils.forceMkdir(dest);

        String srcDirPath = directoryInput.file.getAbsolutePath()
        String destDirPath = dest.getAbsolutePath()
        Map<File, Status> fileStatusMap = directoryInput.changedFiles
        for (Map.Entry<File, Status> changedFile : fileStatusMap.entrySet()) {
            File inputFile = changedFile.getKey()
            Status status = changedFile.getValue()
            String destFilePath = inputFile.getAbsolutePath().replace(srcDirPath, destDirPath)
            File destFile = new File(destFilePath)
            switch (status) {
                case Status.NOTCHANGED:
                    break
                case Status.REMOVED:
                    if (destFile.exists()) {
                        destFile.delete()
                    }
                    break
                case Status.ADDED:
                case Status.CHANGED:
                    try {
                        FileUtils.touch(destFile)
                    } catch(Exception e) {
                        Files.createParentDirs(destFile)
                    }
                    processSingleFile(inputFile, destFile, srcDirPath)
                    break
            }
        }
    }

    private void processSingleFile(File inputFile, File outputFile, String inputBaseDir) {
        if (!inputBaseDir.endsWith(File.separator)) {
            inputBaseDir = inputBaseDir + File.separator
        }
        String fileSimpleName = inputFile.getAbsolutePath()
                .replace(inputBaseDir, "").replace(File.separator, ".")
        if (fileSimpleName != null && fileSimpleName.endsWith(CLASS_FUFFIX)) {
            //原则：先修改再拷贝
            if (!transform.isWhiteListClass(fileSimpleName)) {
                transform.processDirClass(fileSimpleName, inputFile)
//                FileUtils.touch(outputFile)
//                InputStream is = new FileInputStream(inputFile)
//                ClassReader classReader = new ClassReader(is)
//                ClassWriter classWriter = new ClassWriter(classLoader, ClassWriter.COMPUTE_MAXS)
//                ClassVisitor classWriterWrapper = wrapClassWriter(classWriter)
//                classReader.accept(classWriterWrapper, ClassReader.EXPAND_FRAMES)
//                byte[] bytes = classWriter.toByteArray()
//                FileOutputStream fos = new FileOutputStream(outputFile);
//                fos.write(bytes)
//                try {
//                    fos.close()
//                    is.close()
//                } catch (IOException e) {}
                println "tttttttt" + fileSimpleName
            }
            if (inputFile.isFile()) {
                FileUtils.touch(outputFile);
                FileUtils.copyFile(inputFile, outputFile);
            }
        }
    }

}
