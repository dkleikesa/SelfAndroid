#include <jni.h>
#include <string>


JNIEXPORT jstring JNICALL
Java_autonavi_jnitest_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject  ojb ) {
    std::string hello = "Hello from C++";



    return env->NewStringUTF(hello.c_str());

}
