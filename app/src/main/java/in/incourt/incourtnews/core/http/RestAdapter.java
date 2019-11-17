package in.incourt.incourtnews.core.http;

import in.incourt.incourtnews.helpers.UserHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by bhavan on 12/26/16.
 */

public class RestAdapter {

    private static IncourtWebServices REST_CLIENT = null;

    private static String ROOT = "http://beta.incourt.in/index.php/api/v1/";
    //private static String ROOT = "http://192.168.0.100/incourt/index.php/api/v1/";


    static {
        setupRestClient();
    }

    private RestAdapter() {
    }

    public static IncourtWebServices get() {
        return REST_CLIENT;
    }

    public static void setupRestClient() {

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        addHeaderToken(httpClient);
        httpClient.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).
                build();


        if (REST_CLIENT == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ROOT)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();


            REST_CLIENT = retrofit.create(IncourtWebServices.class);
        }
    }

    public static void addHeaderToken(OkHttpClient.Builder httpClient) {
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .header("incourt-header-token", String.valueOf(UserHelper.getId()))
                        .method(original.method(), original.body());

                return chain.proceed(requestBuilder.build());
            }
        });
    }

}
