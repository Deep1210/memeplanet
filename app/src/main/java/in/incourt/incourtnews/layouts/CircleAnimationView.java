package in.incourt.incourtnews.layouts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.RelativeLayout;

/**
 * Created by bhavan on 3/13/17.
 */

public class CircleAnimationView extends RelativeLayout {

    private static final int START_ANGLE_POINT = 0;
    private final Paint paint;
    private final RectF rect;
    private float angle;
    int arc_color = Color.parseColor("#2A2A2A");
    public int strokeWidth = 2;
    float circlesize;

    public CircleAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        int dpSize = 10;

        DisplayMetrics dm = getResources().getDisplayMetrics();
        circlesize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpSize, dm);
        paint = new Paint();
        paint.setAntiAlias(false);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(getArc_color());
        rect = new RectF(strokeWidth, strokeWidth, strokeWidth, strokeWidth);
        angle = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int height = canvas.getHeight() / 2;
        int width = canvas.getWidth() / 2;
        rect.set(width - circlesize, height - circlesize, width + circlesize, height + circlesize);
        canvas.drawArc(rect, START_ANGLE_POINT, angle, false, paint);

    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public int getArc_color() {
        return arc_color;
    }

    @Override
    public int getForegroundGravity() {
        return super.getForegroundGravity();
    }

    public void setArc_color(int arc_color) {
        this.arc_color = arc_color;
    }

    @Override
    protected void onAnimationEnd() {
        super.onAnimationEnd();
    }
}
