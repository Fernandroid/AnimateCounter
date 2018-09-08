package com.example.asus.animatecounter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import android.view.animation.Interpolator;

import java.util.ArrayList;
import java.util.Random;

public class AnimateCounter {

    /**
     * TextView to be animated
     */
    private TextView mView;
    /**
     * Duration of animation
     */
    private long mDuration;
    /**
     * Initial value to start animation
     */
    private int mStartValue;
    /**
     * End value to finish animation
     */
    private int mEndValue;

    /**
     * Interpolator functionality to apply to animation
     */
    private Interpolator mInterpolator;
    private ValueAnimator mValueAnimator;

    /**
     * Provides optional callback functionality on completion of animation
     */
    private AnimateCounterListener mListener;

    /**
     * Number of times to repeat the animation
     */
    private int mRepeatCount;

    /**
     * Repeat mode when RepeatCount is >0. Repeat mode: RESTART or REVERSE
     */
    private int mRepeatMode;

    private Interpolator mRandomInterpolator;
    private ValueAnimator mAnimatorRandomNumber;
    private int mRandomNumber;
    private ArrayList<Integer> mExclude;

    private AnimateCounter(Builder builder) {
        mView = builder.mView;
        mDuration = builder.mDuration;
        mStartValue = builder.mStartValue;
        mEndValue = builder.mEndValue;
        mInterpolator = builder.mInterpolator;
        mRepeatCount = builder.mRepeatCount;
        mRepeatMode = builder.mRepeatMode;
        mRandomInterpolator = builder.mRandomInterpolator;
        mExclude = builder.mExclude;
    }

    public static int getRandomNumber(int min,
                                      int max, ArrayList<Integer> exclude) {
        int r;
        Random random = new Random();

        if (exclude.size() != 0 || !exclude.isEmpty()) {
            exclude.add(0);
        }
        int range = max - min;
        if (range > 0) {
            do {
                r = random.nextInt(range + 1) + min;
                Log.i("function", "random number: " + Integer.toString(r));
            } while (r < min || r > max || exclude.contains(r));
            return r;
        } else {  // range not representable as int
            do {
                r = random.nextInt();
            } while (r < min || r > max || exclude.contains(r));
            return r;

        }
    }

    /**
     * Call to execute the animation
     */
    public void execute() {
        // Get random number between mStartValue and mEndValue
        mRandomNumber = AnimateCounter.getRandomNumber(mStartValue, mEndValue, mExclude);
        Log.i("function", "end number: " + Integer.toString(mEndValue));
        // Animation for the initial counter
        mValueAnimator = ValueAnimator.ofInt(mStartValue, mEndValue);
        mValueAnimator.setDuration(mDuration);
        mValueAnimator.setInterpolator(mInterpolator);
        mValueAnimator.setRepeatCount(mRepeatCount);
        mValueAnimator.setRepeatMode(mRepeatMode);

        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int current = (int)valueAnimator.getAnimatedValue();
                mView.setText(String.valueOf(current));
            }
        });

        // Animation for the random counter
        mAnimatorRandomNumber = ValueAnimator.ofInt(mStartValue, mRandomNumber);
        mAnimatorRandomNumber.setDuration(2000);
        mAnimatorRandomNumber.setInterpolator(mRandomInterpolator);
        mAnimatorRandomNumber.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int current = (int)valueAnimator.getAnimatedValue();
                mView.setText(String.valueOf(current));
            }
        });

        //Coordinate the two animations
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(mAnimatorRandomNumber).after(mValueAnimator);

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mListener != null) {
                    mListener.onAnimateCounterEnd();
                }
            }
        });

        animatorSet.start();
    }

    /**
     * Stop the current animation
     */
    public void stop() {
        if (mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
        }
    }

    /**
     * Set a listener to get notification of completion of animation
     *
     * @param listener AnimationCounterListener to be used for callbacks
     */
    public void setAnimateCounterListener(AnimateCounterListener listener) {
        mListener = listener;
    }

    /**
     * Callback interface for notification of animation end
     */
    public interface AnimateCounterListener {
        void onAnimateCounterEnd();
    }

    public static class Builder {
        private long mDuration = 2000;
        private int mStartValue = 0;
        private int mEndValue = 10;
        private Interpolator mInterpolator = null;
        private TextView mView;
        private int mRepeatCount = 0;
        private int mRepeatMode = ValueAnimator.RESTART;
        private Interpolator mRandomInterpolator = null;
        private ArrayList<Integer> mExclude = new ArrayList<Integer>();


        public Builder(@NonNull TextView view) {
            if (view == null) {
                throw new IllegalArgumentException("View can not be null");
            }
            mView = view;
        }

        /**
         * Set the start and end integers to be animated
         *
         * @param start initial value
         * @param end   final value
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setCount( int start, int end) {
            if (start == end) {
                throw new IllegalArgumentException("Count start and end must be different");
            }

            mStartValue = start;
            mEndValue = end;
            return this;
        }

        /**
         * Set the duration of the animation from start to end
         *
         * @param duration total duration of animation in ms
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setDuration(long duration) {
            if (duration <= 0) {
                throw new IllegalArgumentException("Duration must be positive value");
            }
            mDuration = duration;
            return this;
        }

        /**
         * Set the interpolator to be used with the initial counter animation
         *
         * @param interpolator Optional interpolator to set
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setInterpolator(@Nullable Interpolator interpolator) {
            mInterpolator = interpolator;
            return this;
        }
        /**
         * Set the interpolator to be used with the random counter animation
         *
         * @param interpolator Optional interpolator to set
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setRandomInterpolator(@Nullable Interpolator interpolator) {
            mRandomInterpolator = interpolator;
            return this;
        }

        /**
         * Set the RepeatCount to repeat the animation
         *
         * @param times Optional number of times to repeat the animation to set
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setRepeatCount(int times) {
            mRepeatCount = times;
            return this;
        }

        /**
         * Set the RepeatMode to be used with the animation
         *
         * @param mode Optional repeat mode for the animation to set. Repeat mode: RESTART or REVERSE:
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setRepeatMode(int mode) {
            mRepeatMode = mode;
            return this;
        }

        /**
         * Set the RepeatMode to be used with the animation
         *
         * @param exclude Optional exclude numbers not to be drawn.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setExcludeNumber(ArrayList<Integer> exclude) {
            mExclude = exclude;
            return this;
        }

        /**
         * Creates a {@link AnimateCounter} with the arguments supplied to this builder. It does not
         * {@link AnimateCounter#execute()} the AnimationCounter.
         * Use {@link #execute()} to start the animation
         */
        public AnimateCounter build() {
            return new AnimateCounter(this);
        }
    }


}
