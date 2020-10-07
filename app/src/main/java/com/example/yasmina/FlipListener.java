package com.example.yasmina;

import android.animation.ValueAnimator;
import android.view.View;
import android.widget.LinearLayout;

public class FlipListener implements ValueAnimator.AnimatorUpdateListener {
      View mFrontView;
      View mBackView;
    private boolean mFlipped;
    public FlipListener(View linearLayoutFront, View linearLayoutBack) {
        mFrontView=linearLayoutFront;
        mBackView=linearLayoutBack;
        this.mBackView.setVisibility(View.GONE);

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        final float value = animation.getAnimatedFraction();
        final float scaleValue = 0.625f + (1.5f * (value - 0.5f) * (value - 0.5f));

        if(value <= 0.5f){
            this.mFrontView.setRotationY(180 * value);
            this.mFrontView.setScaleX(scaleValue);
            this.mFrontView.setScaleY(scaleValue);
            if(mFlipped){
                setStateFlipped(false);
            }
        } else {
            this.mBackView.setRotationY(-180 * (1f- value));
            this.mBackView.setScaleX(scaleValue);
            this.mBackView.setScaleY(scaleValue);
            if(!mFlipped){
                setStateFlipped(true);
            }
        }
    }
    private void setStateFlipped(boolean flipped) {
        mFlipped = flipped;
        this.mFrontView.setVisibility(flipped ? View.GONE : View.VISIBLE);
        this.mBackView.setVisibility(flipped ? View.VISIBLE : View.GONE);
    }
}
