package com.xiasm.pluginkit.trace.asm;

import org.objectweb.asm.AnnotationVisitor;

public class TraceAnnotationVisitor extends AnnotationVisitor {
    public TraceAnnotationVisitor(int api, AnnotationVisitor av) {
        super(api, av);
    }
}
