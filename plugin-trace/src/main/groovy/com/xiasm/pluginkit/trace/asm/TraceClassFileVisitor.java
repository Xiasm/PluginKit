package com.xiasm.pluginkit.trace.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class TraceClassFileVisitor extends ClassVisitor {
    private String className;
    private TraceMethodVisitor methodVisitor;

    public TraceClassFileVisitor(int api, ClassVisitor cv) {
        super(api, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        className = name.replace("/", ".");

    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        methodVisitor = new TraceMethodVisitor(Opcodes.ASM5, mv, access, name, desc);
        return methodVisitor;
    }
}
