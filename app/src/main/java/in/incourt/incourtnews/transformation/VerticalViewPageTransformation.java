package in.incourt.incourtnews.transformation;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Created by bhavan on 2/24/17.
 */

public class VerticalViewPageTransformation implements ViewPager.PageTransformer {

    private static final float MIN_SCALE = 0.1f;

    @Override
    public void transformPage(View view, float position) {

        if (position < -1) { // [-Infinity,-1)
            view.setAlpha(0);
        } else if (position <= 0) { // [-1,0]
            //Log.e("Position<0 : ",position+"");
            // Use the default slide transition when moving to the left page
            view.setAlpha(1);
            // Counteract the default slide transition
            //Log.e("PositionX: ",""+view.getWidth() * -position);
            view.setTranslationX(view.getWidth() * -position);

            //set Y position to swipe in from top
            float yPosition = position * view.getHeight();
            view.setTranslationY(yPosition);
            view.setScaleX(1);
            view.setScaleY(1);

            //Log.e("Position: ",position+" Y < 0 YPosition: "+yPosition+"  Height:  "+view.getHeight());

        } else if (position <= 1) { // [0,1]

            view.setAlpha(1);
            // Counteract the default slide transition
            view.setTranslationX(view.getWidth() * -position);
            // Scale the page down (between MIN_SCALE and 1)
            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

            // Log.e("Position: ",position+"Y > 1 YPosition: "+scaleFactor);

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }
}