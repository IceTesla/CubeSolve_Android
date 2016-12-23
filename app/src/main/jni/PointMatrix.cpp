/*
 * PointMatrix.cpp
 *
 *  Created on: Jan 11, 2016
 *      Author: corvo
 */

#include "PointMatrix.h"
#include "MyCvPoint.h"

extern bool compY(const MyCvPoint &p1, const MyCvPoint &p2);

#ifdef __cplusplus
extern "C" {
#endif

PointMatrix::PointMatrix()
{
	PointVtr.clear();
	is_in_order = false;
}

void PointMatrix::AddMem(const MyCvPoint & pt2)
{
	vector<MyCvPoint>::iterator iter;

	/*
	 * if The vector is empty just insert will be comforable (vector为空时)
	 */
	if (PointVtr.empty()) {
		PointVtr.push_back(pt2);
#ifdef _DEBUG
		printf("This is first add mem  ");
		for (iter = PointVtr.begin(); iter < PointVtr.end(); ++iter) {
			printf("(%d, %d)", (*iter).x, (*iter).y);
		}
		printf("\n");
#endif
		return;
	}

	bool isExist = 0;
	for (iter = PointVtr.begin(); iter < PointVtr.end(); ++iter) {		// 是否已经存在
		if ((*iter) == pt2) {
			isExist = 1;
		}
	}

	if (isExist) {
		if (PointVtr.size() >= 10) {
			printf("The wrong has happend\n");
		}
		if (PointVtr.size() == 9 && is_in_order == false) {
			printf("we call makesort\n");
			makeSort();
			is_in_order = true;
		}
		return;
	}

	bool hasInsert = false;
#ifdef _DEBUG
	printf("Current data is (%d, %d); \n", pt2.x, pt2.y);
#endif

	for (iter = PointVtr.begin(); iter < PointVtr.end(); ++iter) {
		if ((*iter) < pt2) {
			PointVtr.insert(iter, pt2);
			hasInsert = true;
			break;
		}
	}

	if (hasInsert == false) {
		PointVtr.push_back(pt2);
	}

#ifdef _DEBUG
	for (iter = PointVtr.begin(); iter < PointVtr.end(); ++iter) {

		printf("(%d, %d)->", (*iter).x, (*iter).y);
	}
	printf("\n");
#endif

}

void PointMatrix::makeSort()
{
	vector<MyCvPoint>::iterator it;
	for (it = PointVtr.begin(); it < PointVtr.end(); it += 3) {
		printf("(%d, %d), (%d, %d) (%d, %d)\n",
				it->x, it->y,
		                (it + 1)->x, (it + 1)->y,
		                (it + 2)->x, (it + 2)->y);
		sort(it, it + 3, compY);
	}

	printf("After sort\n");
	for (it = PointVtr.begin(); it < PointVtr.end(); it += 3) {
		printf("(%d, %d), (%d, %d) (%d, %d)\n", (*it).x, (*it).y,
		                (it + 1)->x, (it + 1)->y, (it + 2)->x,
		                (it + 2)->y);
	}

}

string BuiltResult(PointMatrix &mtx)
{
	string res;
	if(mtx.PointVtr.size() != 9) {
		res = "Please show another Picture";
	} else {
		vector<MyCvPoint>::iterator it;
		char buf[50];
		res = "";
		for (it = mtx.PointVtr.begin(); it < mtx.PointVtr.end(); it += 3) {
			sprintf(buf, "(%d, %d), (%d, %d) (%d, %d)\n", (*it).x, (*it).y,
			                (it + 1)->x, (it + 1)->y, (it + 2)->x,
			                (it + 2)->y);
			res += buf;
                }
	}


	return res;
}



#ifdef __cplusplus
}
#endif
