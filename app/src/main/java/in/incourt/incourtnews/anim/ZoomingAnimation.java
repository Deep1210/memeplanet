package in.incourt.incourtnews.anim;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by gurpreetsingh on 03/03/17.
 */

public class ZoomingAnimation extends AnimationUtils implements Animation.AnimationListener {

    View view;
    Animation animation;

    public ZoomingAnimation(Context context) {
        animation = loadAnimation(context, in.incourt.incourtnews.R.anim.zooming_anim);
        animation.setAnimationListener(this);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public Animation getAnimation() {
        return animation;
    }
}

