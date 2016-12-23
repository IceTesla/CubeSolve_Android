/*
 * Utils.cpp
 *
 *  Created on: Jan 11, 2016
 *      Author: corvo
 */


#include "Utils.h"

#ifdef __cplusplus
extern "C" {
#endif
double angle(CvPoint* pt1, CvPoint* pt2, CvPoint* pt0)
{
	double dx1 = pt1->x - pt0->x;
	double dy1 = pt1->y - pt0->y;
	double dx2 = pt2->x - pt0->x;
	double dy2 = pt2->y - pt0->y;
	return (dx1 * dx2 + dy1 * dy2)
	                / sqrt(
	                                (dx1 * dx1 + dy1 * dy1)
	                                                * (dx2 * dx2 + dy2 * dy2)
	                                                + 1e-10);
}

CvSeq* findSquares4(IplImage *img, CvMemStorage* storage)
{
	CvSeq* contours;
	int i, c, l, N = 11;
	int thresh = 50;
	CvSize sz = cvSize(img->width & -2, img->height & -2);

	IplImage* timg = cvCloneImage(img);
	IplImage* gray = cvCreateImage(sz, 8, 1);
	IplImage* pyr = cvCreateImage(cvSize(sz.width / 2, sz.height / 2), 8, 3);
	IplImage* tgray;

	CvSeq* result;

	// 创建一个空序列用处储存轮廓角点
	CvSeq* squares = cvCreateSeq(0, sizeof(CvSeq), sizeof(CvPoint), storage);

	cvSetImageROI(timg, cvRect(0, 0, sz.width, sz.height));

	// 过滤噪音
	//cvPyrDown(timg, pyr, 7);
	tgray = cvCreateImage(sz, 8, 1);
	//cvPyrUp(pyr, timg, 7);

        // 红绿蓝3色分别提取
	for (int c = 0; c < 3; c++) {
		cvSetImageCOI(timg, c + 1);
		cvCopy(timg, tgray, 0);

                // 尝试各种阈值提取
		for (int l = 0; l < N; l++) {
			if (l == 0) {
				cvCanny(tgray, gray, 0, thresh, 5);
				cvDilate(gray, gray, 0, 1);
			} else {
				cvThreshold(tgray, gray, (l + 1) * 255 / N, 255,
				                CV_THRESH_BINARY);
			}

                        // 找到轮廓并存储在队列中
			cvFindContours(gray, storage, &contours,
			                sizeof(CvContour), CV_RETR_LIST,
			                CV_CHAIN_APPROX_SIMPLE, cvPoint(0, 0));


                        // 遍历每一个轮廓
			while (contours) {
                                // 使用指定的精度逼近多边形曲线
				result = cvApproxPoly(contours,
				                sizeof(CvContour), storage,
				                CV_POLY_APPROX_DP,
				                cvContourPerimeter(contours) * 0.02, 0);
				if (result->total == 4
				                && fabs(
				                                cvContourArea(
				                                                result,
				                                                CV_WHOLE_SEQ))
				                                > 500
				                && fabs(
				                                cvContourArea(
				                                                result,
				                                                CV_WHOLE_SEQ))
				                                < 100000
				                && cvCheckContourConvexity(
				                                result))
				{

					double s = 0, t;
					for (int i = 0; i < 5; i++) {
						if (i >= 2) {
							t =
							                fabs(
							                                angle(
							                                                (CvPoint*) cvGetSeqElem(
							                                                                result,
							                                                                i),
							                                                (CvPoint *) cvGetSeqElem(
							                                                                result,
							                                                                i
							                                                                                - 2),
							                                                (CvPoint *) cvGetSeqElem(
							                                                                result,
							                                                                i
							                                                                                - 1)));
							s = s > t ? s : t;
						}
					}

                                        // 如果余弦值足够小, 可以认定角度为90度, 是直角
					if (s < 0.08) {
						for (int i = 0; i < 4; i++) {
							cvSeqPush(squares,
							                (CvPoint *) cvGetSeqElem(
							                                result,
							                                i));
						}
					}
				}
				contours = contours->h_next;
			}

		}
	}

	cvReleaseImage(&gray);
	cvReleaseImage (&pyr);
	cvReleaseImage(&tgray);
	cvReleaseImage(&timg);

	return squares;
}

void drawSquares(IplImage* img, CvSeq* squares, PointMatrix &mat)
{
        CvSeqReader reader;
        IplImage* cpy = cvCloneImage(img);

        cvStartReadSeq(squares, &reader, 0);

        for (int i = 0; i < squares->total; i++) {
                CvPoint pt[4], *rect = pt;
                int count = 4;
                CV_READ_SEQ_ELEM(pt[0], reader);
                CV_READ_SEQ_ELEM(pt[1], reader);
                CV_READ_SEQ_ELEM(pt[2], reader);
                CV_READ_SEQ_ELEM(pt[3], reader);

                cvLine(cpy, pt[0], pt[2], CV_RGB(0, 0, 0), 1);
                cvLine(cpy, pt[1], pt[3], CV_RGB(255, 255, 0), 1);

                MyCvPoint myCvPoint( (pt[0].x + pt[2].x) /2, (pt[1].y + pt[2].y)/2, img->width, img->height);

                mat.AddMem(myCvPoint);

                cvPolyLine(cpy, &rect, &count, 1, 1, CV_RGB(0, 255, 255), 1, 8, 0);
        }
       // cvShowImage("After Modify", cpy);
        cvReleaseImage(&cpy);
}



#ifdef __cplusplus
}
#endif
