package com.android.plugindemo;

import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.gradle.internal.pipeline.TransformManager;

import java.io.IOException;
import java.util.Set;

public class TransformDemo extends Transform {
    @Override
    public String getName() {
        return "TransformDemo";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    static void printCopyRight() {
        System.out.println();
        System.out.println("******************************************************************************");
        System.out.println("******                                                                  ******");
        System.out.println("******                欢迎使用 JokerWanTransform 编译插件               ******");
        System.out.println("******                                                                  ******");
        System.out.println("******************************************************************************");
        System.out.println();
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);
        printCopyRight();
    }

    @Override
    public boolean isIncremental() {
        return false;
    }
}
