// Created by jiangwei1-g on 2016/5/23.
#include "encrypt.h"
#include "cn_wjdiankong_encryptdemo_MainActivity.h"
#include <string.h>
#include <pthread.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/ptrace.h>

const int handledSignals[] = { SIGSEGV, SIGABRT, SIGFPE, SIGILL, SIGBUS };
const int handledSignalsNum = sizeof(handledSignals) / sizeof(handledSignals[0]);
struct sigaction old_handlers[ sizeof(handledSignals) / sizeof(handledSignals[0]) ];

void my_sigaction(int signal, siginfo_t *info, void *reserved) {
    LOGD("signal:%d",signal);
}

char key_src[] = {'z','y','t','y','r','T','R','A','*','B','n','i','q','C','P','p','V','s'};

int is_number(const char* src){
    if(src == NULL){
        return 0;
    }
    while(*src != '\0'){
        if(*src < 48 || *src > 57){
            return 0;
        }
        src++;
    }
    return 1;
}

char * get_encrypt_str(const char* src){
    if(src == NULL){
        return NULL;
    }
    int len = strlen(src);
    len++;
    char *dest = (char*) malloc(len * sizeof(char));
    char *head = dest;
    char *tmp = src;
    int i=0;
    int key_len = 18;
    for(;i<len;i++){
        int index = (*tmp) - 48;
        if(index == 0){
            index = 1;
        }
        *dest = key_src[key_len-index];
        tmp++;
        dest++;
    }
    dest++;
    *dest = '\0';
    return head;
}

JNIEXPORT jint
JNICALL
int jiangwei (JNIEnv * env, jobject obj, jstring str)
{
    LOGD("JNIEnv1:%p", env);
    const char *strAry = (*env)->GetStringUTFChars(env, str, 0);
    int len = strlen(strAry);
    char* dest = (char*)malloc(len);
    strcpy(dest, strAry);

    int number = is_number(strAry);
    if(number == 0){
        return 0;
    }

    char* encry_str = get_encrypt_str(strAry);
    const char* pas = "ssBCqpBssP";
    int result = strcmp(pas, encry_str);

    (*env)->ReleaseStringUTFChars(env, str, strAry);

    if(result == 0){
        return 1;
    }else{
        return 0;
    }

}

pthread_t t_id;

//获取TracePid
int getnumberfor_str(char* str){
    if(str == NULL){
        return -1;
    }
    char result[20];
    int count = 0;
    while(*str != '\0'){
        if(*str >= 48 && *str <= 57){
            result[count] = *str;
            count++;
        }
        str++;
    }
    int val = atoi(result);
    return val;
}

//开启循环轮训检查TracePid字段
void thread_fuction() {
    int pid = getpid();
    char file_name[20] = {'\0'};
    //读取status文件
    sprintf(file_name, "/proc/%d/status",pid);
    char linestr[256];
    int i=0, traceid;
    FILE *fp;
    while(1){
        i = 0;
        fp = fopen(file_name,"r");
        if(fp == NULL){
            break;
        }
        while(!feof(fp)){
            fgets(linestr, 256, fp);
            //TracerPid是在第6行
            if(i == 5){
                traceid = getnumberfor_str(linestr);
                LOGD("traceId:%d", traceid);
                if(traceid > 0){
                    LOGD("I was be traced...trace pid:%d",traceid);
                    exit(0);
                }
                break;
            }
            i++;
        }
        fclose(fp);
        sleep(5);
    }

}

void create_thread_check_traceid(){
    /**
     * pthread_create：创建一个线程
     * t_id:线程id
     * thread_fuction：线程启动后调用，开始轮循检测TracerPid
     */
    int err = pthread_create(&t_id,NULL, thread_fuction, NULL);
    if(err != 0 ) {
        LOGD("create thread fail: %s\n",strerror(err));
    }
}

/**
 * 签名校验函数
 * 只能用静态方式破解
 */
