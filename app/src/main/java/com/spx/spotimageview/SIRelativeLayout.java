package com.spx.spotimageview;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
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
    private Activity activity;


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
        if (context != null && context instanceof Activity) {
            activity = (Activity) context;
        }

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        myToll = metrics.heightPixels / 2;
    }

    public void setScaleTarget(View view) {
        this.target = (PinchImageView) view;
    }

    public void exit(boolean scaleFlag) {
        target.animate().alpha(0).setDuration(EXIT_TIME).start();

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
        if (target.getPinchMode() == PinchImageView.PINCH_MODE_SCROLL || touchCount > 1) {
            startY = -1;
            this.setAlpha(1);
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
            target.setTranslationY(movement);


            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (alp < 0.5f) {
                    exit(false);
                } else {
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

        return false;
    }
}
