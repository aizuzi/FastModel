package com.zuzi.fastmodel.complier;

import java.util.List;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.type.TypeMirror;

/**
 *  @author liyi
 *  create at 2018/5/30
 **/
public class AnnotatedField {

  TypeMirror type;

  String name;

  boolean mIsStaticField;

  List<? extends AnnotationMirror> annotationMirrors;


  public TypeMirror getType() {
    return type;
  }

  public void setType(TypeMirror type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean ismIsStaticField() {
    return mIsStaticField;
  }

  public void setmIsStaticField(boolean mIsStaticField) {
    this.mIsStaticField = mIsStaticField;
  }

  public List<? extends AnnotationMirror> getAnnotationMirrors() {
    return annotationMirrors;
  }

  public void setAnnotationMirrors(
      List<? extends AnnotationMirror> annotationMirrors) {
    this.annotationMirrors = annotationMirrors;
  }
}
