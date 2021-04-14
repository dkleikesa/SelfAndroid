package com.android.plugindemo;

import com.android.build.gradle.AppExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class MyClass implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getExtensions().findByType(AppExtension.class).registerTransform(new TransformDemo());

    }
}
