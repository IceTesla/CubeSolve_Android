/*
 * PointMatrix.h
 *
 *  Created on: Jan 11, 2016
 *      Author: corvo
 */

#ifndef POINTMATRIX_H_
#define POINTMATRIX_H_

#include <vector>
#include "opencv2/core/core.hpp"
#include "MyCvPoint.h"
#include <algorithm>

#ifdef __cplusplus
extern "C" {
#endif

using namespace std;

// 只是用来添加元素, 使用vector存储数据
class PointMatrix {
public:
	vector<MyCvPoint> PointVtr;
	bool is_in_order;				// 是否为有序状态
	PointMatrix();

	void AddMem(const MyCvPoint & pt2);
	friend string BuiltResult(PointMatrix &mtx);
private:
	void makeSort();
};
#ifdef __cplusplus
}
#endif

#endif /* POINTMATRIX_H_ */
