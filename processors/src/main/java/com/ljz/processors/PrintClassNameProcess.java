package com.ljz.processors;

import com.ljz.annotations.ClassPrint;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * Created by jianzhang.ljz on 2017/11/13.
 */

@SupportedAnnotationTypes("com.ljz.annotations.ClassPrint")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class PrintClassNameProcess extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Messager messager = processingEnv.getMessager();

        messager.printMessage(Diagnostic.Kind.NOTE, "Printing:~~~~~~~~~~~~~~~~START");
        for (TypeElement te : set) {
            for (Element e : roundEnvironment.getElementsAnnotatedWith(te)) {
                messager.printMessage(Diagnostic.Kind.NOTE, "String: " + e.toString());
                messager.printMessage(Diagnostic.Kind.NOTE, "getKind: " + e.getKind().toString());
                messager.printMessage(Diagnostic.Kind.NOTE, "value: " + e.getAnnotation(ClassPrint.class).value());

            }
        }

        messager.printMessage(Diagnostic.Kind.NOTE, "Printing:~~~~~~~~~~~~~~~~FINISH");
        return true;
    }
}
