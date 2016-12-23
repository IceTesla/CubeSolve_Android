#include <stdio.h>
#include <math.h>
#include <string.h>
#include <iostream>
#include <stdlib.h>

#include "cube_example_com_cube_JniUtils.h"
#include "MyCvPoint.h"
#include "Utils.h"
#include "PointMatrix.h"


extern "C" {

JNIEXPORT jstring JNICALL Java_cube_example_com_cube_JniUtils_findSquares
  (JNIEnv *env, jobject obj, jlong lg)
{
        Mat & mat = *(Mat*)lg;
        vector<KeyPoint> v;
        int len = mat.cols;
	PointMatrix mtx;

	CvMemStorage* storage = cvCreateMemStorage(0);
	IplImage* img0 = new IplImage(mat);
	IplImage* img = cvCloneImage(img0);

	drawSquares(img, findSquares4(img, storage), mtx);

	string str = BuiltResult(mtx);

	cvReleaseImage(&img);

        const char* s = str.data();
        //sprintf(s, "%d", len);
        return (env)->NewStringUTF(s);
}

}

