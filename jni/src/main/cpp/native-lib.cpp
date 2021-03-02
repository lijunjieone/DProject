#include <jni.h>
#include <string>
#include <android/log.h>

#define LOG_TAG "System.out"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)

extern "C" JNIEXPORT jstring JNICALL
Java_com_bn_jni_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


extern "C" JNIEXPORT jstring JNICALL
Java_com_bn_jni_MainActivity_stringFromJNI2(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from3 C++";
    LOGD("%s","haha");
    return env->NewStringUTF(hello.c_str());
}

extern "C"

JNIEXPORT jint JNICALL Java_com_bn_jni_MainActivity_add
        (JNIEnv * env, jobject, jint a, jint b) {
    LOGD("%s","haha add commond");
    return a+b;
}

