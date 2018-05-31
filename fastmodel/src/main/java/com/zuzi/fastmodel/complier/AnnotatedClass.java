package com.zuzi.fastmodel.complier;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * @author liyi
 * create at 2018/5/30
 **/
public final class AnnotatedClass {
  private final TypeElement mEnclosingClass;
  private final List<AnnotatedField> mFields;
  private final List<ExecutableElement> mMethods;
  private final boolean mIsBuilder;

  public AnnotatedClass(final TypeElement pEnclosingClass, boolean mIsBuilder) {
    this(pEnclosingClass, new ArrayList<ExecutableElement>(), mIsBuilder);
  }

  public AnnotatedClass(final TypeElement pEnclosingClass, final List<ExecutableElement> pMethods,
      boolean mIsBuilder) {
    mEnclosingClass = pEnclosingClass;
    mMethods = pMethods;
    this.mIsBuilder = mIsBuilder;
    mFields = new ArrayList<>();
  }

  public TypeElement enclosingClass() {
    return mEnclosingClass;
  }

  public void addField(final AnnotatedField pMethod) {
    mFields.add(pMethod);
  }

  public void addMethod(final ExecutableElement pMethod) {
    mMethods.add(pMethod);
  }

  public void writeInto(final Filer pFiler, final Messager pMessager, final String lPackageName) {
    //final GeneratedClass lGeneratedClass =
    //    new GeneratedClass(mEnclosingClass, lPackageName, mMethods, mFields);

    TypeSpec lTypeSpecGeneratedClass = null;

    if (mIsBuilder) {
      lTypeSpecGeneratedClass =
          new GeneratedBuilderClass(mEnclosingClass, lPackageName, mMethods, mFields).buildFieldTypeSpec();
    } else {
      lTypeSpecGeneratedClass =
          new GeneratedClass(mEnclosingClass, lPackageName, mMethods, mFields).buildFieldTypeSpec();
    }

    // create generated class to a file
    try {
      JavaFile.builder(lPackageName, lTypeSpecGeneratedClass)
          .build()
          .writeTo(pFiler);
    } catch (IOException pE) {
      logError(pMessager, mEnclosingClass, "error while writing generated class");
    }
  }

  private void logError(final Messager pMessager, final Element pElement, final String pMessage,
      final Object... pArgs) {
    pMessager.printMessage(Diagnostic.Kind.ERROR, String.format(pMessage, pArgs), pElement);
  }
}
