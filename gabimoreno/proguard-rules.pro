### Crashlytics
-keep public class * extends java.lang.Exception
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**
-keep class com.google.firebase.crashlytics.** { *; }
-dontwarn com.google.firebase.crashlytics.**

### Crash reports
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
