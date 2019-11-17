package in.incourt.incourtnews.others;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebResourceResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import okhttp3.HttpUrl;
import okio.BufferedSource;
import okio.Okio;

public class AdBlocker {
    private static final String AD_HOSTS_FILE = "test_url.txt";
    private static final Set<String> AD_HOSTS = new HashSet<>();

    public static void init(final Context context) {
        Log.e("Hello in adblocker ","");
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Log.e("Hello in adblocker ","async");
                    loadFromAssets(context);
                } catch (IOException e) {
                    // noop
                }
                return null;
            }
        }.execute();
    }

    @WorkerThread
    private static void loadFromAssets(Context context) throws IOException {
        Log.e("Hello in adblocker ","asstes");
        InputStream stream = context.getAssets().open(AD_HOSTS_FILE);
        Log.e("Hello in adblocker ","asstes123344");
        BufferedSource buffer = Okio.buffer(Okio.source(stream));

        String line;

        while ((line = buffer.readUtf8Line()) != null) {
            Log.e("Hello in adblocker ","asstes"+line);
            AD_HOSTS.add(line);
        }
        buffer.close();
        stream.close();
    }

    public static boolean isAd(String url) {
        HttpUrl httpUrl = HttpUrl.parse(url);
        return isAdHost(httpUrl != null ? httpUrl.host() : "");
    }

    private static boolean isAdHost(String host) {
        if (TextUtils.isEmpty(host)) {
            return false;
        }
        int index = host.indexOf(".");
        return index >= 0 && (AD_HOSTS.contains(host) ||
                index + 1 < host.length() && isAdHost(host.substring(index + 1)));
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static WebResourceResponse createEmptyResource() {
        return new WebResourceResponse("text/plain", "utf-8", new ByteArrayInputStream("".getBytes()));
    }


}
