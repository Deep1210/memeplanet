package in.incourt.incourtnews.helpers;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.TypedValue;

/**
 * Created by bhavan on 5/23/17.
 */

public class IncourtColorHelper {

    public static int fetchColor(Context c, int id ) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = c.obtainStyledAttributes( typedValue.data, new int[]{ id } );
        int color = a.getColor( 0, 0 );
        a.recycle();
        return color;
    }

}
