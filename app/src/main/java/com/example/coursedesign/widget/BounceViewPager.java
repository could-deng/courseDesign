package com.example.coursedesign.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.animation.TranslateAnimation;

public class BounceViewPager extends ViewPager {

    private Rect mRect = new Rect();//用来记录初始位置  
    private int pagerCount = 3;
    private int currentItem = 0;
    private boolean handleDefault = true;
    private float preX = 0f;
    private static final float RATIO = 0.5f;//摩擦系数  
    private static final float SCROLL_WIDTH = 30f;
    private boolean bPage0Selected = false;

    public BounceViewPager(Context context) {
        super(context);
    }

    public BounceViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置总共有多少页,请记得调用它*/
    public void setPagerCount(int pagerCount) {
        this.pagerCount = pagerCount;
    }

    /**这是当前是第几页，请在onPageSelect方法中调用它。*/
    public void setCurrentIndex(int currentItem) {
        this.currentItem = currentItem;
        if(currentItem == 0)bPage0Selected = true;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent motion) {
        if (motion.getAction() == MotionEvent.ACTION_DOWN) {
            preX = motion.getX();//记录起点  
        }
        return super.onInterceptTouchEvent(motion);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motion) {
        switch (motion.getAction()) {
            case MotionEvent.ACTION_UP:
                onTouchActionUp();
                break;
            case MotionEvent.ACTION_MOVE:
                //当时滑到第一项或者是最后一项的时候。
                if ((currentItem == 0 || currentItem == pagerCount - 1)) {
                    if((currentItem == 0) && (!bPage0Selected)){
                        handleDefault = true;
                        break;
                    }
                    float nowX = motion.getX();
                    float offset = nowX - preX;
                    preX = nowX;
                    if (currentItem == 0) {
                        if (offset > SCROLL_WIDTH) {//手指滑动的距离大于设定值
                            whetherConditionIsRight(offset);
                        } else if (!handleDefault) {//这种情况是已经出现缓冲区域了，手指慢慢恢复的情况
                            if (getLeft() + (int) (offset * RATIO) >= mRect.left) {
                                layout(getLeft() + (int) (offset * RATIO), getTop(), getRight() + (int) (offset * RATIO), getBottom());
                            }
                        }
                    } else {
                        if (offset < -SCROLL_WIDTH) {
                            whetherConditionIsRight(offset);
                        } else if (!handleDefault) {
                            if (getRight() + (int) (offset * RATIO) <= mRect.right) {
                                layout(getLeft() + (int) (offset * RATIO), getTop(), getRight() + (int) (offset * RATIO), getBottom());
                            }
                        }
                    }
                } else {
                    handleDefault = true;
                }

                if (!handleDefault) {
                    return true;
                }
                break;

            default:
                break;
        }
        return super.onTouchEvent(motion);
    }

    private void whetherConditionIsRight(float offset) {
        if (mRect.isEmpty()) {
            mRect.set(getLeft(), getTop(), getRight(), getBottom());
        }
        handleDefault = false;
        layout(getLeft() + (int) (offset * RATIO), getTop(), getRight() + (int) (offset * RATIO), getBottom());
    }

    private void onTouchActionUp() {
        if (!mRect.isEmpty()) {
            recoveryPosition();
        }
    }

    private void recoveryPosition() {
        TranslateAnimation ta = new TranslateAnimation(getLeft(), mRect.left, 0, 0);
        ta.setDuration(300);
        startAnimation(ta);
        layout(mRect.left, mRect.top, mRect.right, mRect.bottom);
        mRect.setEmpty();
        handleDefault = true;
    }
}
