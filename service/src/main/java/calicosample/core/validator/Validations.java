package calicosample.core.validator;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import jp.co.freemind.calico.core.endpoint.validation.FieldAccessor;
import jp.co.freemind.calico.core.media.Media;
import jp.co.freemind.calico.core.media.MediaProxy;

public class Validations {
  public static Predicate<FieldAccessor> instanceOf(Class<?> type) {
    return (field) -> type.isAssignableFrom(field.getResolvedType().getErasedType());
  }

  public static boolean notNull(FieldAccessor field, Object object) {
    return object != null;
  }

  public static boolean notEmpty(FieldAccessor field, Object object) {
    if (object instanceof Map) {
      return ((Map<?, ?>) object).size() > 0;
    }
    else if (object instanceof Collection) {
      return ((Collection<?>) object).size() > 0;
    }

    return true;
  }

  public static boolean lowerBound(FieldAccessor field, Object object) {
    Number number = toNumber(object);
    if (number == null) return true;

    LowerBound lowerBound = field.getAnnotation(LowerBound.class);
    if (lowerBound == null) return true;

    return number.doubleValue() >= lowerBound.value();
  }

  public static boolean upperBound(FieldAccessor field, Object object) {
    Number number = toNumber(object);
    if (number == null) return true;

    UpperBound upperBound = field.getAnnotation(UpperBound.class);
    if (upperBound == null) return true;

    return number.doubleValue() <= upperBound.value();
  }

  private static @Nullable Number toNumber(Object object) {
    if (object instanceof Number) {
      return (Number) object;
    }
    return null;
  }

  public static boolean allowedExtensions(FieldAccessor field, Object object) {
    if (!(object instanceof Media)) return true;

    Media media = (Media) object;

    AllowedExtensions extensions = field.getAnnotation(AllowedExtensions.class);
    if (extensions == null) return true;

    // nullの場合はOK、欠損がある場合はNGとする
    if (media.getMeta() == null) return false;
    if (media.getMeta().getName() == null) return false;
    return Arrays.stream(extensions.value())
      .map(ext -> "." + ext)
      .anyMatch(media.getMeta().getName()::endsWith);
  }

  public static boolean fileSize(FieldAccessor field, Object object) {
    if (!(object instanceof Media)) return true;
    Media media = (Media) object;

    FileSize fileSize = field.getAnnotation(FileSize.class);
    if (fileSize == null) return true;

    // nullの場合はOK、欠損がある場合はNGとする
    if (media.getMeta() == null) return false;
    if (media.getMeta().getSize() == null) return false;

    long size = media instanceof MediaProxy ? media.getMeta().getSize() : media.getPayload().length;
    return fileSize.upperBound() <= size && size <= fileSize.lowerBound();
  }

  public static boolean letterCount(FieldAccessor field, Object object) {
    if (!(object instanceof CharSequence)) return true;
    CharSequence sequence = (CharSequence) object;

    LetterCount letterCount = field.getAnnotation(LetterCount.class);
    if (letterCount == null) return true;

    int length = sequence.length();
    return letterCount.lowerBound() <= length && length <= letterCount.upperBound();
  }
}
