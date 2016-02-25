package nz.bradcampbell.paperparcel;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;

import com.google.auto.value.processor.AutoValueProcessor;
import com.google.common.base.Joiner;
import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import java.util.Arrays;
import javax.tools.JavaFileObject;

public class AutoValueTest {

  @Test public void globalDateTypeAdapterTest() throws Exception {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
        "package test;",
        "import com.google.auto.value.AutoValue;",
        "import android.os.Parcelable;",
        "import java.util.Date;",
        "@AutoValue",
        "public abstract class Test implements Parcelable {",
        "public abstract Date count();",
        "}"
    ));

    JavaFileObject dateTypeAdapter = JavaFileObjects.forSourceString("test.DateTypeAdapter", Joiner.on('\n').join(
        "package test;",
        "import android.os.Parcel;",
        "import nz.bradcampbell.paperparcel.GlobalTypeAdapter;",
        "import nz.bradcampbell.paperparcel.TypeAdapter;",
        "import org.jetbrains.annotations.NotNull;",
        "import java.util.Date;",
        "@GlobalTypeAdapter",
        "public class DateTypeAdapter implements TypeAdapter<Date> {",
        "@NotNull",
        "@Override",
        "public Date readFromParcel(@NotNull Parcel inParcel) {",
        "return new Date(inParcel.readLong());",
        "}",
        "@Override",
        "public void writeToParcel(@NotNull Date value, @NotNull Parcel outParcel) {",
        "outParcel.writeLong(value.getTime());",
        "}",
        "}"
    ));

    JavaFileObject expectedSource = JavaFileObjects.forSourceString("test/AutoValue_TestParcel", Joiner.on('\n').join(
        "package test;",
        "import android.os.Parcel;",
        "import android.os.Parcelable;",
        "import java.lang.Override;",
        "import java.util.Date;",
        "import nz.bradcampbell.paperparcel.TypedParcelable;",
        "public final class AutoValue_TestParcel implements TypedParcelable<Test> {",
        "public static final Parcelable.Creator<AutoValue_TestParcel> CREATOR = new Parcelable.Creator<AutoValue_TestParcel>() {",
        "@Override public AutoValue_TestParcel createFromParcel(Parcel in) {",
        "return new AutoValue_TestParcel(in);",
        "}",
        "@Override public AutoValue_TestParcel[] newArray(int size) {",
        "return new AutoValue_TestParcel[size];",
        "}",
        "};",
        "private final AutoValue_Test data;",
        "private AutoValue_TestParcel(AutoValue_Test data) {",
        "this.data = data;",
        "}",
        "private AutoValue_TestParcel(Parcel in) {",
        "DateTypeAdapter countTypeAdapter = new DateTypeAdapter();",
        "Date count = countTypeAdapter.readFromParcel(in);",
        "this.data = new AutoValue_Test(count);",
        "}",
        "public static final AutoValue_TestParcel wrap(AutoValue_Test data) {",
        "return new AutoValue_TestParcel(data);",
        "}",
        "public AutoValue_Test getContents() {",
        "return data;",
        "}",
        "@Override",
        "public int describeContents() {",
        "return 0;",
        "}",
        "@Override",
        "public void writeToParcel(Parcel dest, int flags) {",
        "Date count = data.count();",
        "DateTypeAdapter countTypeAdapter = new DateTypeAdapter();",
        "countTypeAdapter.writeToParcel(count, dest);",
        "}",
        "}"
    ));

    assertAbout(javaSources()).that(Arrays.asList(source, dateTypeAdapter))
        .processedWith(new PaperParcelProcessor(), new AutoValueProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(expectedSource);
  }

  @Test public void localDateTypeAdapterTest() throws Exception {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
        "package test;",
        "import nz.bradcampbell.paperparcel.FieldTypeAdapter;",
        "import com.google.auto.value.AutoValue;",
        "import android.os.Parcelable;",
        "import java.util.Date;",
        "@AutoValue",
        "public abstract class Test implements Parcelable {",
        "@FieldTypeAdapter(DateTypeAdapter.class)",
        "public abstract Date count();",
        "}"
    ));

    JavaFileObject dateTypeAdapter = JavaFileObjects.forSourceString("test.DateTypeAdapter", Joiner.on('\n').join(
        "package test;",
        "import android.os.Parcel;",
        "import nz.bradcampbell.paperparcel.TypeAdapter;",
        "import org.jetbrains.annotations.NotNull;",
        "import java.util.Date;",
        "public class DateTypeAdapter implements TypeAdapter<Date> {",
        "@NotNull",
        "@Override",
        "public Date readFromParcel(@NotNull Parcel inParcel) {",
        "return new Date(inParcel.readLong());",
        "}",
        "@Override",
        "public void writeToParcel(@NotNull Date value, @NotNull Parcel outParcel) {",
        "outParcel.writeLong(value.getTime());",
        "}",
        "}"
    ));

    JavaFileObject expectedSource = JavaFileObjects.forSourceString("test/AutoValue_TestParcel", Joiner.on('\n').join(
        "package test;",
        "import android.os.Parcel;",
        "import android.os.Parcelable;",
        "import java.lang.Override;",
        "import java.util.Date;",
        "import nz.bradcampbell.paperparcel.TypedParcelable;",
        "public final class AutoValue_TestParcel implements TypedParcelable<Test> {",
        "public static final Parcelable.Creator<AutoValue_TestParcel> CREATOR = new Parcelable.Creator<AutoValue_TestParcel>() {",
        "@Override public AutoValue_TestParcel createFromParcel(Parcel in) {",
        "return new AutoValue_TestParcel(in);",
        "}",
        "@Override public AutoValue_TestParcel[] newArray(int size) {",
        "return new AutoValue_TestParcel[size];",
        "}",
        "};",
        "private final AutoValue_Test data;",
        "private AutoValue_TestParcel(AutoValue_Test data) {",
        "this.data = data;",
        "}",
        "private AutoValue_TestParcel(Parcel in) {",
        "DateTypeAdapter countTypeAdapter = new DateTypeAdapter();",
        "Date count = countTypeAdapter.readFromParcel(in);",
        "this.data = new AutoValue_Test(count);",
        "}",
        "public static final AutoValue_TestParcel wrap(AutoValue_Test data) {",
        "return new AutoValue_TestParcel(data);",
        "}",
        "public AutoValue_Test getContents() {",
        "return data;",
        "}",
        "@Override",
        "public int describeContents() {",
        "return 0;",
        "}",
        "@Override",
        "public void writeToParcel(Parcel dest, int flags) {",
        "Date count = data.count();",
        "DateTypeAdapter countTypeAdapter = new DateTypeAdapter();",
        "countTypeAdapter.writeToParcel(count, dest);",
        "}",
        "}"
    ));

    assertAbout(javaSources()).that(Arrays.asList(source, dateTypeAdapter))
        .processedWith(new PaperParcelProcessor(), new AutoValueProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(expectedSource);
  }
}
