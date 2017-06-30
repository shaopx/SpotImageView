package com.spx.spotimageview;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.RelativeLayout;

/**
 * Created by SHAOPENGXIANG on 2017/6/30.
 */
public class SIRelativeLayout extends RelativeLayout {
    private static final String TAG = "SIRelativeLayout";
    private static final long EXIT_TIME = 200;

    private float startY;
    private int myToll;

    private PinchImageView target;
    private SpotImageActivity activity;

    private int oldX, oldY;


    public SIRelativeLayout(Context context) {
        super(context);
        init(context);
    }

    public SIRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SIRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if (context != null && context instanceof SpotImageActivity) {
            activity = (SpotImageActivity) context;
        }
    }

    public void setLocation(int x, int y) {
        oldX = x;
        oldY = y;
    }

    public void setScaleTarget(View view) {
        this.target = (PinchImageView) view;
    }

    public void exit(boolean scaleFlag) {
        target.animate().alpha(0).setDuration(180).start();

        if (scaleFlag) {
            int[] mylocation = new int[2];
            this.getLocationOnScreen(mylocation);
//            Log.d(TAG, "exit: mylocation x:" + mylocation[0] + " , y:" + mylocation[1]);
            int ydiff = mylocation[1] - oldY;
            int currentTransY = (int) getTranslationY();
            ViewPropertyAnimator viewPropertyAnimator = this.animate().translationY(currentTransY - ydiff).setDuration(EXIT_TIME);
            viewPropertyAnimator.start();
        }

        ViewPropertyAnimator viewPropertyAnimator = this.animate().alpha(0).setDuration(EXIT_TIME);
        viewPropertyAnimator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (activity != null) {
                    activity.finish();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        viewPropertyAnimator.start();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        int touchCount = event.getPointerCount();
//        Log.d(TAG, "onInterceptTouchEvent: ev:" + event.getAction() + ", action:" + action + ", y:" + event.getY() + ", touchCount:" + touchCount +", mode:"+target.getPinchMode());

        if (target.getPinchMode() == PinchImageView.PINCH_MODE_SCROLL) {
            startY = -1;
            return false;
        }

        if (touchCount > 1) {
            startY = -1;
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startY = event.getY();
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_UP) {

            if (startY == -1) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    startY = event.getY();
                }
                return false;
            }

            float movement = event.getY() - startY;
            float alp = 1.0f - Math.abs(movement / myToll);

            float viewAlpha = alp < 0.5f ? 0.5f : alp;
            this.setAlpha(viewAlpha);
//            Log.d(TAG, "onTouch: movment:" + movement + ", alp:" + alp);
            target.setTranslationY(movement);


            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (alp < 0.5f) {
                    exit(false);
                } else {
                    float lastY = event.getY();
                    target.animate().translationY(0).alpha(1).setDuration(200).start();
                    this.animate().alpha(1).setDuration(200).start();
                }
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        int touchCount = event.getPointerCount();
        Log.d(TAG, "onTouchEvent: ev:" + event.getAction() + ", action:" + action + ", y:" + event.getY() + ", touchCount:" + touchCount);

        return false;
    }
}
