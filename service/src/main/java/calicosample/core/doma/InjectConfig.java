package calicosample.core.doma;

import org.seasar.doma.AnnotateWith;
import org.seasar.doma.Annotation;
import org.seasar.doma.AnnotationTarget;

/**
 * Created by tasuku on 15/03/10.
 */
@AnnotateWith(annotations = {
  @Annotation(target = AnnotationTarget.CONSTRUCTOR, type = javax.inject.Inject.class),
  // とりあえず複数コンフィグファイルが必要になるまで使わない
//  @Annotation(target = AnnotationTarget.CONSTRUCTOR_PARAMETER, type = javax.inject.Named.class, elements = "\"config\"")
})
public @interface InjectConfig {
}
