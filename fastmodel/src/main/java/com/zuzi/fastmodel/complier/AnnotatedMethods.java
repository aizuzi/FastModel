package com.zuzi.fastmodel.complier;

import com.zuzi.fastmodel.FastModel;
import com.zuzi.fastmodel.FastModelWithBuilder;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * @author liyi
 * create at 2018/5/30
 **/
public final class AnnotatedMethods {
  //region Fields
  private final RoundEnvironment mRoundEnvironment;
  //endregion

  //region Constructor
  public AnnotatedMethods(final RoundEnvironment pRoundEnvironment) {
    mRoundEnvironment = pRoundEnvironment;
  }
  //endregion

  //region Visible API
  public AnnotatedClasses enclosingClasses() {
    final Set<? extends Element> lElements =
        mRoundEnvironment.getElementsAnnotatedWith(FastModel.class);

    final AnnotatedClasses lAnnotatedClasses = new AnnotatedClasses();

    for (final Element lElement : lElements) {

      List<? extends Element> elementList = lElement.getEnclosedElements();

      for (Element item :
          elementList) {
        if (item.getKind().isField()) {

          final TypeElement lClass = (TypeElement) item.getEnclosingElement();
          TypeMirror resultType = item.asType();
          Name fieldName = item.getSimpleName();
          lAnnotatedClasses.appendFieldToClass(lClass, resultType, fieldName.toString(),
              item,false);
        }
      }
    }

    //Builder 模式
    final Set<? extends Element> buildElements =
        mRoundEnvironment.getElementsAnnotatedWith(FastModelWithBuilder.class);

    for (final Element lElement : buildElements) {

      List<? extends Element> elementList = lElement.getEnclosedElements();

      for (Element item :
          elementList) {
        if (item.getKind().isField()) {
          final TypeElement lClass = (TypeElement) item.getEnclosingElement();
          TypeMirror resultType = item.asType();
          Name fieldName = item.getSimpleName();
          lAnnotatedClasses.appendFieldToClass(lClass, resultType, fieldName.toString(),
              item,true);
        }
      }
    }

    return lAnnotatedClasses;
  }
  //endregion
}