const char *app_signature= "308201dd30820146020101300d06092a864886f70d010105050030373116301406035504030c0d416e64726f69642044656275673110300e060355040a0c07416e64726f6964310b3009060355040613025553301e170d3138313030383036343833335a170d3438303933303036343833335a30373116301406035504030c0d416e64726f69642044656275673110300e060355040a0c07416e64726f6964310b300906035504061302555330819f300d06092a864886f70d010101050003818d00308189028181008d426f4f027b07dc41c78d696f9d2a955d786d4b55062105d718b7e564e1d9186d2458687d539524a2f31c79ff72325f4c0b9bc60131899db0e1c14cd3ebfec52f12180a3d92ad8025a43ca63bffcde8201fac0ddd9c152c96d3b1761d76c126fc97886b84e8875ef310bf9f5b5ccff939ab238e11cab215f2f3b35f345528530203010001300d06092a864886f70d0101050500038181001cb6f387246b9d6fca1f07f286d529bb6d03c7daa79f9b044e60f27efd44d500949e4cf632435f8f3d290e11908fd6a31afec1e1d65c61368142e8321e0e849e9b6ad7e29dddb383b87a7d6a8abeffb168c62b2fc794d8620b697ac4622e4f611af30a128f51740c00a0508381d4e30b4eddac8916b90e46fa1bbcd3c7813f9c";
int equal_sign(JNIEnv* env){
    //调用Java层的Utils中的获取签名的方法
    char* className = "com/myself/collection/utils/SignatureValidation";
    jclass clazz = (*env)->FindClass(env, className);
    if (clazz == NULL) {
        LOGD("not find class '%s'", className);
        return 1;
    }

    LOGD("class name:%p", clazz);
    //获取Java层的函数
    //TODO  调用方法，传的参数类型有问题
    jmethodID method = (*env)->GetStaticMethodID(env, clazz, "getSignatureSHA1", "()Ljava/lang/String;");
    if(method == NULL){
        LOGD("not find method '%s'", method);
        return 1;
    }

    //调用Java层的函数
    jstring obj = (jstring)(*env)->CallStaticObjectMethod(env, clazz,method);
    if(obj == NULL){
        LOGD("method invoke error:%p", obj);
        return 1;
    }

    const char *strAry = (*env)->GetStringUTFChars(env, obj, 0);
    int cmpval = strcmp(strAry, app_signature);
    if(cmpval == 0){
        return 1;
    }

    (*env)->ReleaseStringUTFChars(env, obj, strAry);

    return 0;

}

//定义目标类名称
static const char *className = "com/myself/collection/activity/MainActivity";

//定义方法隐射关系
/**
 * 参数一:isEnquals是Java中的函数名
 * 参数二:(Ljava/lang/String;)Z描述函数的参数类型和返回值
 * 参数三:(void*)*jiangwei函数指针，指向C函数
 */
static JNINativeMethod methods[] = {
        {"isEquals", "(Ljava/lang/String;)Z", (int*)jiangwei},
};

jint JNI_OnLoad(JavaVM* vm, void* reserved){
    LOGD("JNI on load...");

    //自己附加
    LOGD("ptrace myself...");
    ptrace(PTRACE_TRACEME, 0, 0, 0);

    //声明变量
    jint result = JNI_ERR;
    JNIEnv* env = NULL;
    jclass clazz;
    int methodsLenght;

    //检测自己有没有被trace
    //create_thread_check_traceid();

    //获取JNI环境对象
    if ((*vm)->GetEnv(vm, (void**) &env, JNI_VERSION_1_4) != JNI_OK) {
        LOGD("ERROR: GetEnv failed\n");
        return JNI_ERR;
    }

    //签名校验
   /* LOGD("JNIEnv:%p", env);
    LOGD("start equal signature...");
    int check_sign = equal_sign(env);
    LOGD("check_sign:%d", check_sign);
    if(check_sign == 0){
        exit(0);
    }*/

    //注册本地方法.Load 目标类
    clazz = (*env)->FindClass(env,className);
    if (clazz == NULL) {
        LOGD("Native registration unable to find class '%s'", className);
        return JNI_ERR;
    }

    //建立方法隐射关系
    //取得方法长度
    methodsLenght = sizeof(methods) / sizeof(methods[0]);
    //RegisterNatives：手动注册native方法
    if ((*env)->RegisterNatives(env,clazz, methods, methodsLenght) < 0) {
        LOGD("RegisterNatives failed for '%s'", className);
        return JNI_ERR;
    }

    result = JNI_VERSION_1_4;
    return result;
}

//onUnLoad方法，在JNI组件被释放时调用
void JNI_OnUnload(JavaVM* vm, void* reserved){
    LOGD("JNI unload...");
}