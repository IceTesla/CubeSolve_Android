/*
 * MyCvPoint.h
 *
 *  Created on: Jan 11, 2016
 *      Author: corvo
 */



#ifndef MYCVPOINT_H_
#define MYCVPOINT_H_


#include <stdio.h>
#include "opencv/cv.h"


class MyCvPoint :public CvPoint{
public:
	MyCvPoint(int x, int y, int width, int height);			// Just for Copy
	MyCvPoint(const MyCvPoint &pt);

	MyCvPoint & operator=(const MyCvPoint &pt2);

	bool operator < (const CvPoint &p2) const;				// Compare X

	friend bool compY(const MyCvPoint &p1, const MyCvPoint &p2);	// Compare Y

	bool operator == (const MyCvPoint &pt2);				// 在程序中会有多余的点, 以此来判断

private:
	CvSize cz;

};


#endif /* MYCVPOINT_H_ */
