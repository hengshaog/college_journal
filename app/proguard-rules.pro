# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/hengshao/Downloads/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-optimizationpasses 5

-dontskipnonpubliclibraryclassmembers

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-useuniqueclassmembernames

-renamesourcefileattribute SourceFile

-keepattributes SourceFile,LineNumberTable

-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Fragment

-dontnote junit.framework.**
-dontnote junit.runner.**
-dontwarn android.test.**
-dontwarn android.support.test.**
-dontwarn org.junit.**