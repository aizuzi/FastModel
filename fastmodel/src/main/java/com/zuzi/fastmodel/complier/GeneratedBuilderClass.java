package com.zuzi.fastmodel.complier;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * @author liyi
 * create at 2018/5/30
 **/
public final class GeneratedBuilderClass {
  //region Fields
  private final TypeElement mClassElement;
  private final String packageName;
  private final List<ExecutableElement> mMethodElements;
  private final List<AnnotatedField> mFields;
  //endregion

  //region Constructor
  public GeneratedBuilderClass(final TypeElement pClassElement, final String packageName,
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
      //lClassBuilder.addMethod(
      //    generatedFieldAndMethod.buildSetMethod());
    }
    lClassBuilder.addMethod(buildToStringMethod());

    //构造Builder内部类
    final TypeSpec.Builder lClassBuilderBuilder = TypeSpec.classBuilder("Builder")
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);

    //构造方法
    final MethodSpec.Builder lBuilder =
        MethodSpec.constructorBuilder()
            .addParameter(ParameterSpec.builder(
                ClassName.get(packageName, lClassName.replace(packageName + ".", ""), "Builder"),
                "build").build())
            .addModifiers(Modifier.PUBLIC);

    for (final AnnotatedField annotatedField : mFields) {
      GeneratedFieldAndMethod generatedFieldAndMethod =
          new GeneratedFieldAndMethod(mClassElement, annotatedField);
      lClassBuilderBuilder.addField(
          generatedFieldAndMethod.buildField());

      lClassBuilderBuilder.addMethod(
          generatedFieldAndMethod.buildBuilderSetMethod(
              ClassName.get(packageName, lClassName.replace(packageName + ".", ""), "Builder")));
      //lClassBuilderBuilder.addMethod(
      //    generatedFieldAndMethod.buildGetMethod());

      lBuilder.addCode("this." + annotatedField.name + " = build." + annotatedField.name + " ;\n");
    }

    //构造Builder内部类返回值
    final MethodSpec.Builder returnBuilder =
        MethodSpec.methodBuilder("build")
            .returns(
                ClassName.get(packageName, lClassName.replace(packageName + ".", "")))
            .addModifiers(Modifier.PUBLIC);
    returnBuilder.addCode("return new " + lClassName.replace(packageName + ".", "") + "(this);\n");

    lClassBuilderBuilder.addMethod(returnBuilder.build());

    lClassBuilder.addType(lClassBuilderBuilder.build());
    lClassBuilder.addMethod(lBuilder.build());

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
    lBuilder.addCode(";\n");

    return lBuilder.build();
  }
}
