package com.sam.umain.di

import com.sam.umain.data.network.ApiService
import com.sam.umain.data.repository.RepositoryImpl
import com.sam.umain.domain.repository.Repository
import com.sam.umain.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.Interceptor.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {


    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val clientInterceptor = Interceptor { chain: Chain ->
            var request = chain.request()
            val url = request.url.newBuilder()
                .addQueryParameter("Accept", "application/json")
                .addQueryParameter("Connection", "close")
                .build()
            request = request.newBuilder().url(url).build()
            chain.proceed(request)
        }

        val okHttpClient: OkHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(50, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)
            .writeTimeout(50, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addNetworkInterceptor(clientInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesRepository(flickrApi: ApiService): Repository {
        return RepositoryImpl(flickrApi)
    }

}