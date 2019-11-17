package in.incourt.incourtnews.core.sql;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bhavan on 1/16/17.
 */

public abstract class IncourtSQLiteHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "incourt_sql";
    public static int VERSION = 1;

    public IncourtSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }



}
