package com.hzwsunshine.freetime.Utils;

import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.View;

/**
 * Created by 何志伟 on 2015/12/26.
 */
public class AnimUtils {

    public static void toLarge(View view){
        if(view==null){
            return;
        }
        view.setVisibility(View.VISIBLE);
        ObjectAnimator animator=ObjectAnimator
                .ofFloat(view,"xxx",0.0F,1.0F)
                .setDuration(500);
        animator.start();
        animator.addUpdateListener(animation -> {
            float val= (float) animation.getAnimatedValue();
            view.setScaleX(val);
            view.setScaleY(val);
        });
    }

    public static void toSmall(View view){
        if(view==null){
            return;
        }
        ObjectAnimator animator=ObjectAnimator
                .ofFloat(view,"xxx",1.0F,0.0F)
                .setDuration(500);
        animator.start();
        animator.addUpdateListener(animation -> {
            float val= (float) animation.getAnimatedValue();
            view.setScaleX(val);
            view.setScaleY(val);
        });
        //        view.setVisibility(View.GONE);
    }
}
