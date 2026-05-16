# Crash 디버깅용 줄 번호 보존
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# KotlinX Serialization — R8이 serializer() companion 메서드를 제거하면 런타임 크래시 발생
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep @kotlinx.serialization.Serializable class ** { *; }
-keepclassmembers @kotlinx.serialization.Serializable class ** {
    *** Companion;
    *** INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}

# OkHttp 5.x / Okio — JVM 조건부 API 사용으로 인한 경고 제거
-dontwarn okhttp3.**
-dontwarn okio.**
