package in.incourt.incourtnews.helpers;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by bhavan on 2/13/17.
 */

public class IncourtLoader extends ProgressDialog {

    static IncourtLoader incourtLoader;

    public IncourtLoader(Context context) {
        super(context);
    }

    public IncourtLoader(Context context, int theme) {
        super(context, theme);
    }

    public static IncourtLoader init(Context context){
        incourtLoader = new IncourtLoader(context);
        incourtLoader.setProgressStyle(IncourtLoader.STYLE_SPINNER);
        incourtLoader.setCancelable(false);
        incourtLoader.setMessage("Loading...");
        return incourtLoader;
    }

    public static void init(Context context, String msg){
        incourtLoader = new IncourtLoader(context);
        incourtLoader.setMessage(msg);
    }

    public IncourtLoader start(){
        incourtLoader.show();
        return this;
    }

    public void stop(){
        incourtLoader.hide();
    }

}
