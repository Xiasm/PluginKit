package com.xiasm.pluginkit.trace.asm;

import org.objectweb.asm.AnnotationVisitor;

public class TraceAnnotationVisitor extends AnnotationVisitor {
    public String tag;

    public TraceAnnotationVisitor(int api, AnnotationVisitor av) {
        super(api, av);
    }

    @Override
    public void visit(String name, Object value) {
        super.visit(name, value);
        tag = value.toString();
    }
}
