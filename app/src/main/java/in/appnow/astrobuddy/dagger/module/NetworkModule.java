package in.appnow.astrobuddy.dagger.module;

import android.content.Context;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import java.io.File;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import in.appnow.astrobuddy.BuildConfig;
import in.appnow.astrobuddy.dagger.scope.AppScope;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.DateTimeConverter;
import in.appnow.astrobuddy.rest.RestUtils;
import in.appnow.astrobuddy.rest.SslUtils;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Module
public class NetworkModule {

    @AppScope
    @Provides
    public Cache cache(File cacheFile) {
        return new Cache(cacheFile, 10 * 1024 * 1024);//10MB Cache
    }

    @AppScope
    @Provides
    public File cacheFile(Context context) {
        return new File(context.getCacheDir(), "okhttp_cache");
    }

    @AppScope
    @Provides
    public OkHttpClient okHttpClient(HttpLoggingInterceptor loggingInterceptor, Cache cache, Context context, PreferenceManger preferenceManger) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();

        if (SslUtils.INSTANCE.getTrustAllHostsSSLSocketFactory() != null) {
            client.sslSocketFactory(SslUtils.INSTANCE.getTrustAllHostsSSLSocketFactory());
        }

        client.sslSocketFactory(SslUtils.INSTANCE.getSslContextForCertificateFile(context).getSocketFactory());
        client.readTimeout(5, TimeUnit.MINUTES)
                .connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("X-Token", preferenceManger.getStringValue(PreferenceManger.AUTH_TOKEN))
                            .addHeader("X-Access-token", preferenceManger.getStringValue(PreferenceManger.NODE_AUTH_TOKEN))
                            .addHeader("X-Key",RestUtils.getAstroBlurtAPIKey()).build();
                    return chain.proceed(request);
                })
                .addInterceptor(loggingInterceptor)
                .cache(cache);
        return client.build();
      /*  return new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.MINUTES)
                .connectTimeout(5, TimeUnit.MINUTES)
                .addInterceptor(loggingInterceptor)
                .cache(cache)
                .build();*/
    }

    @AppScope
    @Provides
    public HttpLoggingInterceptor httpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        return interceptor;
    }

    @AppScope
    @Provides
    public Retrofit retrofit(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(RestUtils.getEndPoint())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    @AppScope
    @Provides
    public APIInterface apiInterface(Retrofit retrofit) {
        return retrofit.create(APIInterface.class);
    }

    @AppScope
    @Provides
    public Gson gson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        //gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        gsonBuilder.registerTypeAdapter(DateTime.class, new DateTimeConverter());
        gsonBuilder.create();
        return Converters.registerAll(gsonBuilder)
                .create();
    }




}
