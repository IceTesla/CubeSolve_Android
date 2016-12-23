/*
 * MyCvPoint.cpp
 *
 *  Created on: Jan 11, 2016
 *      Author: corvo
 */


#include "MyCvPoint.h"

MyCvPoint::MyCvPoint(int x, int y, int width, int height)
{
	this->x = x;
	this->y = y;
	this->cz.width = width;
	this->cz.height = height;
}

MyCvPoint::MyCvPoint(const MyCvPoint &pt)
{
	this->x = pt.x;
	this->y = pt.y;
	this->cz.width = pt.cz.width;
	this->cz.height = pt.cz.height;
}

bool MyCvPoint::operator <(const CvPoint &p2) const
{
	bool isSmaller = 0;
	if (this->x < p2.x) {
		isSmaller = 1;
	}
	return isSmaller;
}

MyCvPoint & MyCvPoint::operator=(const MyCvPoint &pt2)
{
	if (this == &pt2) {
		return *this;
	}

	this->x = pt2.x;
	this->y = pt2.y;
	return *this;
}

bool MyCvPoint::operator ==(const MyCvPoint &pt2)
{
	bool isSimilar = false;
	int narrow = (cz.height >= cz.width) ? cz.width : cz.height;

	if (fabs(this->x - pt2.x) <= narrow / 8
	                && fabs(this->y - pt2.y) <= narrow / 8)
	{
		isSimilar = true;
	}
	return isSimilar;
}

bool compY(const MyCvPoint &p1, const MyCvPoint &p2)
{
	if (p1.y > p2.y) {
		return true;
	} else {
		return false;
	}
}

