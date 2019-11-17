package in.incourt.incourtnews.anim;

import android.graphics.Color;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import in.incourt.incourtnews.layouts.CircleAnimationView;

/**
 * Created by bhavan on 3/13/17.
 */

public class CircleAngleAnimation extends Animation implements Animation.AnimationListener {

    private CircleAnimationView circle;
    private float oldAngle;
    private float newAngle;

    public CircleAngleAnimation(CircleAnimationView circle, int newAngle) {
        this.oldAngle = circle.getAngle();
        this.newAngle = newAngle;
        this.circle = circle;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation) {
        float angle = oldAngle + ((newAngle - oldAngle) * interpolatedTime);
        circle.setAngle(angle);
        circle.requestLayout();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if(circle.getArc_color() == Color.RED){ circle.setArc_color(Color.TRANSPARENT); }
        else{ circle.setArc_color(Color.RED); }
        circle.refreshDrawableState();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
