package com.xiasm.pluginkit.router.asm;

import com.xiasm.pluginkit.base.Logger;

import org.objectweb.asm.AnnotationVisitor;

public class RouterAnnotationVisitor extends AnnotationVisitor {
    public String annotationValue;

    public RouterAnnotationVisitor(int api, AnnotationVisitor av) {
        super(api, av);
    }

    @Override
    public void visit(String name, Object value) {
        super.visit(name, value);
        annotationValue = value.toString();
        Logger.d("RouterAnnotationVisitor visit start, annotation=" + annotationValue);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        Logger.d("RouterAnnotationVisitor visit end");
    }
}
