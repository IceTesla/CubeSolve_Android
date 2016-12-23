/*
 * Utils.h
 *
 *  Created on: Jan 11, 2016
 *      Author: corvo
 */


#ifndef UTILS_H_
#define UTILS_H_


#include "opencv/cv.h"
#include "opencv/highgui.h"
#include "opencv2/core/core.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include "opencv2/features2d/features2d.hpp"
#include "math.h"
#include "MyCvPoint.h"
#include "PointMatrix.h"

using namespace cv;

#ifdef __cplusplus
extern "C" {
#endif



//angle函数用来返回（两个向量之间找到角度的余弦值）
double angle(CvPoint* pt1, CvPoint* pt2, CvPoint* pt0);

// 寻找正方形
CvSeq* findSquares4(IplImage *img, CvMemStorage* stroage);

void drawSquares(IplImage* img, CvSeq* squares, PointMatrix &mat);

#ifdef __cplusplus
}
#endif
#endif /* UTILS_H_ */


