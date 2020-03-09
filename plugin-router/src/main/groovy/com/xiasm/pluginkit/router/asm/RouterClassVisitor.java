package com.xiasm.pluginkit.router.asm;

import com.xiasm.pluginkit.base.Logger;
import com.xiasm.pluginkit.common.annotation.Router;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class RouterClassVisitor extends ClassVisitor {
    private String className;
    private RouterAnnotationVisitor routerAnnotationVisitor;
    private boolean isRouterClass = false;

    public RouterClassVisitor(int api, ClassVisitor cv) {
        super(api, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        className = name.replace("/", ".");
        Logger.d("RouterClassVisitor visit :" + className);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        AnnotationVisitor annotationVisitor = super.visitAnnotation(desc, visible);
        if (Type.getDescriptor(Router.class).equals(desc)) {
            isRouterClass = true;
            routerAnnotationVisitor = new RouterAnnotationVisitor(Opcodes.ASM5, annotationVisitor);
            return routerAnnotationVisitor;
        }
        return annotationVisitor;
    }



    @Override
    public void visitEnd() {
        super.visitEnd();
        Logger.d("=======RouterClassVisitor visitEnd :" + className + "=========");
        Logger.d(".");
        Logger.d(".");
        Logger.d(".");
    }

    public boolean isRouteClass() {
        return isRouterClass;
    }

    public String getRouteClassName() {
        return className;
    }

    public String getRouteAnnotationValue() {
        if (routerAnnotationVisitor != null) {
            return routerAnnotationVisitor.annotationValue;
        }
        return null;
    }
}
