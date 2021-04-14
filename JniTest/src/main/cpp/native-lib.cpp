#include <jni.h>
#include <string>
#include <stdio.h>
#include <dirent.h>
#include <inttypes.h>
#include <errno.h>
#include <android/log.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <map>

extern "C" {

#define LOG_TAG "LLJJZZ"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)  // 定义LOGD类型
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,  LOG_TAG, __VA_ARGS__)  // 定义LOGI类型
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,  LOG_TAG, __VA_ARGS__)  // 定义LOGW类型
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)  // 定义LOGE类型

#ifndef NELEM
#define NELEM(x) ((int) (sizeof(x) / sizeof((x)[0])))
#endif


 static const char* QTAGUID_IFACE_STATS = "/proc/net/xt_qtaguid/iface_stat_fmt";
 static const  char*  QTAGUID_UID_STATS = "/proc/net/xt_qtaguid/stats";

static const  uint64_t UNKNOWN = -1;

enum StatsType {
    RX_BYTES = 0,
    RX_PACKETS = 1,
    TX_BYTES = 2,
    TX_PACKETS = 3,
    TCP_RX_PACKETS = 4,
    TCP_TX_PACKETS = 5
};

typedef struct {
    uint64_t rxBytes;
    uint64_t rxPackets;
    uint64_t txBytes;
    uint64_t txPackets;
    uint64_t tcpRxPackets;
    uint64_t tcpTxPackets;
} Stats;

 uint64_t getStatsType(Stats* stats, StatsType type) {
    switch (type) {
        case RX_BYTES:
            return stats->rxBytes;
        case RX_PACKETS:
            return stats->rxPackets;
        case TX_BYTES:
            return stats->txBytes;
        case TX_PACKETS:
            return stats->txPackets;
        case TCP_RX_PACKETS:
            return stats->tcpRxPackets;
        case TCP_TX_PACKETS:
            return stats->tcpTxPackets;
        default:
            return UNKNOWN;
    }
}

 int parseIfaceStats(const char* iface, Stats* stats) {
    FILE *fp = fopen(QTAGUID_IFACE_STATS, "r");
//    FILE *fp = fopen("/proc/stat", "r");

    LOGE("parseIfaceStats fp:%d",fp);
    if (fp == NULL) {
        return -1;
    }
    LOGE("parseIfaceStats 11");
    char buffer[384];
    char cur_iface[32];
    bool foundTcp = false;
    uint64_t rxBytes, rxPackets, txBytes, txPackets, tcpRxPackets, tcpTxPackets;

    while (fgets(buffer, sizeof(buffer), fp) != NULL) {
        int matched = sscanf(buffer, "%31s %" SCNu64 " %" SCNu64 " %" SCNu64
                " %" SCNu64 " " "%*u %" SCNu64 " %*u %*u %*u %*u "
                "%*u %" SCNu64 " %*u %*u %*u %*u", cur_iface, &rxBytes,
                &rxPackets, &txBytes, &txPackets, &tcpRxPackets, &tcpTxPackets);

        LOGE("parseIfaceStats matched:%d %d",matched,rxBytes);
        if (matched >= 5) {
            if (matched == 7) {
                foundTcp = true;
            }
            if (!iface || !strcmp(iface, cur_iface)) {
                stats->rxBytes += rxBytes;
                stats->rxPackets += rxPackets;
                stats->txBytes += txBytes;
                stats->txPackets += txPackets;
                if (matched == 7) {
                    stats->tcpRxPackets += tcpRxPackets;
                    stats->tcpTxPackets += tcpTxPackets;
                }
            }
        }
    }

    if (!foundTcp) {
        stats->tcpRxPackets = UNKNOWN;
        stats->tcpTxPackets = UNKNOWN;
    }

    if (fclose(fp) != 0) {
        return -1;
    }
    return 0;
}

 jlong getTotalStat(JNIEnv* env, jclass clazz, jint type) {
    Stats stats = {};

    if (parseIfaceStats(NULL, &stats) == 0) {
        return getStatsType(&stats, (StatsType) type);
    } else {
        return UNKNOWN;
    }
}


JNIEXPORT jstring JNICALL
Java_autonavi_jnitest_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject  ojb ) {
    std::string hello = "Hello from C++";

   /* FILE *fp = fopen("/proc/net/xt_qtaguid/iface_stat_fmt", "r");

     LOGE("fp: %d",fp);
    if(fp !=NULL){
    char buffer[384];
    fgets(buffer, sizeof(buffer),fp);
    LOGE("%s",buffer);
    fclose(fp);
    }*/
//    getTotalStat(NULL,NULL,0);

    std::map<uint32_t ,Stats> mapResult;
    char buf[1000];

    sprintf(buf,"rx1:  %ld %ld",mapResult[10].rxBytes,mapResult[20].rxBytes);
    LOGE("%s",buf);
    mapResult[10].rxBytes+=300;
    mapResult[20].rxBytes+=300;
    sprintf(buf,"rx2:  %ld %ld",mapResult[10].rxBytes,mapResult[20].rxBytes);
    LOGE("%s",buf);


//    mapResult[10] =stats;
//    sprintf(buf,"rx3:  %d",mapResult[10].rxBytes);
//    LOGE("%s",buf);
    mapResult[10].rxBytes+=10;
    mapResult[20].rxBytes +=11;
    sprintf(buf,"rx3: %ld %ld %d %d",mapResult[10].rxBytes,mapResult[20].rxBytes,mapResult.size());
    LOGE("%s",buf);




    return env->NewStringUTF(hello.c_str());

}
}