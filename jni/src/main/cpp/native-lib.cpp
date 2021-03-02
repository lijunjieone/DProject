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
Java_com_bn_jni_MainActivity_callJavaInstanceMethod3(JNIEnv *env, jobject thiz, jstring from,
                                                     jint index) {
    jclass clazz = NULL;
    jobject jobj = NULL;
    jmethodID mid_construct = NULL;
    jmethodID mid_instance = NULL;
    jstring str_arg = NULL;
    // 1、从classpath路径下搜索ClassMethod这个类，并返回该类的Class对象
    clazz = env->FindClass("com/bn/jni/ClassMethod");
    if (clazz == NULL) {
        printf("找不到'com.study.jnilearn.ClassMethod'这个类");
        std::string error = "";
        return env->NewStringUTF(error.c_str());

    }

    // 2、获取类的默认构造方法ID
    mid_construct = env->GetMethodID(clazz, "<init>","()V");
    if (mid_construct == NULL) {
        printf("找不到默认的构造方法");
        std::string error = "";
        return env->NewStringUTF(error.c_str());

    }

    // 3、查找实例方法的ID
    mid_instance = env->GetMethodID( clazz, "callInstanceMethod", "(Ljava/lang/String;I)Ljava/lang/String;");
    if (mid_instance == NULL) {

        std::string error = "";
        return env->NewStringUTF(error.c_str());
    }

    // 4、创建该类的实例
    jobj = env->NewObject(clazz,mid_construct);
    if (jobj == NULL) {
        printf("在com.bn.jni.ClassMethod类中找不到callInstanceMethod方法");
        std::string error = "";
        return env->NewStringUTF(error.c_str());

    }

    // 5、调用对象的实例方法
    str_arg = env->NewStringUTF("我是实例方法");
    from = (jstring )env->CallObjectMethod(jobj,mid_instance,str_arg,200);

    // 删除局部引用
    env->DeleteLocalRef(clazz);
    env->DeleteLocalRef(jobj);
    env->DeleteLocalRef(str_arg);
    return env->NewStringUTF(jstring2string(env,from).c_str());

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

