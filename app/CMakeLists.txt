
cmake_minimum_required(VERSION 3.4.1)

add_library( # 为library起名字
             native-lib
             # 设置为SHARE类型，STATIC为需要加载.a文件
             SHARED
             # 需要编译的源代码文件目录
             src/main/jni/encrypt.c)
set(CURRENT_DIR ${CMAKE_SOURCE_DIR})
#set_target_properties( native-lib
#                    PROPERTIES IMPORTED_LOCATION
#                    ${CURRENT_DIR}/libs/armeabi-v7a/libnative-lib.so )


#在Gradle Console输出信息
#CMAKE_SOURCE_DIR: E:/MyApplication2/app
message("CURRENT_DIR:" ${CMAKE_SOURCE_DIR})

add_library(
             # 为library起名字
             avcodec-57
             #avcodec-lib
             SHARED
             IMPORTED ) # 不需要编译，由外部导入
set_target_properties( #avcodec-lib
                       avcodec-57
                       PROPERTIES IMPORTED_LOCATION
                       #${CURRENT_DIR}/libs/armeabi-v7a/libavcodec-lib.so
                       ${CURRENT_DIR}/libs/armeabi-v7a/libavcodec-57.so)#配置导入文件的路径
add_library( avfilter-6
             #avfilter-lib
             SHARED
             IMPORTED )
set_target_properties( #avfilter-lib
                       avfilter-6
                       PROPERTIES IMPORTED_LOCATION
                       #${CURRENT_DIR}/libs/armeabi-v7a/libavfilter-lib.so
                       ${CURRENT_DIR}/libs/armeabi-v7a/libavfilter-6.so)
add_library( avformat-57
             #avformat-lib
             SHARED
             IMPORTED )
set_target_properties( #avformat-lib
                       avformat-57
                       PROPERTIES IMPORTED_LOCATION
                       #${CURRENT_DIR}/libs/armeabi-v7a/libavformat-lib.so
                       ${CURRENT_DIR}/libs/armeabi-v7a/libavformat-57.so)
add_library( avutil-55
             #avutil-lib
             SHARED
             IMPORTED )
set_target_properties( #aavutil-lib
                       avutil-55
                       PROPERTIES IMPORTED_LOCATION
                       #${CURRENT_DIR}/libs/armeabi-v7a/libavutil-lib.so
                       ${CURRENT_DIR}/libs/armeabi-v7a/libavutil-55.so)
add_library( swresample-2
             #swresample-lib
             SHARED
             IMPORTED )
set_target_properties( #swresample-lib
                       swresample-2
                       PROPERTIES IMPORTED_LOCATION
                       #${CURRENT_DIR}/libs/armeabi-v7a/libswresample-lib.so
                       ${CURRENT_DIR}/libs/armeabi-v7a/libswresample-2.so)
add_library( swscale-4
             #swscale-lib
             SHARED
             IMPORTED )
set_target_properties( #swscale-lib
                       swscale-4
                       PROPERTIES IMPORTED_LOCATION
                       #${CURRENT_DIR}/libs/armeabi-v7a/libswscale-lib.so
                        ${CURRENT_DIR}/libs/armeabi-v7a/libswscale-4.so)
include_directories( libs/include )#外部库需要的头文件
find_library( # Sets the name of the path variable.
              log-lib
              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )#加载NDK上的library
# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.
#将需要的库文件进行链接
target_link_libraries( # Specifies the target library.
                       native-lib
                       #avcodec-lib
                       avcodec-57
                       #avfilter-lib
                       avfilter-6
                       #avformat-lib
                       avformat-57
                       #avutil-lib
                       avutil-55
                       #swresample-lib
                       swresample-2
                       #swscale-lib
                       swscale-4
                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )