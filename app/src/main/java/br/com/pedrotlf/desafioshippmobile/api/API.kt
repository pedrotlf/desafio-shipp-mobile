package br.com.pedrotlf.desafioshippmobile.api

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface API {

    @Headers("Accept: application/json")
    @POST("order/resume")
    fun postResume(@Body token: ResumeRequest): Call<ResumeResponse>

    companion object {
        fun create(urlBase: String): API {
            val client = OkHttpClient()
                    .newBuilder()
                    .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .build()

            return Retrofit.Builder()
                    .baseUrl(urlBase)
                    .addConverterFactory(JacksonConverterFactory.create(jacksonObjectMapper()
                            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                            .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
                            .setSerializationInclusion(JsonInclude.Include.NON_NULL))
                    )
                    .client(client)
                    .build()
                    .create(API::class.java)
        }
    }
}