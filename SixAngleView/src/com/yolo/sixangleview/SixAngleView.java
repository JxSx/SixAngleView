package com.yolo.sixangleview;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class SixAngleView extends ViewGroup {

	private static final String TAG = "SpecialView";
	private ArrayList<CircleCenteter> centers = new ArrayList<SixAngleView.CircleCenteter>();
	private int mChildCount;
	private int childeWidth;
	private int childHeight;
	private int radius;

	public SixAngleView(Context context, AttributeSet attrs) {

		super(context, attrs);
	}

	public SixAngleView(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 根据测量规范获取到布局宽高大小
		int wSize = MeasureSpec.getSize(widthMeasureSpec);
		int hSize = MeasureSpec.getSize(heightMeasureSpec);
		// 这个方法必须在onMeasure中调用，用于储存测量的宽度和高度。如果不调用则会抛出异常
		setMeasuredDimension(wSize, hSize);

		// 计算此ViewGroup中心点坐标
		int centerX = wSize / 2;
		int centerY = hSize / 2-10;
		radius = wSize / 6;
		childeWidth = 2 * radius;
		childHeight = 2 * radius;

		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			View childView = getChildAt(i);
			childView.measure(childeWidth, childHeight);
		}
		if (mChildCount != childCount) {
			mChildCount = childCount;
		}

		calculateOtherCenters(centerX, centerY, radius);
		Log.i(TAG, "onMeasure()--measuredWidth=" + wSize + "measuredHeight"
				+ hSize + "radius" + radius);
	}

	/**
	 * 计算周围六个子view的中心点坐标
	 * 
	 * @param a
	 *            中心点x坐标
	 * @param b
	 *            中心点y坐标
	 * @param r
	 *            childView的半径
	 */
	private void calculateOtherCenters(double a, double b, double r) {
		double sqrt3 = Math.sqrt(3);
//		// 中心点
//		CircleCenteter c0 = new CircleCenteter(a, b);

		// 中心点左边的点
		CircleCenteter c1 = new CircleCenteter(a - 2 * r, b);
		// 中心点左上角的点（顺时针）
		CircleCenteter c2 = new CircleCenteter(a - r, b - sqrt3 * r);
		CircleCenteter c3 = new CircleCenteter(a + r, b - sqrt3 * r);
		CircleCenteter c4 = new CircleCenteter(a + 2 * r, b);
		CircleCenteter c5 = new CircleCenteter(a + r, b + sqrt3 * r);
		CircleCenteter c6 = new CircleCenteter(a - r, b + sqrt3 * r);

//		centers.add(c0);
		centers.add(c1);
		centers.add(c2);
		centers.add(c3);
		centers.add(c4);
		centers.add(c5);
		centers.add(c6);
	}

	class CircleCenteter {
		double x, y;

		public CircleCenteter(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		//获取每个孩子设置布局
		int childLeft, childTop;
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View child = getChildAt(i);
			childLeft = (int) (centers.get(i).x - radius);
			childTop = (int) (centers.get(i).y - radius);
			child.layout(childLeft, childTop, childLeft + childeWidth, childTop
					+ childHeight);
		}
	}
}
