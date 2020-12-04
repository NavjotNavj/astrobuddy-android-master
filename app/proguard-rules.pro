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

-keep class com.tozny.crypto.android.AesCbcWithIntegrity$PrngFixes$* { *; }

#Facebook SDK Proguard Rules
-keepattributes Signature
-keep class com.facebook.android.*
-keep class android.webkit.WebViewClient
-keep class * extends android.webkit.WebViewClient
-keepclassmembers class * extends android.webkit.WebViewClient {
    <methods>;
}

#Retrofit Proguard
# Platform calls Class.forName on types which do not exist on Android to determine platform.
#-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
#-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.**

#Calligraphy
-keep class uk.co.chrisjenx.calligraphy.* { *; }
-keep class uk.co.chrisjenx.calligraphy.*$* { *; }

-dontwarn afu.org.**
-dontwarn com.google.**
-dontwarn com.rilixtech.**
-dontwarn org.checkerframework.checker.**
-dontwarn org.joda.time.**


#For Razor Pay
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keepattributes JavascriptInterface
-keepattributes *Annotation*

-dontwarn com.razorpay.**
-keep class com.razorpay.** {*;}

-optimizations !method/inlining/*

-keepclasseswithmembers class * {
  public void onPayment*(...);
}

-dontwarn com.twitter.sdk.android.**
-dontwarn in.appnow.astrobuddy.**

#ripple animation
-keep class com.gauravbhola.ripplepulsebackground.RipplePulseLayout$RippleView { *; }

##Evernote Proguard
-dontwarn com.evernote.android.job.gcm.**
-dontwarn com.evernote.android.job.GcmAvailableHelper
-dontwarn com.evernote.android.job.work.**
-dontwarn com.evernote.android.job.WorkManagerAvailableHelper

-keep public class com.evernote.android.job.v21.PlatformJobService
-keep public class com.evernote.android.job.v14.PlatformAlarmService
-keep public class com.evernote.android.job.v14.PlatformAlarmReceiver
-keep public class com.evernote.android.job.JobBootReceiver
-keep public class com.evernote.android.job.JobRescheduleService
-keep public class com.evernote.android.job.gcm.PlatformGcmService
-keep public class com.evernote.android.job.work.PlatformWorker

#Firebase Realtime database proguard rules
 # Add this global rule
-keepattributes Signature

    # This rule will properly ProGuard all the model classes in
    # the package com.yourcompany.models. Modify to fit the structure
    # of your app.

-keep class in.appnow.astrobuddy.models.** { *; }

-keep class in.appnow.astrobuddy.rest.request.** { *; }

-keep class in.appnow.astrobuddy.rest.response.** { *; }

-keep class in.appnow.astrobuddy.conversation_module.rest_service.models.request.** { *; }

-keep class in.appnow.astrobuddy.conversation_module.rest_service.models.response.** { *; }
