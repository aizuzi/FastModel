package com.zuzi.fastmodel.complier;

import java.util.ArrayList;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * @author liyi
 * create at 2018/5/30
 **/
public final class AnnotatedClasses extends ArrayList<AnnotatedClass> {

  //region Visible API
  public void appendMethodToClass(final TypeElement pClass, final ExecutableElement pMethod) {
    //annotatedClassForClass(pClass).addMethod(pMethod);
  }

  public void appendFieldToClass(final TypeElement pClass, final TypeMirror type, final String name,
      final Element item,final boolean isBuilder) {
    AnnotatedField annotatedField = new AnnotatedField();
    annotatedField.setName(name);
    annotatedField.setType(type);
    annotatedField.setmIsStaticField(item.getModifiers().contains(
        Modifier.STATIC));
    annotatedField.setAnnotationMirrors(item.getAnnotationMirrors());
    annotatedClassForClass(pClass,isBuilder).addField(annotatedField);
  }
  //endregion

  //region Specific job
  private AnnotatedClass annotatedClassForClass(final TypeElement pClass,final boolean isBuilder) {
    // get existing one
    for (final AnnotatedClass lAnnotatedClass : this) {
      if (lAnnotatedClass.enclosingClass() == pClass) {
        return lAnnotatedClass;
      }
    }
    // or create a new one if not exist
    final AnnotatedClass lAnnotatedClass = new AnnotatedClass(pClass,isBuilder);
    add(lAnnotatedClass);
    return lAnnotatedClass;
  }
  //endregion
}
