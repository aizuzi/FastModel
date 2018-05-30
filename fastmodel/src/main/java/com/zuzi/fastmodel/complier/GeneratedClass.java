package com.zuzi.fastmodel.complier;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 *  @author liyi
 *  create at 2018/5/30
 **/
public final class GeneratedClass {
  //region Fields
  private final TypeElement mClassElement;
  private final String packageName;
  private final List<ExecutableElement> mMethodElements;
  private final List<AnnotatedField> mFields;
  //endregion

  //region Constructor
  public GeneratedClass(final TypeElement pClassElement, final String packageName,
      final List<ExecutableElement> pMethodElements, List<AnnotatedField> mFields) {
    mClassElement = pClassElement;
    mMethodElements = pMethodElements;
    this.packageName = packageName;
    this.mFields = mFields;
  }
  //endregion

  //region Visible API
  public TypeSpec buildFieldTypeSpec() {
    //final String lClassName = String.format("%s%s", mClassElement.getSimpleName(), FastModel.class.getSimpleName());
    final String lClassName = getClassName();
    final TypeSpec.Builder lClassBuilder = TypeSpec.classBuilder(lClassName)
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

    //创建Field
    //创建 get 和 set方法
    for (final AnnotatedField annotatedField : mFields) {
      GeneratedFieldAndMethod generatedFieldAndMethod =
          new GeneratedFieldAndMethod(mClassElement, annotatedField);
      lClassBuilder.addField(
          generatedFieldAndMethod.buildField());

      lClassBuilder.addMethod(
          generatedFieldAndMethod.buildGetMethod());
      lClassBuilder.addMethod(
          generatedFieldAndMethod.buildSetMethod());
    }
    lClassBuilder.addMethod(buildToStringMethod());
    return lClassBuilder.build();
  }

  public String getClassName() {

    final String lQualifiedName = mClassElement.getQualifiedName().toString();

    String name = lQualifiedName.replace(packageName, "");

    if (name.length() > 0) {
      //内部类转换为_
      name = name.substring(1).replace(".", "_");
    }

    return "Model_" + name;
  }
  //endregion

  private MethodSpec buildToStringMethod() {

    final MethodSpec.Builder lBuilder =
        MethodSpec.methodBuilder("toString")
            .returns(String.class)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addAnnotation(Override.class);

    lBuilder.addCode("return ");

    for (int i = 0, size = mFields.size(); i < size; i++) {
      AnnotatedField annotatedField = mFields.get(i);
      lBuilder.addCode("\"" + annotatedField.name + " = \"" + "  +" + annotatedField.name);
      if (i != size - 1) {
        lBuilder.addCode(" + ");
      }
    }
    lBuilder.addCode(";");

    return lBuilder.build();
  }
}
