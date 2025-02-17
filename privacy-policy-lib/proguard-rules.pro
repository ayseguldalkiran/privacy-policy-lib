# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Kütüphaneyi koruma kuralları
-keep class com.example.privacy_policy_lib.** { *; }
-keep class javax.xml.stream.** { *; }
-keep class org.simpleframework.xml.** { *; }
-keep class retrofit2.converter.simplexml.** { *; }

# Gson ve Retrofit koruma kuralları
-keepattributes Signature
-keep class * implements java.io.Serializable { *; }
-keep class com.google.gson.** { *; }