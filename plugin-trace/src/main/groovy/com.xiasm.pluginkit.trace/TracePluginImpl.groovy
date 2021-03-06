package com.xiasm.pluginkit.trace

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project;

public class TracePluginImpl implements Plugin<Project> {
    @Override
    void apply(Project project) {
        def android = project.extensions.getByType(AppExtension);
        android.registerTransform(new TraceTransform(project))
    }


}
