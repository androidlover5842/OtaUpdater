#include <stdio.h>
#include <string>
#include <unistd.h>
#include <dirent.h>
#include <jni.h>
#include <bits/stdc++.h>
#include <android/log.h>
#include <iostream>
#include <stdexcept>

using namespace std;

#define  LOG_TAG    "FROM LIB"

string exec(const char* cmd) {
    char buffer[128];
    string result = "";
    FILE* pipe = popen(cmd, "r");
    if (!pipe) throw std::runtime_error("popen() failed!");
    try {
        while (!feof(pipe)) {
            if (fgets(buffer, 128, pipe) != NULL)
                result += buffer;
        }
    } catch (...) {
        pclose(pipe);
        throw;
    }
    pclose(pipe);
    return result;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_io_github_otaupdater_otalibary_util_ShellExecuter_RunCommand(JNIEnv *env, jobject instance,jstring command) {

    const char *convert = env->GetStringUTFChars(command, NULL);
    string co=exec(convert);
    return env->NewStringUTF(co.c_str());
}