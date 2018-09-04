package com.example.asus.animatecounter;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*

        Example 1: Count from 1 to 20 in five seconds using the default AccelerateDecelerateInterpolator interpolation.
         */
        Button button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView text=findViewById(R.id.tv_count);
                AnimateCounter animateCounter = new AnimateCounter.Builder(text)
                        .setCount(1, 20)
                        .setDuration(5000)
                        .setRepeatCount(3)
                        .setRepeatMode(ValueAnimator.REVERSE)
                        .build();
                animateCounter.execute();
            }
        });
    }
}
/*
.setRepeatCount(3)
.setRepeatMode(ValueAnimator.REVERSE)

final float startSize = o; // Size in pixels
    final float endSize = 30;
    final int animationDuration = 1000; // Animation duration in ms

    ValueAnimator animator = ValueAnimator.ofFloat(startSize, endSize);
    animator.setDuration(animationDuration);

    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float animatedValue = (float) valueAnimator.getAnimatedValue();
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, animatedValue);
        }
    });

    animator.start();
 */