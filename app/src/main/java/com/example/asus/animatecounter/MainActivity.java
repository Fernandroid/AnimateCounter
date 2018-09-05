package com.example.asus.animatecounter;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ArrayList<Integer>exclude=new ArrayList<Integer>();
        exclude.add(5);

        /*

        Example 1: Count from 1 to 20 in five seconds using the default AccelerateDecelerateInterpolator interpolation.
         */
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView text = findViewById(R.id.tv_count);
                int number=getRandomNumber(1,17,exclude);
                AnimateCounter animateCounter = new AnimateCounter.Builder(text)
                        .setCount(1, 20)
                        .setDuration(5000)
                        .setRepeatCount(3)
                        .setRepeatMode(ValueAnimator.REVERSE)
                        .build();
                animateCounter.execute();
            }
        });
        Button randomButton = findViewById(R.id.random_button);
        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number=getRandomNumber(1,20,exclude);
                Log.i("MainActivity","random number: "+Integer.toString(number));
                TextView text=findViewById(R.id.textView);
                text.setText(Integer.toString(number));
            }
        });
    }


    public int getRandomNumber(int min,
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
                Log.i("function","random number: "+Integer.toString(r));
            } while (r < min || r > max || exclude.contains(r));
            return r;
        } else {  // range not representable as int
            do {
                r = random.nextInt();
            } while (r < min || r > max || exclude.contains(r));
            return r;

        }
    }
}


/*

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