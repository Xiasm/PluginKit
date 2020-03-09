package com.xiasm.pluginkit.router.asm;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.HashMap;
import java.util.Map;

public class ASMCoder implements Opcodes {

    public static byte[] generateRouterInfoManager(String pkg, String javaFileSimpleName, String interfacePkg, HashMap<String, String> map) throws Exception {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        MethodVisitor mv;

        //生成这个类
        cw.visit(V1_7, ACC_PUBLIC + ACC_SUPER, pkg, null, "java/lang/Object", new String[]{interfacePkg});
        cw.visitSource("RouteMapping.java", null);

        {
            //构造方法
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }

        {
            //getRouterMap
            mv = cw.visitMethod(ACC_PUBLIC, "getRouterMap", "(Ljava/util/HashMap;)V", "(Ljava/util/HashMap<Ljava/lang/String;Ljava/lang/String;>;)V", null);
            mv.visitCode();
            if (map.size() > 0) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    mv.visitVarInsn(ALOAD, 1);
                    mv.visitLdcInsn(key);
                    mv.visitLdcInsn(value);
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false);
                    mv.visitInsn(POP);
                }
            }
            mv.visitInsn(RETURN);
            mv.visitMaxs(3, 2);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }

    public static byte[] dump() throws Exception {

        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av0;

        cw.visit(V1_7, ACC_PUBLIC + ACC_SUPER, "com/xiasm/pluginkit/router/core/RouteMapping", null, "java/lang/Object", new String[]{"com/xiasm/pluginkit/router/core/IRouteMapping"});

        cw.visitSource("RouteMapping.java", null);

        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(5, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv.visitInsn(RETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "Lcom/xiasm/pluginkit/router/core/RouteMapping;", null, l0, l1, 0);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "getRouterMap", "(Ljava/util/HashMap;)V", "(Ljava/util/HashMap<Ljava/lang/String;Ljava/lang/String;>;)V", null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(8, l0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitLdcInsn("aaa");
            mv.visitLdcInsn("bbb");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false);
            mv.visitInsn(POP);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(9, l1);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitLdcInsn("aaa");
            mv.visitLdcInsn("bbb");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false);
            mv.visitInsn(POP);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLineNumber(10, l2);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitLdcInsn("aaa");
            mv.visitLdcInsn("bbb");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false);
            mv.visitInsn(POP);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitLineNumber(11, l3);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitLdcInsn("aaa");
            mv.visitLdcInsn("bbb");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false);
            mv.visitInsn(POP);
            Label l4 = new Label();
            mv.visitLabel(l4);
            mv.visitLineNumber(12, l4);
            mv.visitInsn(RETURN);
            Label l5 = new Label();
            mv.visitLabel(l5);
            mv.visitLocalVariable("this", "Lcom/xiasm/pluginkit/router/core/RouteMapping;", null, l0, l5, 0);
            mv.visitLocalVariable("route", "Ljava/util/HashMap;", "Ljava/util/HashMap<Ljava/lang/String;Ljava/lang/String;>;", l0, l5, 1);
            mv.visitMaxs(3, 2);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }
}
