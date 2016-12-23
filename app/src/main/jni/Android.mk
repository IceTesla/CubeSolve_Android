LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

OpenCV_INSTALL_MODULES := on
OpenCV_CAMERA_MODULES := off

OPENCV_LIB_TYPE := STATIC

include /home/corvo/Android/OpenCV-android-sdk/sdk/native/jni/OpenCV.mk

LOCAL_MODULE := JniUtils 

LOCAL_LDLIBS += -llog

LOCAL_SRC_FILES := JniUtils.cpp MyCvPoint.cpp PointMatrix.cpp Utils.cpp
LOCAL_C_INCLUDES := $(LOCAL)/include-all
LOCAL_C_INCLUDES += /home/corvo/Android/OpenCV-android-sdk/sdk/native/jni/include

include $(BUILD_SHARED_LIBRARY)
LOCAL_C_INCLUDES += external/stlport/stlport
LOCAL_C_INCLUDES += bionic

