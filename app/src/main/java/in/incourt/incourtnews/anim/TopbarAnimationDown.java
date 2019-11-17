package in.incourt.incourtnews.anim;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by bhavan on 2/27/17.
 */

public class TopbarAnimationDown extends AnimationUtils implements Animation.AnimationListener {

    View view;

    Animation animation;

    public TopbarAnimationDown(Context context) {
        animation = loadAnimation(context, in.incourt.incourtnews.R.anim.top_anim_down);
        animation.setAnimationListener(this);
    }

    @Override
    public void onAnimationStart(Animation animation) {
        view.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public Animation getAnimation(View view){
        this.view = view;
        return animation;
    }
}
