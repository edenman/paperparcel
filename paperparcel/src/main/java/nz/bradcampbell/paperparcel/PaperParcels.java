package nz.bradcampbell.paperparcel;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Central mechanism for {@link #wrap(Object)}ing/{@link #unwrap(TypedParcelable)}ping arbitrary
 * PaperParcel objects.
 */
public final class PaperParcels {
  static final Map<Class, Delegator> FROM_ORIGINAL;
  static final Map<Class, Delegator> FROM_PARCELABLE;

  static {
    //noinspection TryWithIdenticalCatches
    try {
      Class clazz = Class.forName("nz.bradcampbell.paperparcel.PaperParcelMapping");
      FROM_ORIGINAL = getFieldValue(clazz, "FROM_ORIGINAL");
      FROM_PARCELABLE = getFieldValue(clazz, "FROM_PARCELABLE");
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> TypedParcelable<T> wrap(T originalObj) {
    Class<?> type = originalObj.getClass();
    //noinspection unchecked
    Delegator<T, TypedParcelable<T>> delegator = FROM_ORIGINAL.get(type);
    return delegator.wrap(originalObj);
  }

  public static <T> T unwrap(TypedParcelable<T> parcelableObj) {
    Class<?> type = parcelableObj.getClass();
    //noinspection unchecked
    Delegator<T, TypedParcelable<T>> delegator = FROM_PARCELABLE.get(type);
    return delegator.unwrap(parcelableObj);
  }

  interface Delegator<ORIG, PARCEL extends TypedParcelable<ORIG>> {
    ORIG unwrap(PARCEL parcelableObj);

    TypedParcelable<ORIG> wrap(ORIG originalObj);
  }

  private static Map<Class, Delegator> getFieldValue(Class clazz, String fieldName)
      throws NoSuchFieldException, IllegalAccessException {
    Field field = clazz.getDeclaredField(fieldName);
    field.setAccessible(true);
    //noinspection unchecked
    return (Map<Class, Delegator>) field.get(null);
  }
}
