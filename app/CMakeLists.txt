cmake_minimum_required(VERSION 3.4.1)

add_library(OtaUpdater SHARED src/main/cpp/OtaUpdater.cpp)

include_directories(src/main/cpp/)

find_library(log-lib log)

target_link_libraries(OtaUpdater ${log-lib})

