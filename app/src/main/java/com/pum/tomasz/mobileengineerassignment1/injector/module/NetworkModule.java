package com.pum.tomasz.mobileengineerassignment1.injector.module;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pum.tomasz.mobileengineerassignment1.BuildConfig;
import com.pum.tomasz.mobileengineerassignment1.frontend.DataProvider;
import com.pum.tomasz.mobileengineerassignment1.frontend.RetrofitRestDataProvider;
import com.pum.tomasz.mobileengineerassignment1.gson.JsonExclusionStrategy;
import com.pum.tomasz.mobileengineerassignment1.gson.ResponseDeserializer;
import com.pum.tomasz.mobileengineerassignment1.injector.scope.PerApplication;
import com.pum.tomasz.mobileengineerassignment1.model.GithubSearchResultWrapper;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tomasz on 18.10.2017.
 */

@Module
public class NetworkModule {
    public static final FieldNamingPolicy API_JSON_NAMING_POLICY = FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;

    @Provides
    @PerApplication
    DataProvider daggerProvideDataProvider(Retrofit retrofit) {
        return new RetrofitRestDataProvider(retrofit);
    }


    @Provides
    @PerApplication
    Retrofit provideRetrofit() {
        String endpointUrl = BuildConfig.githubApiEndpointUrl;

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(API_JSON_NAMING_POLICY)
                .registerTypeAdapter(GithubSearchResultWrapper.class, new ResponseDeserializer())
                .addSerializationExclusionStrategy(new JsonExclusionStrategy())
                .addDeserializationExclusionStrategy(new JsonExclusionStrategy())
                .create();

        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();
        return new Retrofit.Builder()
                .baseUrl(endpointUrl)
                .client(client)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

}
