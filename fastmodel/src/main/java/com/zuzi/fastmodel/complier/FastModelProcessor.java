package com.zuzi.fastmodel.complier;

import com.google.auto.service.AutoService;
import com.zuzi.fastmodel.FastModel;
import com.zuzi.fastmodel.FastModelWithBuilder;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 *  @author liyi
 *  create at 2018/5/30
 **/
@AutoService(Processor.class)
public class FastModelProcessor extends AbstractProcessor {
  //region Fields
  private Messager mMessager;
  private Filer mFiler;
  private Elements mElements;

  @Override
  public synchronized void init(final ProcessingEnvironment pProcessingEnvironment) {
    super.init(pProcessingEnvironment);
    mMessager = pProcessingEnvironment.getMessager();
    mFiler = pProcessingEnvironment.getFiler();
    mElements = pProcessingEnvironment.getElementUtils();
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    final Set<String> lAnnotations = new HashSet<>();
    lAnnotations.add(FastModel.class.getCanonicalName());
    lAnnotations.add(FastModelWithBuilder.class.getCanonicalName());
    return lAnnotations;
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  public boolean process(final Set<? extends TypeElement> pSet,
      final RoundEnvironment pRoundEnvironment) {
    final AnnotatedClasses lAnnotatedClasses =
        new AnnotatedMethods(pRoundEnvironment).enclosingClasses();

    for (final AnnotatedClass lClass : lAnnotatedClasses) {
      PackageElement packageElement = mElements.getPackageOf(lClass.enclosingClass());
      String packageName = packageElement.getQualifiedName().toString();
      lClass.writeInto(mFiler, mMessager,packageName);
    }

    return false;
  }
}
