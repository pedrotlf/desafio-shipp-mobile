package br.com.pedrotlf.desafioshippmobile.dependencyinjection

import br.com.pedrotlf.desafioshippmobile.api.OrderApi
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val client = OkHttpClient()
                .newBuilder()
                .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build()

        return Retrofit.Builder()
                .baseUrl(OrderApi.baseUrl)
                .addConverterFactory(JacksonConverterFactory.create(jacksonObjectMapper()
                        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                        .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
                        .setSerializationInclusion(JsonInclude.Include.NON_NULL))
                )
                .client(client)
                .build()
    }

    @Provides
    @Singleton
    fun provideOrderApi(retrofit: Retrofit): OrderApi =
            retrofit.create(OrderApi::class.java)
}