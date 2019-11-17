package in.incourt.incourtnews.anim;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by gurpreetsingh on 03/03/17.
 */

public class RotateAnimation extends AnimationUtils implements Animation.AnimationListener {

    View view;
    Animation animation;

    public RotateAnimation(Context context) {
        animation = loadAnimation(context, in.incourt.incourtnews.R.anim.rotate_anim);
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

    public Animation getAnimation(View view) {
        this.view = view;
        return animation;
    }
}

