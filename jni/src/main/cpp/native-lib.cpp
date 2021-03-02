#include <jni.h>
#include <string>
#include <android/log.h>

#define LOG_TAG "System.out"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)

std::string jstring2string(JNIEnv *pEnv, jstring pJstring);

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

extern "C"
JNIEXPORT void JNICALL
Java_com_bn_jni_MainActivity_callJavaInstaceMethod(JNIEnv *env, jobject thiz) {

}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_bn_jni_MainActivity_callJavaStaticMethod2(JNIEnv *env, jobject thiz, jstring from,
                                                     jint index) {
    std::string hello = jstring2string(env,from);
    LOGD("%s","haha");
    return env->NewStringUTF(hello.c_str());
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_bn_jni_MainActivity_callJavaStaticMethod3(JNIEnv *env, jobject thiz, jstring from,
                                                     jint index) {

    jclass clazz = NULL;
    jstring str_arg = NULL;
    jmethodID mid_static_method;
    // 1、从classpath路径下搜索ClassMethod这个类，并返回该类的Class对象
    clazz = env->FindClass("com/bn/jni/ClassMethod");
    if (clazz == NULL) {
    }

    // 2、从clazz类中查找callStaticMethod方法
    mid_static_method = env->GetStaticMethodID(clazz,"callStaticMethod","(Ljava/lang/String;I)Ljava/lang/String;");
    if (mid_static_method == NULL) {
        printf("找不到callStaticMethod这个静态方法。");
    }

    // 3、调用clazz类的callStaticMethod静态方法
    str_arg = env->NewStringUTF("我是静态方法");
    from = (jstring)env->CallStaticObjectMethod(clazz,mid_static_method, str_arg, 100);

    // 删除局部引用
    env->DeleteLocalRef(clazz);
    env->DeleteLocalRef(str_arg);

    return env->NewStringUTF(jstring2string(env,from).c_str());

}

std::string jstring2string(JNIEnv *env, jstring jStr) {
    if (!jStr)
        return "";

    const jclass stringClass = env->GetObjectClass(jStr);
    const jmethodID getBytes = env->GetMethodID(stringClass, "getBytes", "(Ljava/lang/String;)[B");
    const jbyteArray stringJbytes = (jbyteArray) env->CallObjectMethod(jStr, getBytes, env->NewStringUTF("UTF-8"));

    size_t length = (size_t) env->GetArrayLength(stringJbytes);
    jbyte* pBytes = env->GetByteArrayElements(stringJbytes, NULL);

    std::string ret = std::string((char *)pBytes, length);
    env->ReleaseByteArrayElements(stringJbytes, pBytes, JNI_ABORT);

    env->DeleteLocalRef(stringJbytes);
    env->DeleteLocalRef(stringClass);
    return ret;
}

